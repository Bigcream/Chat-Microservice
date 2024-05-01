package com.message.chatservice.service.impl;

import com.message.chatservice.exception.ChatException;
import com.message.chatservice.exception.ErrorType;
import com.message.chatservice.model.dto.*;
import com.message.chatservice.model.entity.*;
import com.message.chatservice.repository.*;
import com.message.chatservice.service.RoomChatService;
import com.message.chatservice.utils.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.message.chatservice.utils.DateTimeUtil.formatLocalDateTime;
import static com.message.chatservice.utils.DateTimeUtil.today;
import static com.message.chatservice.utils.SerializerUtils.deserializeFromJsonString;

@Service
@RequiredArgsConstructor
public class RoomChatServiceImpl implements RoomChatService {
    private final MessageRepo messageRepo;
    private final RoomMemberRepo roomMemberRepo;
    private final ContactRepo contactRepo;
    private final MessageUnreadRepo messageUnreadRepo;
    private final RoomChatAdminRepo roomChatAdminRepo;
    private final RoomChatRepo roomChatRepo;
    private final ShareFileRepo shareFileRepo;
    private static final String COUPLE_TYPE = "couple";

    @Value("${gate-way-url}")
    private String GATE_WAY_URL;

    @Override
    @Transactional(readOnly = true)
    public List<RoomChatDTO> getRoomChats(String email, long status) {
        Optional<Contact> contact = contactRepo.findByEmail(email);
        if(!contact.isPresent()){
            return new ArrayList<>();
        }
        List<RoomMember> contactMembers = roomMemberRepo.findByContactIdAndStatus(contact.get().getId(), status);
        return contactMembers.stream()
                .map(RoomMember::getRoomChat)
                .map(roomChat -> {
                    return handleDataRoomChat(roomChat, contact.get()).convertDTO();
                }).sorted(Comparator.comparing(RoomChatDTO::getUpdatedDate). reversed()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ConversationResponse getConversationByRoomId(Long roomChatId, String sender) throws ChatException {
        Optional<RoomChat> roomChat= roomChatRepo.findById(roomChatId);
        if(!roomChat.isPresent()){
            throw new ChatException(ErrorType.NOT_FOUND_ROOM_CHAT);
        }
        List<RoomMember> roomMembers = roomMemberRepo.findByRoomChatId(roomChatId);
        List<ContactDTO> contactDTOS = roomMembers.stream().map(roomMember ->
                roomMember.getContact().convertDTO()).collect(Collectors.toList());
        List<Message> messages = messageRepo.findByRoomChatId(roomChatId);
        messages.forEach(message -> message.setDateFormat(formatLocalDateTime(message.getTimeCreated())));
        List<MessageDTO> messageDTOS = messages.stream()
                .sorted(Comparator.comparing(Message::getTimeCreated))
                .map(Message::convertDTO)
                .collect(Collectors.toList());

        if(roomChat.get().getType().equals(COUPLE_TYPE)){
            Optional<ContactDTO> receiver = contactDTOS.stream()
                    .filter(contactDTO -> !contactDTO.getEmail().equals(sender)).findFirst();
            receiver.ifPresent(res -> {
                roomChat.get().setAvatar(res.getAvatar());
                roomChat.get().setName(res.getUsername());
            });
        }

        List<Long> admins = roomChatAdminRepo.findAdminByRoomChatId(roomChatId);
        return ConversationResponse.builder()
                .id(roomChatId)
                .unread(0L)
                .type(roomChat.get().getType())
                .contacts(contactDTOS)
                .messages(messageDTOS)
                .draftMessage("")
                .avatar(roomChat.get().getAvatar())
                .name(roomChat.get().getName())
                .admins(admins)
                .build();
    }

    @Override
    @Transactional
    public MessageUnread readConversation(Long roomChatId, String email) throws ChatException {
        Optional<Contact> contact = contactRepo.findByEmail(email);
        if(!contact.isPresent()){
            throw new ChatException(ErrorType.NOT_FOUND_CONTACT);
        }
        MessageUnread messageUnread = messageUnreadRepo.findByRoomChatIdAndContactId(roomChatId, contact.get().getId());
        messageUnread.setMessageUnread(0L);
        Message message = messageRepo.findByRoomChatIdAndReceiverId(roomChatId, contact.get().getId());
        message.setTimeSeen(today());
        return messageUnreadRepo.save(messageUnread);
    }

    @Override
    public RoomChatDTO updateRoomChatInfo(String roomInfoString, MultipartFile file) throws ChatException {
        RoomInfoDTO roomInfoDTO = deserializeFromJsonString(roomInfoString, RoomInfoDTO.class);
        Optional<RoomChat> roomChatOptional = roomChatRepo.findById(roomInfoDTO.getId());
        if(!roomChatOptional.isPresent()){
            throw new ChatException(ErrorType.NOT_FOUND_ROOM_CHAT);
        }
        if(file == null){
            roomChatOptional.get().setAvatar(roomInfoDTO.getAvatar());
        } else {
            Path path = FileManager.writeFile(file);
            ShareFile shareFile = shareFileRepo.save(ShareFile.builder()
                    .size(file.getSize())
                    .path(path.toString())
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .build());
            roomChatOptional.get().setAvatar(GATE_WAY_URL + "/api/v1/chat/image/" + shareFile.getId());
        }
        roomChatOptional.get().setName(roomInfoDTO.getName() != null ? roomInfoDTO.getName() : roomChatOptional.get().getName());
        return roomChatRepo.save(roomChatOptional.get()).convertDTO();
    }

    @Override
    @Transactional
    public void deleteConversation(Long roomChatId) {
        roomMemberRepo.deleteByRoomChatId(roomChatId);
        roomChatRepo.deleteById(roomChatId);
    }

    @Override
    public List<String> getListRoomToJoin(String email) throws ChatException {
        Optional<Contact> contact = contactRepo.findByEmail(email);
        if(!contact.isPresent()){
            throw  new ChatException(ErrorType.NOT_FOUND_CONTACT);
        }
        List<RoomMember> contactMembers = roomMemberRepo.findByContactId(contact.get().getId());
        return contactMembers.stream().map(RoomMember::getRoomChat).map(RoomChat::getId)
                .map(id -> "room " + id).collect(Collectors.toList());
    }

    @Override
    public RoomChatDTO getRoomChatById(Long roomChatId, String email) throws ChatException {
        Optional<RoomChat> roomChat = roomChatRepo.findById(roomChatId);
        if(!roomChat.isPresent()){
            throw new ChatException(ErrorType.NOT_FOUND_ROOM_CHAT);
        }
        Optional<Contact> contact = contactRepo.findByEmail(email);
        if(!contact.isPresent()){
            return new RoomChatDTO();
        }
        return handleDataRoomChat(roomChat.get(), contact.get()).convertDTO();
    }

    private RoomChat handleDataRoomChat(RoomChat roomChat, Contact contact){
        Optional<Message> message = messageRepo.findById(roomChat.getLatestMessageId());
        message.ifPresent(mes -> {
            message.get().setDateFormat(formatLocalDateTime(message.get().getTimeCreated()));
            roomChat.setMessage(message.get());
        });
        MessageUnread messageUnread = messageUnreadRepo.findByRoomChatIdAndContactId(roomChat.getId(), contact.getId());
        if(roomChat.getType().equals(COUPLE_TYPE)){
            roomChat.setMessageUnread(messageUnread.getMessageUnread());
            Long receiverId = roomMemberRepo.findReceiverIdInRoomCouple(contact.getId(), roomChat.getId());
            Optional<Contact> receiver = contactRepo.findById(receiverId);
            receiver.ifPresent(res -> {
                roomChat.setAvatar(res.getAvatar());
                roomChat.setName(res.getUsername());
            });
        }
        return roomChat;
    }
}

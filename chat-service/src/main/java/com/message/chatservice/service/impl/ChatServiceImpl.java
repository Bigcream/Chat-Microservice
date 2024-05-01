package com.message.chatservice.service.impl;

import com.message.chatservice.constant.RoomStatus;
import com.message.chatservice.exception.ChatException;
import com.message.chatservice.exception.ErrorType;
import com.message.chatservice.kafka.producer.SocketProducer;
import com.message.chatservice.model.dto.MessageDTO;
import com.message.chatservice.model.dto.NotificationPayload;
import com.message.chatservice.model.entity.*;
import com.message.chatservice.repository.*;
import com.message.chatservice.service.ChatService;
import com.message.chatservice.utils.ChatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.message.chatservice.utils.DateTimeUtil.formatLocalDateTime;
import static com.message.chatservice.utils.DateTimeUtil.today;
import static com.message.chatservice.utils.SerializerUtils.serializeToJsonString;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final SocketProducer socketProducer;
    private final MessageRepo messageRepo;
    private final RoomChatRepo roomChatRepo;
    private final ContactRepo contactRepo;
    private final MessageUnreadRepo messageUnreadRepo;
    private final RoomMemberRepo roomMemberRepo;
    private final RoomChatAdminRepo roomChatAdminRepo;
    private final ContactMemberRepo contactMemberRepo;

    @Override
    @Transactional
    public MessageDTO sendMessage(Message message) throws ChatException {
        message.setTimeCreated(today());
        message.setDeleted(false);
        Message messageSave = messageRepo.save(message);
        messageSave.setDateFormat(formatLocalDateTime(today()));
        message.setId(messageSave.getId());
        boolean isNewRoom = message.getRoomChatId().equals(0L);
        RoomChat roomChat = getOrCreateNewRoomChat(message.getRoomChatId(), message);
        messageSave.setRoomChatId(roomChat.getId());
        messageRepo.save(messageSave);
        Contact contactReceiver = validateContact(message.getContactId());
        messageSave.setContact(contactReceiver);
        handleMessageByType(message, messageSave, roomChat);
        MessageDTO messageDTO = messageSave.convertDTO();
        messageDTO.setRoomChatId(roomChat.getId());
        handleRoomMemberWhenIsNewRoom(isNewRoom, messageDTO, roomChat.getId(), message);
        handleNotification(contactReceiver, roomChat, message, messageDTO);
        return messageDTO;
    }

    private void handleMessageByType(Message message, Message messageSave, RoomChat roomChat){
        if(ChatUtils.isCoupleType(message.getType())){
            handleMessageUnread(messageSave);
        }
        if(ChatUtils.isGroupTypeAndIsNewGroup(message)){
            handleRoomChatAdmin(messageSave.getContactId(), roomChat.getId());
        }
    }

    private void handleNotification(Contact contactReceiver, RoomChat roomChat, Message message, MessageDTO messageDTO){
        NotificationPayload payload = NotificationPayload.builder()
                .title(ChatUtils.getTitle(contactReceiver, roomChat, message.getType()))
                .avatar(ChatUtils.getAvatar(contactReceiver, roomChat, message.getType()))
                .content(message.getContent())
                .build();
        messageDTO.setNotification(serializeToJsonString(payload));
        socketProducer.publishMessage(messageDTO);
    }

    private void handleRoomMemberWhenIsNewRoom(boolean isNewRoom, MessageDTO messageDTO, Long roomChatId, Message message){
        messageDTO.setIsNewRoom(isNewRoom);
        if(isNewRoom){
            handleRoomMember(roomChatId, message);
            messageDTO.setMemberEmails(getRoomMemberEmail(message.getMemberIds()));
        }
    }

    private void handleMessageUnread(Message message){
        Optional<MessageUnread> unreadReceiver = Optional.ofNullable(messageUnreadRepo.findByRoomChatIdAndContactId(message.getRoomChatId(),
                message.getReceiverId()));
        if (!unreadReceiver.isPresent()) {
            messageUnreadRepo.save(MessageUnread.builder()
                    .messageUnread(1L)
                    .roomChatId(message.getRoomChatId())
                    .contactId(message.getReceiverId())
                    .build());
        }
        else {
            unreadReceiver.get().setMessageUnread(unreadReceiver.get().getMessageUnread() + 1);
            messageUnreadRepo.save(unreadReceiver.get());
        }
        Optional<MessageUnread> unreadSender = Optional.ofNullable(messageUnreadRepo.findByRoomChatIdAndContactId(message.getRoomChatId(),
                message.getContactId()));
        if (!unreadSender.isPresent()){
            messageUnreadRepo.save(MessageUnread.builder()
                    .messageUnread(0L)
                    .roomChatId(message.getRoomChatId())
                    .contactId(message.getContactId())
                    .build());
        }
    }

    private RoomChat getOrCreateNewRoomChat(Long chatRoomId, Message message) throws ChatException {
        Optional<RoomChat> roomChat = roomChatRepo.findById(chatRoomId);
        if (roomChat.isPresent()) {
            roomChat.get().setLatestMessageId(message.getId());
            roomChat.get().setUpdatedDate(today());
            return roomChatRepo.save(roomChat.get());
        } else {
            String name;
            String avatar;
            if (ChatUtils.isCoupleType(message.getType())) {
                Contact contactReceiver = validateContact(message.getReceiverId());
                name = contactReceiver.getUsername();
                avatar = contactReceiver.getAvatar();
            } else {
                name = message.getGroupName();
                avatar = message.getAvatar();
            }
            return roomChatRepo.save(RoomChat.builder()
                    .avatar(avatar)
                    .latestMessageId(message.getId())
                    .type(message.getType())
                    .name(name)
                    .createdDate(today())
                    .updatedDate(today())
                    .build());
        }
    }

    private void handleRoomMember(Long chatRoomId, Message message) {
        List<RoomMember> listRoomMember = new ArrayList<>();
        message.getMemberIds().forEach(memberId -> {
            long status = getStatusRoomChat(memberId, message);
            listRoomMember.add(RoomMember.builder()
                    .roomChatId(chatRoomId)
                    .contactId(memberId)
                    .status(status)
                    .build());
        });
        roomMemberRepo.saveAll(listRoomMember);
    }

    private long getStatusRoomChat(Long contactId, Message message) {
        if (ChatUtils.isSender(contactId, message.getContactId()) || ChatUtils.isGroupType(message.getType())) {
            return RoomStatus.ACTIVE;
        }
        if (ChatUtils.isReceiver(contactId, message.getReceiverId())
                && ChatUtils.isCoupleType(message.getType())
                && isContact(message.getContactId(), message.getReceiverId())) {
            return RoomStatus.ACTIVE;
        }
        return RoomStatus.ARCHIVE;
    }

    private void handleRoomChatAdmin(Long adminId, Long roomChatId) {
        roomChatAdminRepo.save(RoomChatAdmin.builder()
                .roomChatId(roomChatId)
                .adminId(adminId)
                .build());
    }

    private List<String> getRoomMemberEmail(List<Long> roomMemberIds){
        return contactRepo.getListEmailByIds(roomMemberIds);
    }

    private Contact validateContact(Long contactId) throws ChatException {
        Optional<Contact> contact = contactRepo.findById(contactId);
        if(!contact.isPresent()){
            throw new ChatException(ErrorType.NOT_FOUND_CONTACT);
        }
        return contact.get();
    }

    private boolean isContact(Long senderId, Long receiverId){
        return contactMemberRepo.existsBySenderIdAndReceiverId(senderId, receiverId);
    }
}

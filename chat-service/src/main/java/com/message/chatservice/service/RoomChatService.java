package com.message.chatservice.service;

import com.message.chatservice.exception.ChatException;
import com.message.chatservice.model.dto.ConversationResponse;
import com.message.chatservice.model.dto.RoomChatDTO;
import com.message.chatservice.model.entity.MessageUnread;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomChatService {
    List<RoomChatDTO> getRoomChats(String email, long status);

    ConversationResponse getConversationByRoomId(Long roomChatId, String sender) throws ChatException;

    MessageUnread readConversation(Long roomChatId, String email) throws ChatException;

    RoomChatDTO updateRoomChatInfo(String roomInfoString, MultipartFile file) throws ChatException;

    void deleteConversation(Long roomChatId);

    List<String> getListRoomToJoin(String email) throws ChatException;

    RoomChatDTO getRoomChatById(Long roomChatId, String email) throws ChatException;
}

package com.message.chatservice.service;

import com.message.chatservice.exception.ChatException;
import com.message.chatservice.model.dto.MessageDTO;
import com.message.chatservice.model.entity.Message;

public interface ChatService {

    MessageDTO sendMessage(Message message) throws ChatException;

}

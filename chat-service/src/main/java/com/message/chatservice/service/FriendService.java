package com.message.chatservice.service;

import com.message.chatservice.exception.ChatException;
import com.message.chatservice.model.dto.ContactDTO;

public interface FriendService {

    ContactDTO addFriend(ContactDTO contactDTO, String token) throws ChatException;

    void friendAction(String currentEmail, String addEmail, Long status) throws ChatException;
}

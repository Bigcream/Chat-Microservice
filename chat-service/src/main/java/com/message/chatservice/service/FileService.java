package com.message.chatservice.service;

import com.message.chatservice.exception.ChatException;
import com.message.chatservice.model.dto.ContentFile;

public interface FileService {
    ContentFile readFile(Long id) throws ChatException;
}

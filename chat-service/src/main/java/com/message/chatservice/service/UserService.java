package com.message.chatservice.service;

import com.message.chatservice.exception.ChatException;
import com.message.chatservice.model.dto.ContactDTO;
import com.message.chatservice.model.dto.pageable.ContactPageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<ContactDTO> getListContactByEmail(String email) throws ChatException;

    ContactDTO getContactByEmail(String email, String token) throws ChatException;

    String removeContact(Long contactRemoveId, Long currentContact);

    ContactDTO updateUserInfo(String contactString, String token, MultipartFile file) throws ChatException;

    ContactPageable getUserInSystemNotFriend(Long contactId, int page, int size);

    ContactPageable getAllUserInSystem(String email, int page, int size);
}

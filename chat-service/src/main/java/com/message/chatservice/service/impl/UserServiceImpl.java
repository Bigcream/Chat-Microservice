package com.message.chatservice.service.impl;

import com.message.chatservice.exception.ChatException;
import com.message.chatservice.exception.ErrorType;
import com.message.chatservice.model.dto.ContactDTO;
import com.message.chatservice.model.dto.UserInfo;
import com.message.chatservice.model.dto.pageable.ContactPageable;
import com.message.chatservice.model.entity.Contact;
import com.message.chatservice.model.entity.ContactMember;
import com.message.chatservice.model.entity.ShareFile;
import com.message.chatservice.redis.RedisHelper;
import com.message.chatservice.repository.ContactMemberRepo;
import com.message.chatservice.repository.ContactRepo;
import com.message.chatservice.repository.ShareFileRepo;
import com.message.chatservice.service.UserService;
import com.message.chatservice.utils.FileManager;
import com.message.chatservice.utils.UserUtils;
import com.message.chatservice.utils.restemplate.RestAuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.message.chatservice.constant.RedisPattern.USER_ONLINE_PATTERN;
import static com.message.chatservice.utils.SerializerUtils.deserializeFromJsonString;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ContactRepo contactRepo;
    private final ContactMemberRepo contactMemberRepo;
    private final ShareFileRepo shareFileRepo;
    private final RestAuthUtil restAuthUtil;
    private final RedisHelper redisHelper;

    @Value("${gate-way-url}")
    private String GATE_WAY_URL;

    @Override
    public List<ContactDTO> getListContactByEmail(String email){
        List<ContactMember> contactMembers = contactMemberRepo.findByCurrentContact(email);
        List<ContactDTO> contacts = new ArrayList<>();
        for (ContactMember contactMember : contactMembers){
            ContactDTO contactDTO = contactMember.getContact().convertDTO();
            contactDTO.setOnline(!redisHelper.findByKey(USER_ONLINE_PATTERN + contactDTO.getEmail()).isEmpty());
            contacts.add(contactDTO);
        }
        return contacts;
    }

    @Override
    @Transactional
    public ContactDTO getContactByEmail(String email, String token){
        return getOrCreateNewContact(email, token).convertDTO();
    }

    @Override
    @Transactional
    public String removeContact(Long senderId, Long receiverId) {
        try {
            contactMemberRepo.deleteBySenderIdAndReceiverId(senderId, receiverId);
            contactMemberRepo.deleteBySenderIdAndReceiverId(receiverId, senderId);
            return "Success";
        } catch (Exception e) {
            return "Failed";
        }
    }

    @Override
    @Transactional
    public ContactDTO updateUserInfo(String contactString, String token, MultipartFile file) throws ChatException {
        Contact contact = deserializeFromJsonString(contactString, Contact.class);
        Optional<Contact> contactOptional = contactRepo.findById(contact.getId());
        if(!contactOptional.isPresent()){
            throw new ChatException(ErrorType.NOT_FOUND_CONTACT);
        }
        if(file != null){
            Path path = FileManager.writeFile(file);
            ShareFile shareFile = shareFileRepo.save(ShareFile.builder()
                    .size(file.getSize())
                    .path(path.toString())
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .build());
            contactOptional.get().setAvatar(GATE_WAY_URL + "/api/v1/chat/image/" + shareFile.getId());
        } else {
            contactOptional.get().setAvatar(contact.getAvatar());
        }
        contactOptional.get().setUsername(UserUtils.getFullName(contact));
        contactOptional.get().setLastName(contact.getLastName());
        contactOptional.get().setFirstName(contact.getFirstName());
        restAuthUtil.updateUserInfo(convertContactToUserInfo(contact), token);
        return contactRepo.save(contactOptional.get()).convertDTO();
    }

    @Override
    public ContactPageable getUserInSystemNotFriend(Long contactId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Contact> contactPage = contactRepo.getAllUserInSystemNotFriend(contactId, pageable);
        List<ContactDTO> contactDTOS = contactPage.getContent().stream()
                .map(Contact::convertDTO)
                .collect(Collectors.toList());
        return ContactPageable.builder()
                .content(contactDTOS)
                .currentPage(page)
                .totalPage(contactPage.getTotalPages())
                .total(contactPage.getTotalElements())
                .hasNext(contactPage.hasNext())
                .build();
    }

    @Override
    public ContactPageable getAllUserInSystem(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Contact> contactPage = contactRepo.getAllUserInSystemNotSelf(email, pageable);
        List<ContactDTO> contactDTOS = contactPage.getContent().stream()
                .map(Contact::convertDTO)
                .collect(Collectors.toList());
        return ContactPageable.builder()
                .content(contactDTOS)
                .currentPage(page)
                .totalPage(contactPage.getTotalPages())
                .total(contactPage.getTotalElements())
                .hasNext(contactPage.hasNext())
                .build();
    }

    public UserInfo convertContactToUserInfo(Contact contact){
        return UserInfo.builder()
                .id(contact.getUserId())
                .email(contact.getEmail())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .username(UserUtils.getFullName(contact))
                .build();
    }

    public Contact getOrCreateNewContact(String email, String token) {
        Optional<Contact> contact = contactRepo.findByEmail(email);
        if(contact.isPresent()){
            return contact.get();
        }
        UserInfo userInfo = restAuthUtil.getUserFromAuthService(email, token);
        return contactRepo.save(Contact.builder()
                .username(userInfo.getUsername())
                .userId(userInfo.getId())
                .email(userInfo.getEmail())
                .avatar("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.pinterest.com%2Fpin%2Fdefault-avatar-profile-icon-of-social-media-user--947022627871095943%2F&psig=AOvVaw0w5L_1u5LOlNWUnGPGL45O&ust=1708950102346000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCJj6nbC9xoQDFQAAAAAdAAAAABAE")
                .firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName()).build());
    }
}

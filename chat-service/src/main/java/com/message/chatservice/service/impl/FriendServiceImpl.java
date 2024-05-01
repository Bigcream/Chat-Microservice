package com.message.chatservice.service.impl;

import com.message.chatservice.constant.FriendStatus;
import com.message.chatservice.constant.NotificationStatus;
import com.message.chatservice.exception.ChatException;
import com.message.chatservice.exception.ErrorType;
import com.message.chatservice.kafka.producer.NotificationProducer;
import com.message.chatservice.model.dto.ContactDTO;
import com.message.chatservice.model.dto.UserInfo;
import com.message.chatservice.model.entity.Contact;
import com.message.chatservice.model.entity.ContactMember;
import com.message.chatservice.model.entity.ContactPending;
import com.message.chatservice.model.entity.Notification;
import com.message.chatservice.repository.ContactMemberRepo;
import com.message.chatservice.repository.ContactPendingRepo;
import com.message.chatservice.repository.ContactRepo;
import com.message.chatservice.service.FriendService;
import com.message.chatservice.utils.NotificationUtils;
import com.message.chatservice.utils.restemplate.RestAuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.message.chatservice.constant.NotificationType.FRIEND_NOTIFICATION;
import static com.message.chatservice.utils.DateTimeUtil.formatTimeNotification;
import static com.message.chatservice.utils.DateTimeUtil.today;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final ContactMemberRepo contactMemberRepo;
    private final RestAuthUtil restAuthUtil;
    private final NotificationProducer notificationProducer;
    private final ContactRepo contactRepo;
    private final ContactPendingRepo contactPendingRepo;

    @Override
    @Transactional
    public ContactDTO addFriend(ContactDTO contactDTO, String token) throws ChatException {
        Contact addContact = validatePresentContact(contactDTO.getAddEmail(), token);
        Contact currentContact = validatePresentContact(contactDTO.getCurrentEmail(), token);
        Optional<ContactMember> contactMemberOpt = contactMemberRepo.findByCurrentContactAndContactId(currentContact.getEmail(),
                addContact.getId());
        if(contactMemberOpt.isPresent()){
            throw new ChatException(ErrorType.CONTACT_ALREADY_EXIST);
        }
        contactPendingRepo.save(ContactPending.builder().currentContact(currentContact.getEmail())
                .receiverId(addContact.getId())
                .addContact(addContact.getEmail())
                .status(FriendStatus.PENDING)
                .senderId(currentContact.getId())
                .build());
        notificationProducer.publishMessage(Notification.builder()
                .topic(FRIEND_NOTIFICATION.name())
                .type(FRIEND_NOTIFICATION.name())
                .receiverEmail(addContact.getEmail())
                .senderEmail(currentContact.getEmail())
                .createdDate(today())
                .updatedDate(today())
                .status(NotificationStatus.USER)
                .timeFormat(formatTimeNotification(today()))
                .payload(NotificationUtils.buildPayload(addContact))
                .build());
        return addContact.convertDTO();
    }

    @Override
    @Transactional
    public void friendAction(String currentEmail, String addEmail, Long status) throws ChatException {
        Optional<ContactPending> contactPending = contactPendingRepo.findByCurrentContactAndAddContactAndStatus(addEmail,
                currentEmail, FriendStatus.PENDING);
        if (!contactPending.isPresent()) {
            throw new ChatException(ErrorType.NOT_FOUND_CONTACT_PENDING);
        }
        if (status.equals(FriendStatus.REJECT)) {
            contactPendingRepo.deleteById(contactPending.get().getId());
        } else if (status.equals(FriendStatus.ACCEPT)) {
            contactPending.get().setStatus(status);
            contactPendingRepo.save(contactPending.get());
            contactMemberRepo.save(ContactMember.builder()
                    .receiverId(contactPending.get().getReceiverId())
                    .senderId(contactPending.get().getSenderId())
                    .addContact(contactPending.get().getAddContact())
                    .currentContact(contactPending.get().getCurrentContact())
                    .build());
            contactMemberRepo.save(ContactMember.builder()
                    .senderId(contactPending.get().getReceiverId())
                    .receiverId(contactPending.get().getSenderId())
                    .addContact(contactPending.get().getCurrentContact())
                    .currentContact(contactPending.get().getAddContact())
                    .build());
        }
    }

    public Contact validatePresentContact(String email, String token) {
        Optional<Contact> contact = contactRepo.findByEmail(email);
        if(contact.isPresent()){
            return contact.get();
        }
        UserInfo userInfo = restAuthUtil.getUserFromAuthService(email, token);
        return contactRepo.save(Contact.builder()
                .username(userInfo.getUsername())
                .userId(userInfo.getId())
                .email(userInfo.getEmail())
                .firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName()).build());
    }
}

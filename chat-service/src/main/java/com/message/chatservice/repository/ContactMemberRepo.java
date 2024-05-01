package com.message.chatservice.repository;

import com.message.chatservice.model.entity.ContactMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactMemberRepo extends JpaRepository<ContactMember, Long> {
    List<ContactMember> findByCurrentContact(String email);

    Optional<ContactMember> findByCurrentContactAndContactId(String email, Long contactId);

    void deleteBySenderIdAndReceiverId(Long senderId, Long receiverId);

    boolean existsBySenderIdAndReceiverId(Long senderId, Long receiverId);
}

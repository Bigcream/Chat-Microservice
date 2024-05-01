package com.message.chatservice.repository;

import com.message.chatservice.model.entity.ContactPending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactPendingRepo extends JpaRepository<ContactPending, Long> {

    Optional<ContactPending> findByCurrentContactAndAddContactAndStatus(String currentEmail, String addEmail, Long status);
}

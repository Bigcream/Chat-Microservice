package com.message.chatservice.repository;

import com.message.chatservice.model.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Long>, JpaSpecificationExecutor<Contact> {
    Optional<Contact> findByEmail(String email);

    @Query(value = "select * from contact where id != ?1\n" +
            "and id not in (select receiver_id from contact_member where sender_id = ?1)\n" +
            "and id not in (select receiver_id from contact_pending where sender_id = ?1)",
            countQuery = "select count(*) from contact where id != ?1\n" +
                    "and id not in (select receiver_id from contact_member where sender_id = ?1)\n" +
                    "and id not in (select receiver_id from contact_pending where sender_id = ?1))", nativeQuery = true)
    Page<Contact> getAllUserInSystemNotFriend(Long contactId, Pageable pageable);

    @Query(value = "select email from contact where id in ?1", nativeQuery = true)
    List<String> getListEmailByIds(List<Long> ids);

    @Query(value = "select * from contact where email != ?1",
            countQuery = "select count(*) from contact where email != ?1", nativeQuery = true)
    Page<Contact> getAllUserInSystemNotSelf(String email, Pageable pageable);
}

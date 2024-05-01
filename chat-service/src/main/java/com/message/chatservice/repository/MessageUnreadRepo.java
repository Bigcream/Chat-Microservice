package com.message.chatservice.repository;

import com.message.chatservice.model.entity.MessageUnread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageUnreadRepo extends JpaRepository<MessageUnread, Long> {
    MessageUnread findByRoomChatIdAndContactId(Long roomChatId, Long contactId);
}

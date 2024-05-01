package com.message.chatservice.repository;

import com.message.chatservice.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {

    List<Message> findByRoomChatId(Long roomChatId);
    @Query(value = "select * from message where room_chat_id = ?1 and receiver_id = ?2 order by time_created desc limit 1", nativeQuery = true)
    Message findByRoomChatIdAndReceiverId(Long roomChatId, Long receiverId);
}

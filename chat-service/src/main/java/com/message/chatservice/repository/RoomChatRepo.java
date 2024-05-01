package com.message.chatservice.repository;

import com.message.chatservice.model.entity.RoomChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomChatRepo extends JpaRepository<RoomChat, Long> {
}

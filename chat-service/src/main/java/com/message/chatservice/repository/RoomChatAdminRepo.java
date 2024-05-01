package com.message.chatservice.repository;

import com.message.chatservice.model.entity.RoomChatAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomChatAdminRepo extends JpaRepository<RoomChatAdmin, Long> {

    @Query(value = "select admin_id from room_chat_admin where room_chat_id = ?1", nativeQuery = true)
    List<Long> findAdminByRoomChatId(Long roomChatId);

    void deleteByAdminIdAndRoomChatId(Long adminId, Long roomChatId);

    Optional<RoomChatAdmin> findByAdminIdAndRoomChatId(Long adminId, Long roomChatId);
}

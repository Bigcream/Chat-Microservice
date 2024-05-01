package com.message.chatservice.repository;

import com.message.chatservice.model.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomMemberRepo extends JpaRepository<RoomMember, Long> {
    List<RoomMember> findByRoomChatId(Long roomChatId);

    List<RoomMember> findByContactId(Long contactId);

    List<RoomMember> findByContactIdAndStatus(Long contactId, Long status);

    @Query(value = "select contact_id from room_member where contact_id != ?1 and room_chat_id = ?2", nativeQuery = true)
    Long findReceiverIdInRoomCouple(Long senderId, Long roomChatId);

    void deleteByContactIdAndRoomChatId(Long memberId, Long roomChatId);

    void deleteByRoomChatId(Long roomChatId);
}

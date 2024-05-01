package com.message.chatservice.service.impl;

import com.message.chatservice.model.entity.RoomChatAdmin;
import com.message.chatservice.repository.RoomChatAdminRepo;
import com.message.chatservice.repository.RoomMemberRepo;
import com.message.chatservice.service.RoomActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomActionServiceImpl implements RoomActionService {
    private final RoomChatAdminRepo roomChatAdminRepo;
    private final RoomMemberRepo roomMemberRepo;
    private static final String DEFAULT_MESSAGE_SUCCESS = "Success";

    @Override
    @Transactional
    public String promoteToAdmin(Long memberId, Long roomChatId) {
        try {
            Optional<RoomChatAdmin> roomChatAdmin = roomChatAdminRepo.findByAdminIdAndRoomChatId(memberId, roomChatId);
            if(!roomChatAdmin.isPresent()){
                roomChatAdminRepo.save(RoomChatAdmin.builder()
                        .roomChatId(roomChatId)
                        .adminId(memberId)
                        .build());
            }
            return DEFAULT_MESSAGE_SUCCESS;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    @Transactional
    public String demoteToMember(Long memberId, Long roomChatId) {
        try {
            roomChatAdminRepo.deleteByAdminIdAndRoomChatId(memberId, roomChatId);
            return DEFAULT_MESSAGE_SUCCESS;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    @Transactional
    public String removeMember(Long memberId, Long roomChatId) {
        try {
            roomMemberRepo.deleteByContactIdAndRoomChatId(memberId, roomChatId);
            return DEFAULT_MESSAGE_SUCCESS;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}

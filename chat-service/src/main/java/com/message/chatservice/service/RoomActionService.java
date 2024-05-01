package com.message.chatservice.service;

public interface RoomActionService {
    String promoteToAdmin(Long memberId, Long roomChatId);

    String demoteToMember(Long memberId, Long roomChatId);

    String removeMember(Long memberId, Long roomChatId);
}

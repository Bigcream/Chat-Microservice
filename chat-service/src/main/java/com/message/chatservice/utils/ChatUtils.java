package com.message.chatservice.utils;

import com.message.chatservice.model.entity.Contact;
import com.message.chatservice.model.entity.Message;
import com.message.chatservice.model.entity.RoomChat;

public class ChatUtils {
    private static final String COUPLE_TYPE = "couple";
    private static final String GROUP_TYPE = "group";

    private static final Long CREATE_NEW_GROUP = 0L;
    public static String getAvatar(Contact contactReceiver, RoomChat roomChat, String type) {
        return isCoupleType(type) ? contactReceiver.getAvatar() : roomChat.getAvatar();
    }

    public static Long getReceiver(Contact contactReceiver, RoomChat roomChat, String type) {
        return isCoupleType(type) ? contactReceiver.getId() : roomChat.getId();
    }

    public static Long getSender(Contact contactReceiver, RoomChat roomChat, String type) {
        return isCoupleType(type) ? contactReceiver.getId() : roomChat.getId();
    }

    public static String getTitle(Contact contactReceiver, RoomChat roomChat, String type) {
        return isCoupleType(type) ? contactReceiver.getUsername() : roomChat.getName();
    }

    public static boolean isCoupleType(String type) {
        return type.equals(COUPLE_TYPE);
    }

    public static boolean isGroupType(String type) {
        return type.equals(GROUP_TYPE);
    }

    public static boolean isGroupTypeAndIsNewGroup(Message message) {
        return message.getType().equals(GROUP_TYPE) && message.getId().equals(CREATE_NEW_GROUP);
    }

    public static boolean isReceiver(Long contactId, Long receiverId) {
        return contactId.equals(receiverId);
    }

    public static boolean isSender(Long contactId, Long senderId) {
        return contactId.equals(senderId);
    }
}

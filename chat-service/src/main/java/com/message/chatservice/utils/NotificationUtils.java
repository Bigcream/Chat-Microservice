package com.message.chatservice.utils;

import com.message.chatservice.model.dto.NotificationPayload;
import com.message.chatservice.model.entity.Contact;

public class NotificationUtils {

    public static String buildPayload(Contact contact){
        NotificationPayload payload = NotificationPayload.builder()
                .content("<span class=\"font-semibold text-gray-900 dark:text-white\">" + contact.getUsername() +"</span>"
                        + " sent you a friend request")
                .avatar(contact.getAvatar())
                .build();
        return SerializerUtils.serializeToJsonString(payload);
    }
}

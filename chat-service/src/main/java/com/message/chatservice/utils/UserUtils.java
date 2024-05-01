package com.message.chatservice.utils;

import com.message.chatservice.model.entity.Contact;

public class UserUtils {
    public static String getFullName(Contact contact){
        return contact.getFirstName() + " " + contact.getLastName();
    }
}

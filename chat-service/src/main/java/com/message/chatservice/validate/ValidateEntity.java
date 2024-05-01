//package com.message.chatservice.validate;
//
//import com.message.chatservice.model.entity.Contact;
//import com.message.chatservice.repository.ContactRepo;
//
//import java.util.Optional;
//
//public class ValidateEntity {
//    public static Contact getOrCreateContact(ContactRepo contactRepo, String email){
//        Optional<Contact> contact = contactRepo.findByEmail(email);
//        if(!contact.isPresent()){
//            return Contact.builder().build();
//        }
//        return contact.get();
//    }
//}

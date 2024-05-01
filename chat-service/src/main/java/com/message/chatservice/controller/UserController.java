package com.message.chatservice.controller;

import com.message.chatservice.exception.ChatException;
import com.message.chatservice.model.dto.ContactDTO;
import com.message.chatservice.model.dto.pageable.ContactPageable;
import com.message.chatservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class UserController extends BaseController{

    private final UserService userService;

    @GetMapping("/contacts/{email}")
    public ResponseEntity<List<ContactDTO>> getListContactByEmail(@PathVariable String email) throws ChatException {
        return new ResponseEntity<>(userService.getListContactByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/current-contact")
    public ResponseEntity<ContactDTO> getCurrentContact(@RequestParam String email,
                                                     @RequestHeader("Authorization") String token) throws ChatException {
        return new ResponseEntity<>(userService.getContactByEmail(email, token), noCacheHeader, HttpStatus.OK);
    }

    @PostMapping("/remove-contact")
    public ResponseEntity<String> removeContact(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return new ResponseEntity<>(userService.removeContact(senderId, receiverId), noCacheHeader, HttpStatus.OK);
    }

    @PutMapping("/contact-info")
    public ResponseEntity<ContactDTO> updateContactInfo(@RequestParam MultipartFile file,
                                                        @RequestParam String contactString,
                                                        @RequestHeader("Authorization") String token) throws ChatException {
        return new ResponseEntity<>(userService.updateUserInfo(contactString, token, file), noCacheHeader, HttpStatus.OK);
    }

    @GetMapping("/contact-not-friend/{contactId}")
    public ResponseEntity<ContactPageable> getUserInSystemNotFriend(@PathVariable Long contactId,
                                                                    @RequestParam int page,
                                                                    @RequestParam int size){
        return new ResponseEntity<>(userService.getUserInSystemNotFriend(contactId, page, size), noCacheHeader, HttpStatus.OK);
    }

    @GetMapping("/contact-all/{email}")
    public ResponseEntity<ContactPageable> getAllUserInSystem(@PathVariable String email,
                                                              @RequestParam int page,
                                                              @RequestParam int size) {
        return new ResponseEntity<>(userService.getAllUserInSystem(email, page, size), noCacheHeader, HttpStatus.OK);
    }
}

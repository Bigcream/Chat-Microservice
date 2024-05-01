package com.message.chatservice.controller;

import com.message.chatservice.exception.ChatException;
import com.message.chatservice.model.dto.ContactDTO;
import com.message.chatservice.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class FriendController extends BaseController{

    private final FriendService friendService;

    @PostMapping("/add-friend")
    public ResponseEntity<ContactDTO> addContactByEmail(@RequestBody ContactDTO contactDTO,
                                                     @RequestHeader("Authorization") String token) throws ChatException {
        return new ResponseEntity<>(friendService.addFriend(contactDTO, token), noCacheHeader, HttpStatus.OK);
    }

    @PostMapping("/friend-action/{currentContact}")
    public ResponseEntity<Void> friendAction(@PathVariable String currentContact, @RequestParam String addContact,
                                             @RequestParam Long status) throws ChatException {
        friendService.friendAction(currentContact, addContact, status);
        return new ResponseEntity<>(noCacheHeader, HttpStatus.OK);
    }
}

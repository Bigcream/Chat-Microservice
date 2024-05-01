package com.message.chatservice.controller;

import com.message.chatservice.exception.ChatException;
import com.message.chatservice.model.dto.MessageDTO;
import com.message.chatservice.model.entity.Message;
import com.message.chatservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController extends BaseController{

    private final ChatService chatService;

    @PostMapping("/send-message")
    public ResponseEntity<MessageDTO> sendDirectMessage(@RequestBody Message message) throws ChatException {
        return new ResponseEntity<>(chatService.sendMessage(message), noCacheHeader, HttpStatus.OK);
    }

//    @GetMapping("/direct-message")
//    public ResponseEntity<MessageResponse> getDirectConversation(@RequestParam Long roomChatId) {
//        return new ResponseEntity<>(roomChatService.getConversationByRoomId(roomChatId), noCacheHeader, HttpStatus.OK);
//    }
}

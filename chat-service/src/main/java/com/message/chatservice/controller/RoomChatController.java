package com.message.chatservice.controller;

import com.message.chatservice.exception.ChatException;
import com.message.chatservice.model.dto.ConversationResponse;
import com.message.chatservice.model.dto.RoomChatDTO;
import com.message.chatservice.model.entity.MessageUnread;
import com.message.chatservice.service.RoomChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class RoomChatController extends BaseController{

    private final RoomChatService roomChatService;

    @GetMapping("/room-chat/conversation")
    public ResponseEntity<ConversationResponse> getDirectConversation(@RequestParam Long roomChatId,
                                                                      @RequestParam String sender) throws ChatException {
        return new ResponseEntity<>(roomChatService.getConversationByRoomId(roomChatId, sender), noCacheHeader, HttpStatus.OK);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomChatDTO>> getRoomChatActives(@RequestParam String email,
                                                                @RequestParam long status) {
        return new ResponseEntity<>(roomChatService.getRoomChats(email, status), noCacheHeader, HttpStatus.OK);
    }


    @PostMapping("/read-conversation/{roomChatId}")
    public ResponseEntity<MessageUnread> readConversation(@PathVariable Long roomChatId,
                                                          @RequestParam String email) throws ChatException {
        return new ResponseEntity<>(roomChatService.readConversation(roomChatId, email), noCacheHeader, HttpStatus.OK);
    }

    @PutMapping("/room-chat-info")
    public ResponseEntity<RoomChatDTO> updateRoomChatInfo(@RequestParam MultipartFile file,
                                                          @RequestParam String roomInfoString) throws ChatException {
        return new ResponseEntity<>(roomChatService.updateRoomChatInfo(roomInfoString, file), noCacheHeader, HttpStatus.OK);
    }

    @DeleteMapping("/room-chat/{roomChatId}")
    public ResponseEntity<Void> deleteConversation(@PathVariable Long roomChatId){
        roomChatService.deleteConversation(roomChatId);
        return new ResponseEntity<>(noCacheHeader, HttpStatus.OK);
    }

    @GetMapping("/list-room-socket/{email}")
    public ResponseEntity<List<String>> getListRoomToJoin(@PathVariable String email) throws ChatException {
        return new ResponseEntity<>(roomChatService.getListRoomToJoin(email), noCacheHeader, HttpStatus.OK);
    }

    @GetMapping("/room-chat/{id}")
    public ResponseEntity<RoomChatDTO> getListRoomToJoin(@PathVariable Long id,
                                                          @RequestParam String email) throws ChatException {
        return new ResponseEntity<>(roomChatService.getRoomChatById(id, email), noCacheHeader, HttpStatus.OK);
    }
}

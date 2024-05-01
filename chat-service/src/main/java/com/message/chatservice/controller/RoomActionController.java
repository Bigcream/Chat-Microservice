package com.message.chatservice.controller;

import com.message.chatservice.service.RoomActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class RoomActionController extends BaseController{

    private final RoomActionService roomActionService;

    @PostMapping("/promote-admin")
    public ResponseEntity<String> promoteToAdmin(@RequestParam Long memberId, @RequestParam Long roomChatId){
        return new ResponseEntity<>(roomActionService.promoteToAdmin(memberId, roomChatId), noCacheHeader, HttpStatus.OK);
    }

    @PostMapping("/demote-member")
    public ResponseEntity<String> demoteToMember(@RequestParam Long memberId, @RequestParam Long roomChatId) {
        return new ResponseEntity<>(roomActionService.demoteToMember(memberId, roomChatId), noCacheHeader, HttpStatus.OK);
    }

    @DeleteMapping("/remove-member")
    public ResponseEntity<String> removeMember(@RequestParam Long memberId, @RequestParam Long roomChatId) {
        return new ResponseEntity<>(roomActionService.removeMember(memberId, roomChatId), noCacheHeader, HttpStatus.OK);
    }
}

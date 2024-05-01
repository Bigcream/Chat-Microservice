package com.message.chatservice.controller;

import com.message.chatservice.exception.ChatException;
import com.message.chatservice.model.dto.ContentFile;
import com.message.chatservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class FileController extends BaseController{

    private final FileService fileService;

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getCurrentContact(@PathVariable Long id) throws ChatException {
        return getFileResponseEntity(fileService.readFile(id));
    }

    private ResponseEntity<byte[]> getFileResponseEntity(ContentFile file){
        return ResponseEntity.ok()
                .contentType(file.getMediaType())
                .body(file.getBytes());
    }
}

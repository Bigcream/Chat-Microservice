package com.message.chatservice.service.impl;

import com.message.chatservice.exception.ChatException;
import com.message.chatservice.exception.ErrorType;
import com.message.chatservice.model.dto.ContentFile;
import com.message.chatservice.model.entity.ShareFile;
import com.message.chatservice.repository.ShareFileRepo;
import com.message.chatservice.service.FileService;
import com.message.chatservice.utils.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final ShareFileRepo shareFileRepo;

    @Override
    public ContentFile readFile(Long id) throws ChatException {
        Optional<ShareFile> shareFile = shareFileRepo.findById(id);
        if(!shareFile.isPresent()){
            throw new ChatException(ErrorType.NOT_FOUND_FILE);
        }
        return ContentFile.builder()
                .bytes(FileManager.readFile(shareFile.get().getPath()))
                .size(shareFile.get().getSize())
                .type(shareFile.get().getType())
                .name(shareFile.get().getName())
                .build();
    }
}

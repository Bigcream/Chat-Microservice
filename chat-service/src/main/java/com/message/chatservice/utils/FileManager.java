package com.message.chatservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class FileManager {
    private static final String UPLOAD_PATH = "/upload/";
    public static String makeFileName(String fileName){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(new Date()) + RandomStringUtils.randomAlphabetic(2) + fileName;
    }

    public static String makeUploadPath(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return UPLOAD_PATH + (localDateTime.getYear() + localDateTime.getMonthValue());
    }

    public static Path writeFile(MultipartFile file){
        String fileName = makeFileName(file.getName());
        String uploadPath = makeUploadPath();
        try {
            FileUtils.forceMkdir(new File(uploadPath));
            String filePath = uploadPath + fileName;
            FileUtils.writeByteArrayToFile(new File(filePath), file.getBytes());
        } catch (IOException e){
            log.error(e.getMessage());
        }
        File fileResult = new File(FilenameUtils.normalize(uploadPath + fileName));
        return fileResult.toPath();
    }

    public static byte[] readFile(String filePath){
        byte[] result = new byte[0];
        try {
            File fileResult = new File(FilenameUtils.normalize(filePath));
            result = FileUtils.readFileToByteArray(fileResult);
        } catch (IOException e){
            log.error(e.getMessage());
        }
        return result;
    }
}

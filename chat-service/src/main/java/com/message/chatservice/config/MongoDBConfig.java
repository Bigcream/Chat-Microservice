//package com.message.chatservice.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
//import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
//
//
//@Configuration
//@RequiredArgsConstructor
//public class MongoDBConfig {
//
//    private final MappingMongoConverter mappingMongoConverter;
//
//    // remove _class
//    @Bean
//    public void setUpMongoEscapeCharacterConversion() {
//        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
//    }
//}

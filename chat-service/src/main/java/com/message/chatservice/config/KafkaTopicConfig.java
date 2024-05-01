package com.message.chatservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.message.chatservice.constant.KafkaTopic.*;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic directMessage(){
        return TopicBuilder.name(DIRECT_MESSAGE)
                .partitions(3)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic groupMessage(){
        return TopicBuilder.name(GROUP_MESSAGE)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic groupAction(){
        return TopicBuilder.name(GROUP_ACTION)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic privateAction(){
        return TopicBuilder.name(PRIVATE_ACTION)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic notificationTopic(){
        return TopicBuilder.name(NOTIFICATION)
                .partitions(3)
                .replicas(1)
                .build();
    }
}

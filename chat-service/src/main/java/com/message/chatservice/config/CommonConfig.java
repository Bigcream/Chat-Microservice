package com.message.chatservice.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CommonConfig {
    @Bean(name = "restTemplate")
    public RestTemplate createTemplate(){
        return new RestTemplate();
    }

//    @Bean
//    public Module hibernateModule() {
//        return new Hibernate5Module();
//    }
}

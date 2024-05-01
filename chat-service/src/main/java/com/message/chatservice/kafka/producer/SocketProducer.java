package com.message.chatservice.kafka.producer;

import com.message.chatservice.model.dto.MessageDTO;
import com.message.chatservice.model.dto.NotificationDTO;
import com.message.chatservice.utils.SerializerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.message.chatservice.constant.KafkaTopic.DIRECT_MESSAGE;
import static com.message.chatservice.constant.KafkaTopic.NOTIFICATION;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final static long sendTimeout = 3000;
    public void publishMessage(MessageDTO messageKafka) {
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(DIRECT_MESSAGE,
                SerializerUtils.serializeToJsonBytes(messageKafka));
        try {
            kafkaTemplate.send(record).get(sendTimeout, TimeUnit.MILLISECONDS);
            log.info("publishing kafka record value >>>>> {}", new String(record.value()));

        } catch (Exception ex) {
            log.error("(KafkaEventBus) publish get timeout", ex);
            throw new RuntimeException(ex);
        }
    }

    public void publishNotification(byte[] payload) {
        try {
            ProducerRecord<String, byte[]> record = new ProducerRecord<>(NOTIFICATION, payload);
            kafkaTemplate.send(record).get(sendTimeout, TimeUnit.MILLISECONDS);
            log.info("publishing kafka record value >>>>> {}", new String(record.value()));
        } catch (Exception ex) {
            log.error("(KafkaEventBus) publish get timeout", ex);
            throw new RuntimeException(ex);
        }
    }

    public void publishNotification(NotificationDTO notificationDTO) {
        try {
            ProducerRecord<String, byte[]> record = new ProducerRecord<>(NOTIFICATION,
                    SerializerUtils.serializeToJsonBytes(notificationDTO));
            kafkaTemplate.send(record).get(sendTimeout, TimeUnit.MILLISECONDS);
            log.info("publishing kafka record value >>>>> {}", new String(record.value()));
        } catch (Exception ex) {
            log.error("(KafkaEventBus) publish get timeout", ex);
            throw new RuntimeException(ex);
        }
    }
}

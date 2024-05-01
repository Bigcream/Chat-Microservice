package com.message.chatservice.kafka.producer;

import com.message.chatservice.model.entity.Notification;
import com.message.chatservice.utils.SerializerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.message.chatservice.constant.KafkaTopic.GROUP_ACTION;
import static com.message.chatservice.constant.KafkaTopic.NOTIFICATION;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final static long sendTimeout = 3000;

    public void publishMessage(byte[] payload) {
        try {
            ProducerRecord<String, byte[]> record = new ProducerRecord<>(NOTIFICATION, payload);
            kafkaTemplate.send(record).get(sendTimeout, TimeUnit.MILLISECONDS);
            log.info("publishing kafka record value >>>>> {}", new String(record.value()));
        } catch (Exception ex) {
            log.error("(KafkaEventBus) publish get timeout", ex);
            throw new RuntimeException(ex);
        }
    }

    public void publishMessage(Notification Notification) {
        try {
            ProducerRecord<String, byte[]> record = new ProducerRecord<>(Notification.getTopic(),
                    SerializerUtils.serializeToJsonBytes(Notification));
            kafkaTemplate.send(record).get(sendTimeout, TimeUnit.MILLISECONDS);
            log.info("publishing kafka record value >>>>> {}", new String(record.value()));
        } catch (Exception ex) {
            log.error("(KafkaEventBus) publish get timeout", ex);
            throw new RuntimeException(ex);
        }
    }

    public void addRoomMember(List<String> rooms) {
        try {
            ProducerRecord<String, byte[]> record = new ProducerRecord<>(GROUP_ACTION,
                    SerializerUtils.serializeToJsonBytes(rooms));
            kafkaTemplate.send(record).get(sendTimeout, TimeUnit.MILLISECONDS);
            log.info("publishing kafka record value >>>>> {}", new String(record.value()));
        } catch (Exception ex) {
            log.error("(KafkaEventBus) publish get timeout", ex);
            throw new RuntimeException(ex);
        }
    }
}

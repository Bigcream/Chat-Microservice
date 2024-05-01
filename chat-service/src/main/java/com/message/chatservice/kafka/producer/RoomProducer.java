package com.message.chatservice.kafka.producer;

import com.message.chatservice.utils.SerializerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.message.chatservice.constant.KafkaTopic.GROUP_ACTION;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final static long sendTimeout = 3000;
    public void joinRoom(List<String> rooms) {
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

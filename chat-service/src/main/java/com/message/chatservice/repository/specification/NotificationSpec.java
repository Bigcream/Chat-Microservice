package com.message.chatservice.repository.specification;

import com.message.chatservice.model.entity.Notification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class NotificationSpec {
    public Specification<Notification> hasStatus(long status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public Specification<Notification> hasEmail(String receiverEmail) {
        return (root, query, cb) -> cb.equal(root.get("receiverEmail"), receiverEmail);
    }
}

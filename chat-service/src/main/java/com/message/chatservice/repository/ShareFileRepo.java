package com.message.chatservice.repository;

import com.message.chatservice.model.entity.ShareFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareFileRepo extends JpaRepository<ShareFile, Long> {
}

package com.message.chatservice.model.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "share_file")
@Entity
public class ShareFile {

    @Id
    @GeneratedValue(generator="share_file_id_seq")
    @SequenceGenerator(name = "share_file_id_seq", sequenceName = "share_file_id_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String type;

    private String path;

    private long size;

    private boolean deleted;
}

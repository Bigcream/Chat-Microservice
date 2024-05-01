package com.message.chatservice.model.dto.pageable;

import com.message.chatservice.model.dto.ContactDTO;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ContactPageable {

    private long totalPage;
    private long total;
    private long currentPage;
    private Boolean hasNext;
    private List<ContactDTO> content;
}

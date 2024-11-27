package com.ezen.spring.dto;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class NoticeFileDTO {
    private NoticeDTO noticeDTO;
    private List<String> uuidArr;
}

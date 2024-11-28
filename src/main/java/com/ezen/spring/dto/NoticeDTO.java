package com.ezen.spring.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class NoticeDTO {

    private Long noticeId; // DTO 또한 Long 형식으로 사용
    private String title;
    private String writer;
    private String content;
    private Long userId;
    private LocalDateTime regAt, modAt;
    private String isDel;

}

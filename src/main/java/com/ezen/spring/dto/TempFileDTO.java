package com.ezen.spring.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class TempFileDTO {

    private String uuid;
    private String saveDir;
    private String fileName;
    private long fileSize;
    private Long noticeId;
    private LocalDateTime regAt, modAt;

}
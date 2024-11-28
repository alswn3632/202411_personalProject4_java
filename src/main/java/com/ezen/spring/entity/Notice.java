package com.ezen.spring.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
public class Notice extends TimeBase{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(length = 200, nullable = false)
    private String writer;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private Long userId;

    @Column(name = "is_del", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String isDel;

    @PrePersist  // 엔티티가 처음 저장될 때
    @PreUpdate   // 엔티티가 업데이트될 때
    public void setDefaultIsDel() {
        // isDel이 null인 경우에만 'N'으로 설정
        if (this.isDel == null) {
            this.isDel = "N";
        }
    }

}

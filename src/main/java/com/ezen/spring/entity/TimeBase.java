package com.ezen.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
public class TimeBase {

    @CreatedDate
    @Column(name = "reg_at", updatable = false)
    private LocalDateTime regAt;

    @LastModifiedDate
    @Column(name = "mod_at")
    private LocalDateTime modAt;

}
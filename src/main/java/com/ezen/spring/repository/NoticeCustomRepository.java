package com.ezen.spring.repository;

import com.ezen.spring.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeCustomRepository {

    Page<Notice> searchBoards(String type, String keyword, Pageable pageable);

}

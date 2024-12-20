package com.ezen.spring.repository;

import com.ezen.spring.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeCustomRepository {

    @Query("SELECT n FROM Notice n WHERE n.isDel = 'N'")
    List<Notice> getList();
}

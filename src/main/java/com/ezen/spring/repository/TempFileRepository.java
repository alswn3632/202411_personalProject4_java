package com.ezen.spring.repository;

import com.ezen.spring.entity.TempFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempFileRepository extends JpaRepository<TempFile, String> {
}

package com.ezen.spring.repository;

import com.ezen.spring.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {
}

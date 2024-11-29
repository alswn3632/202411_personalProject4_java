package com.ezen.spring;

import com.ezen.spring.dto.NoticeDTO;
import com.ezen.spring.dto.NoticeFileDTO;
import com.ezen.spring.service.NoticeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class ApplicationTests {
	@Autowired
	private NoticeService noticeService;

	@Test
	void contextLoads() {
		for(int i=1; i<=300; i++){
			NoticeDTO noticeDTO = NoticeDTO.builder()
					.title("test title [" + i + "]")
					.writer("관리자")
					.content("test content " + i)
					.userId(1L)
					.build();
			NoticeFileDTO noticeFileDTO = new NoticeFileDTO(noticeDTO, new ArrayList<String>());
			noticeService.create(noticeFileDTO);
		}
	}


}

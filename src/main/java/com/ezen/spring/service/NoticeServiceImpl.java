package com.ezen.spring.service;

import com.ezen.spring.dto.FileDTO;
import com.ezen.spring.dto.NoticeDTO;
import com.ezen.spring.dto.NoticeFileDTO;
import com.ezen.spring.dto.TempFileDTO;
import com.ezen.spring.entity.File;
import com.ezen.spring.entity.TempFile;
import com.ezen.spring.repository.FileRepository;
import com.ezen.spring.repository.NoticeRepository;
import com.ezen.spring.repository.TempFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService{
    private final NoticeRepository noticeRepository;
    private final TempFileRepository tempFileRepository;
    private final FileRepository fileRepository;

    @Transactional
    @Override
    public Long create(NoticeFileDTO noticeFileDTO) {
        Long num = noticeRepository.save(convertDtoToEntity(noticeFileDTO.getNoticeDTO())).getNoticeId();

        if(num != null){
            for(String uuid : noticeFileDTO.getUuidArr()){
                Optional<TempFile> optional = tempFileRepository.findById(uuid);
                optional.get().setNoticeId(num);
                if(optional.isPresent()){
                    fileRepository.save(convertTempToReal(optional.get()));
                    tempFileRepository.deleteById(uuid);
                }
            }
        }

        return num;
    }

    @Override
    public int tempSave(TempFileDTO tempFileDTO) {
        return tempFileRepository.save(tfconvertDtoToEntity(tempFileDTO)).getUuid() != null? 1 : 0;
    }
}

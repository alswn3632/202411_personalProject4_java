package com.ezen.spring.service;

import com.ezen.spring.dto.NoticeDTO;
import com.ezen.spring.dto.NoticeFileDTO;
import com.ezen.spring.dto.TempFileDTO;
import com.ezen.spring.entity.File;
import com.ezen.spring.entity.Notice;
import com.ezen.spring.entity.TempFile;
import com.ezen.spring.repository.FileRepository;
import com.ezen.spring.repository.NoticeRepository;
import com.ezen.spring.repository.TempFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
        // 공지사항 작성
        Long num = noticeRepository.save(convertDtoToEntity(noticeFileDTO.getNoticeDTO())).getNoticeId();

        if(num != null && !noticeFileDTO.getUuidArr().isEmpty()){
            for(String uuid : noticeFileDTO.getUuidArr()){
                Optional<TempFile> optional = tempFileRepository.findById(uuid);
                if(optional.isPresent()){
                    optional.get().setNoticeId(num);
                    // file 테이블에 저장
                    fileRepository.save(convertTempToReal(optional.get()));
                    // tempFile 테이블은 삭제
                    tempFileRepository.deleteById(uuid);
                }
            }
        }

        return num;
    }

    @Override
    public int tempSave(TempFileDTO tempFileDTO) {
        // 사진 임시 저장
        return tempFileRepository.save(tfconvertDtoToEntity(tempFileDTO)).getUuid() != null? 1 : 0;
    }

    @Override
    public List<NoticeDTO> read() {
        // 목록 데이터 가져오기
        List<Notice> noticeList = noticeRepository.getList();
        List<NoticeDTO> noticeDTOList = new ArrayList<>();
        for (Notice notice : noticeList) {
            notice.setContent(null);
            noticeDTOList.add(convertEntityToDto(notice));
        }
        return noticeDTOList;
    }

    @Override
    public NoticeDTO getDetail(Long noticeId) {
        Optional<Notice> optional = noticeRepository.findById(noticeId);
        if(optional.isPresent()){
            return convertEntityToDto(optional.get());
        }
        return null;
    }

    @Override
    public int deleteFile(String uuid) {
        // 사용자의 파일 삭제를 감지해서
        // 두 방향으로
        // 1. file 테이블에 저장된 이미지인지 (수정) - file 에서 삭제
        // 2. 새로 작성하던 이미지인지 (입력) - tempfile 에서 삭제
        Optional<File> optional1 = fileRepository.findById(uuid);
        if(optional1.isPresent()){
            fileRepository.deleteById(uuid);
            return 1;
        }

        Optional<TempFile> optional2 = tempFileRepository.findById(uuid);
        if(optional2.isPresent()){
            tempFileRepository.deleteById(uuid);
            return 1;
        }

        return 0;
    }

    @Transactional
    @Override
    public Long update(NoticeFileDTO noticeFileDTO) {
        Optional<Notice> optional = noticeRepository.findById(noticeFileDTO.getNoticeDTO().getNoticeId());
        Long num = 0L;
        if(optional.isPresent()){
            Notice notice = optional.get();
            notice.setTitle(noticeFileDTO.getNoticeDTO().getTitle());
            notice.setContent(noticeFileDTO.getNoticeDTO().getContent());
            notice.setWriter(noticeFileDTO.getNoticeDTO().getWriter());
            notice.setUserId(noticeFileDTO.getNoticeDTO().getUserId());
            num = noticeRepository.save(notice).getNoticeId();
        }

        if(num != 0L && !noticeFileDTO.getUuidArr().isEmpty()){
            for(String uuid : noticeFileDTO.getUuidArr()) {
                Optional<File> file = fileRepository.findById(uuid);
                // file 테이블에 데이터가 있는지 = 기존에 저장된 파일인지
                if (file.isEmpty()) {
                    // 데이터가 없다면 = 새롭게 추가된 파일인 것
                    Optional<TempFile> tempFile = tempFileRepository.findById(uuid);
                    // 파일 저장을 위해 tempFile의 정보를 가져와 noticeId 세팅해 저장하기
                    if (tempFile.isPresent()) {
                        tempFile.get().setNoticeId(num);
                        fileRepository.save(convertTempToReal(tempFile.get()));
                        tempFileRepository.deleteById(uuid);
                    }
                }
            }
        }

        return num;
    }

    @Override
    public Long delete(Long noticeId) {
        Optional<Notice> optional = noticeRepository.findById(noticeId);
        if(optional.isPresent()){
            optional.get().setIsDel("Y");
            Long num = noticeRepository.save(optional.get()).getNoticeId();
            return num;
        }
        return null;
    }
}

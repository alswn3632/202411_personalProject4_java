package com.ezen.spring.service;

import com.ezen.spring.dto.FileDTO;
import com.ezen.spring.dto.NoticeDTO;
import com.ezen.spring.dto.NoticeFileDTO;
import com.ezen.spring.dto.TempFileDTO;
import com.ezen.spring.entity.File;
import com.ezen.spring.entity.Notice;
import com.ezen.spring.entity.TempFile;

import java.util.List;

public interface NoticeService {

    default Notice convertDtoToEntity(NoticeDTO noticeDTO){
        return Notice.builder()
                .noticeId(noticeDTO.getNoticeId())
                .title(noticeDTO.getTitle())
                .writer(noticeDTO.getWriter())
                .content(noticeDTO.getContent())
                .userId(noticeDTO.getUserId())
                .isDel(noticeDTO.getIsDel())
                .build();
    }

    default NoticeDTO convertEntityToDto(Notice notice){
        return NoticeDTO.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .writer(notice.getWriter())
                .content(notice.getContent())
                .userId(notice.getUserId())
                .isDel(notice.getIsDel())
                .regAt(notice.getRegAt())
                .modAt(notice.getModAt())
                .build();
    }

    default File convertDtoToEntity(FileDTO fileDTO){
        return File.builder()
                .uuid(fileDTO.getUuid())
                .saveDir(fileDTO.getSaveDir())
                .fileName(fileDTO.getFileName())
                .fileSize(fileDTO.getFileSize())
                .noticeId(fileDTO.getNoticeId())
                .build();
    }

    default FileDTO convertEntityToDto(File file){
        return FileDTO.builder()
                .uuid(file.getUuid())
                .saveDir(file.getSaveDir())
                .fileName(file.getFileName())
                .fileSize(file.getFileSize())
                .noticeId(file.getNoticeId())
                .regAt(file.getRegAt())
                .modAt(file.getModAt())
                .build();
    }

    default TempFile tfconvertDtoToEntity(TempFileDTO tempfileDTO){
        return TempFile.builder()
                .uuid(tempfileDTO.getUuid())
                .saveDir(tempfileDTO.getSaveDir())
                .fileName(tempfileDTO.getFileName())
                .fileSize(tempfileDTO.getFileSize())
                .noticeId(tempfileDTO.getNoticeId())
                .build();
    }

    default TempFileDTO tfconvertEntityToDto(TempFile tempfile){
        return TempFileDTO.builder()
                .uuid(tempfile.getUuid())
                .saveDir(tempfile.getSaveDir())
                .fileName(tempfile.getFileName())
                .fileSize(tempfile.getFileSize())
                .noticeId(tempfile.getNoticeId())
                .regAt(tempfile.getRegAt())
                .modAt(tempfile.getModAt())
                .build();
    }

    default File convertTempToReal(TempFile tempFile){
        return File.builder()
                .uuid(tempFile.getUuid())
                .saveDir(tempFile.getSaveDir())
                .fileName(tempFile.getFileName())
                .fileSize(tempFile.getFileSize())
                .noticeId(tempFile.getNoticeId())
                .build();
    }

    Long create(NoticeFileDTO noticeFileDTO);

    int tempSave(TempFileDTO tempFileDTO);

    List<NoticeDTO> read();

    NoticeDTO getDetail(Long noticeId);

    int deleteFile(String uuid);

    Long update(NoticeFileDTO noticeFileDTO);

    Long delete(Long noticeId);
}

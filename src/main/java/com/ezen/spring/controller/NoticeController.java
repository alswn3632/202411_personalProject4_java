package com.ezen.spring.controller;

import com.ezen.spring.dto.NoticeDTO;
import com.ezen.spring.dto.NoticeFileDTO;
import com.ezen.spring.dto.TempFileDTO;
import com.ezen.spring.handler.FileHandler;
import com.ezen.spring.service.NoticeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/notice/*")
@RequiredArgsConstructor
@Controller
public class NoticeController {
    private final NoticeService noticeService;
    private final FileHandler fileHandler;

    @GetMapping("/register")
    public void register(){}

    @ResponseBody
    @PostMapping("/register")
    public String register(@RequestBody NoticeDTO noticeDTO){

        List<String> uuidArr = fileHandler.extractUuids(noticeDTO.getContent());
        NoticeFileDTO noticeFileDTO = new NoticeFileDTO(noticeDTO, uuidArr);

        Long num = noticeService.create(noticeFileDTO);

        return num != null? "1" : "0";
    }

    @PostMapping("/upload")
    @ResponseBody
    public Map<String, String> imageUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        TempFileDTO tempFileDTO = fileHandler.uploadFile(file);
        log.info(">>>> fileDTO > {}", tempFileDTO);

        int isOk = noticeService.tempSave(tempFileDTO);

        if(isOk > 0){
            Map<String, String> response = new HashMap<>();
            String path = "/upload/" + tempFileDTO.getSaveDir() + "/" + tempFileDTO.getUuid() + "_" + tempFileDTO.getFileName();

            response.put("link", path);
            return response;
        }

        return null;
    }


}

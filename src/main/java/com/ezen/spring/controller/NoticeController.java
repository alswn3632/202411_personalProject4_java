package com.ezen.spring.controller;

import com.ezen.spring.dto.NoticeDTO;
import com.ezen.spring.dto.NoticeFileDTO;
import com.ezen.spring.dto.PagingVO;
import com.ezen.spring.dto.TempFileDTO;
import com.ezen.spring.handler.FileHandler;
import com.ezen.spring.service.NoticeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
            //D:\_myproject\_java\_fileUpload\2024\11\28
            String path = "/upload/" + tempFileDTO.getSaveDir() + "/" + tempFileDTO.getUuid() + "_" + tempFileDTO.getFileName();

            response.put("link", path);
            return response;
        }

        return null;
    }

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                       @RequestParam(value = "type", required = false) String type,
                       @RequestParam(value = "keyword", required = false) String keyword){

        Page<NoticeDTO> list = noticeService.read(pageNo, type, keyword);
        PagingVO pgvo = new PagingVO(list, type, keyword);
        log.info(">>>> list > {}", list);
        model.addAttribute("list", list);
        model.addAttribute("pgvo", pgvo);

        return "/notice/list";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("noticeId") Long noticeId ,Model model){
        NoticeDTO noticeDTO = noticeService.getDetail(noticeId);
        model.addAttribute("noticeDTO", noticeDTO);

        return "/notice/detail";
    }

    @GetMapping("/modify")
    public String modify(@RequestParam("noticeId") Long noticeId ,Model model){
        NoticeDTO noticeDTO = noticeService.getDetail(noticeId);
        model.addAttribute("noticeDTO", noticeDTO);

        return "/notice/modify";
    }

    @ResponseBody
    @PostMapping("/modify")
    public String modify(@RequestBody NoticeDTO noticeDTO){
        log.info(">>>> update noticeDTO > {}", noticeDTO);
        List<String> uuidArr = fileHandler.extractUuids(noticeDTO.getContent());
        NoticeFileDTO noticeFileDTO = new NoticeFileDTO(noticeDTO, uuidArr);

        Long num = noticeService.update(noticeFileDTO);

        return num != null? "1" : "0";
    }

    @ResponseBody
    @GetMapping(value = "/deleteFile/{uuid}")
    public String deleteFile(@PathVariable("uuid") String uuid){
        log.info(">>>> uuid > {}", uuid);
        int isOk = noticeService.deleteFile(uuid);
        log.info(">>>> deleteFile is {}", (isOk>0? "성공" : "실패"));
        return isOk>0? "1" : "0";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("noticeId") Long noticeId){
        Long num = noticeService.delete(noticeId);
        return "redirect:/notice/list";
    }


}

package com.ezen.spring.handler;

import com.ezen.spring.dto.FileDTO;
import com.ezen.spring.dto.TempFileDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class FileHandler {
    private final String UP_DIR = "D:\\_myproject\\_java\\_fileUpload\\";

    public TempFileDTO uploadFile (MultipartFile file){
        // 업로드 날짜의 폴더 생성
        LocalDate date = LocalDate.now();
        String today = date.toString().replace("-", File.separator);

        File folders = new File(UP_DIR, today);

        if(!folders.exists()){ // **월 **일의 폴더가 없다면 생성하자
            folders.mkdirs();
        }

        // FileDTO 생성
        TempFileDTO tempFileDTO = new TempFileDTO();

        // saveDir
        tempFileDTO.setSaveDir(today); // 위에서 폴더를 생성했으니 여기에 저장할 수 있겠죠?

        // fileSize
        tempFileDTO.setFileSize(file.getSize());

        // fileName
        // 일반적으로 file.name이 경로를 포함하는 경우가 많다. 경로가 붙은 파일 이름의 끝 \\ 찾아서 그 뒤의 이름을 사용
        String originalFileName = file.getOriginalFilename();
        String onlyFileName = originalFileName.substring((originalFileName.lastIndexOf(File.separator)+1));
        tempFileDTO.setFileName(onlyFileName);

        // uuid
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        tempFileDTO.setUuid(uuidStr);

        // 실제 파일 저장
        String fileName = uuidStr + "_" + onlyFileName;
        File storeFile = new File(folders, fileName);

        try {
            file.transferTo(storeFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tempFileDTO;
    }

    public List<String> extractUuids(String content) {
        List<String> uuidList = new ArrayList<>();

        // img 태그에서 src 속성을 추출하는 정규 표현식
        Pattern pattern = Pattern.compile("<img[^>]*src=[\"']([^\"']+)[\"'][^>]*>");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            // 이미지 경로 추출
            String imagePath = matcher.group(1);

            // UUID만 추출
            String uuid = extractUuidFromPath(imagePath);

            if (uuid != null) {
                uuidList.add(uuid);
            }
        }

        return uuidList;
    }

    // 경로에서 UUID만 추출 (파일명에서 첫 번째 "_" 앞의 문자열)
    private String extractUuidFromPath(String imagePath) {
        // 마지막 '/' 이후부터 첫 번째 '_'까지의 문자열을 UUID로 추출
        String fileNamePart = imagePath.substring(imagePath.lastIndexOf("/") + 1);
        String uuid = fileNamePart.split("_")[0];  // UUID 부분만 추출
        return uuid;
    }

}

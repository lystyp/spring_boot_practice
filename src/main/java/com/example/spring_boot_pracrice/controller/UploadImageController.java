package com.example.spring_boot_pracrice.controller;

import com.example.spring_boot_pracrice.Utils.OAuth2AuthorizedClientProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@Slf4j
public class UploadImageController {
    @Autowired
    private OAuth2AuthorizedClientProvider oauth2AuthorizedClientProvider;

    public UploadImageController() {
    }

    @GetMapping("/upload")
    public String uploadView() {
        return "upload";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file1") MultipartFile file1,
                         @RequestParam("file2") MultipartFile file2,
                         @RequestParam("file3") MultipartFile file3,
                         HttpServletRequest request) throws IOException {
        if (oauth2AuthorizedClientProvider.getClient() == null
        || !oauth2AuthorizedClientProvider.getClient().getClientRegistration().getClientName().equals("Google")) {
            HttpSession session = request.getSession();
            session.setAttribute("requestUrl", "/upload");
            return "redirect:/oauth2/authorization/google";
        }

        String token = oauth2AuthorizedClientProvider.getClient().getAccessToken().getTokenValue();
        log.debug("file1 : " + file1.getOriginalFilename());
        log.debug("file2 : " + file2.getOriginalFilename());
        log.debug("file3 : " + file3.getOriginalFilename());
        if (!file1.isEmpty()) {
            uploadFile(token, file1);
        }
        if (!file2.isEmpty()) {
            uploadFile(token, file2);
        }
        if (!file3.isEmpty()) {
            uploadFile(token, file3);
        }


        return "upload";
    }

    private void uploadFile(String accessToken, MultipartFile file) {
        log.info("uploadFile : " + file.getOriginalFilename());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<byte[]> requestEntity = null;
        try {

            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentDispositionFormData("file", file.getOriginalFilename());

            requestEntity = new HttpEntity<>(file.getBytes(), headers);


        } catch (IOException e) {
            log.error("Upload error : " + e.toString());
            throw new RuntimeException(e);
        }
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                "https://www.googleapis.com/upload/drive/v3/files?uploadType=media",
                requestEntity,
                String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("文件已成功上傳到Google Drive！");
        } else {
            log.info("文件上傳失敗，API響應狀態碼：" + responseEntity.getStatusCodeValue());
        }
    }


}

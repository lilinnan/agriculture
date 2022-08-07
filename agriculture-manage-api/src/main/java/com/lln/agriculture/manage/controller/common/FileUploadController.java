package com.lln.agriculture.manage.controller.common;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.manage.service.common.FileUploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/6 15:57
 */

@RestController
@RequestMapping("/v1/file-upload")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping
    public Message upload(@RequestParam("file") MultipartFile multipartFile) {
        return fileUploadService.uploadFile(multipartFile);
    }
}

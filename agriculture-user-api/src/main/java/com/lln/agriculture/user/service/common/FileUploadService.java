package com.lln.agriculture.user.service.common;

import com.lln.agriculture.common.domain.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/6 16:50
 */

@Service
public interface FileUploadService {

    /**
     * 上传文件
     *
     * @param multipartFile multipartFile
     * @return 文件链接
     */
    Message uploadFile(MultipartFile multipartFile);


    Message uploadAvatar(String base64);
}

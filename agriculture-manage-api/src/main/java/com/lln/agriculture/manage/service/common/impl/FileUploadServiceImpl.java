package com.lln.agriculture.manage.service.common.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.exception.BadRequestException;
import com.lln.agriculture.common.util.CommonUtils;
import com.lln.agriculture.common.util.ImageUtils;
import com.lln.agriculture.common.util.NameUtils;
import com.lln.agriculture.manage.config.AliYunConfig;
import com.lln.agriculture.manage.service.common.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/6 16:51
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final AliYunConfig aliYunConfig;

    @Autowired
    public FileUploadServiceImpl(AliYunConfig aliYunConfig) {
        this.aliYunConfig = aliYunConfig;
    }

    @Override
    public Message uploadFile(MultipartFile multipartFile) {
        String name = UUID.randomUUID().toString() + CommonUtils.getFileSuffix(multipartFile.getOriginalFilename());
        try {
            return upload(name, multipartFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Message uploadAvatar(String base64) {
        return upload(NameUtils.randomFilename("png"), ImageUtils.decodeBase64ImgToInputStream(base64));
    }


    private Message upload(String name, InputStream inputStream) {
        if (inputStream == null) {
            throw new BadRequestException("上传失败");
        }
//        name = "test/" + name;
        OSS ossClient = new OSSClientBuilder().build(aliYunConfig.getEndPoint(), aliYunConfig.getAccessKeyId(), aliYunConfig.getAccessKeySecret());
        try {
            try {
                ossClient.putObject("img-lilinnan", name, inputStream);
            } catch (OSSException | ClientException e) {
                throw new BadRequestException("上传失败");
            }
            return Message.builder().status(200).data("https://img.lilinnan.com/" + name).build();
        } finally {
            ossClient.shutdown();
        }
    }
}

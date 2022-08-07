package com.lln.agriculture.manage.controller.admininfo;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.exception.BadRequestException;
import com.lln.agriculture.manage.service.admininfo.AdminInfoService;
import com.lln.agriculture.manage.service.common.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/28 11:43
 */

@RestController
@RequestMapping("/v1/admin-info/avatar")
public class AvatarController {

    private final AdminInfoService adminInfoService;
    private final FileUploadService fileUploadService;

    @Autowired
    public AvatarController(AdminInfoService adminInfoService, FileUploadService fileUploadService) {
        this.adminInfoService = adminInfoService;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping
    public Message getMyAvatar(RedirectAttributes redirectAttributes) {
        return adminInfoService.getAvatar();
    }

    @PostMapping
    public Message upload(String avatar) {
        if (avatar == null || avatar.length() == 0) {
            throw new BadRequestException("数据为空");
        }
        return fileUploadService.uploadAvatar(avatar);
    }

}

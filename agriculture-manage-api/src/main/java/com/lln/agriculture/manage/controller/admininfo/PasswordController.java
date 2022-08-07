package com.lln.agriculture.manage.controller.admininfo;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.manage.domain.admininfo.ChangePassword;
import com.lln.agriculture.manage.service.admininfo.AdminInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/17 18:39
 */
@RestController
@RequestMapping("/v1/admin-info/password")
public class PasswordController {

    private final AdminInfoService adminInfoService;

    @Autowired
    public PasswordController(AdminInfoService adminInfoService) {
        this.adminInfoService = adminInfoService;
    }

    @PutMapping
    public Message changePassword(@Valid @RequestBody ChangePassword changePassword) {
        return adminInfoService.changePassword(changePassword);
    }

}

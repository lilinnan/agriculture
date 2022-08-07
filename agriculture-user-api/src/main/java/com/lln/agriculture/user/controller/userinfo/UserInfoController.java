package com.lln.agriculture.user.controller.userinfo;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.user.service.userinfo.UserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/19 14:52
 */


@RestController
@RequestMapping("/v1/user-info/user-info")
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping
    public Message userInfo() {
        return userInfoService.getUserInfo();
    }
}

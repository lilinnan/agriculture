package com.lln.agriculture.manage.controller.userinfo;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.common.domain.userinfo.User;
import com.lln.agriculture.manage.service.userinfo.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/27 17:13
 */

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_SELECT')")
    public Message selectAllUserPage(PageRequest pageRequest) {
        return userService.selectAllUserPage(pageRequest);
    }

    @PutMapping("/lock")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public Message updateUser(@RequestBody User user) {
        return userService.lockUser(user);
    }

}

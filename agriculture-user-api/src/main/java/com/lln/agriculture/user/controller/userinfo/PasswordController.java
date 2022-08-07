package com.lln.agriculture.user.controller.userinfo;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.user.service.userinfo.PasswordService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/16 21:40
 */

@RestController
@RequestMapping("/v1/user-info/password")
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @GetMapping("find/{mail}/{token}")
    public Message findPassword(@PathVariable String mail, HttpServletRequest httpServletRequest, @PathVariable String token) {
        return passwordService.sendFindPasswordMail(mail, httpServletRequest, token);
    }

    @PutMapping("find")
    public Message changeFindPassword(@RequestBody @Valid PasswordService.ChangeFindPassword changeFindPassword) {
        return passwordService.changeFindPassword(changeFindPassword);
    }

    @PutMapping("change")
    public Message changePassword(@RequestBody @Valid PasswordService.ChangePassword changePassword) {
        return passwordService.changePassword(changePassword);
    }

}

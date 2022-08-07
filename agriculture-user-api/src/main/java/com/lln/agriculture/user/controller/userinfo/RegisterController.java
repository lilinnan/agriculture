package com.lln.agriculture.user.controller.userinfo;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.user.service.userinfo.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/16 14:15
 */

@RestController
@RequestMapping("/v1/user-info/register")
public class RegisterController {

    private final RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }


    @GetMapping("/{mail}/{token}")
    public Message resendMail(@PathVariable String mail, HttpServletRequest httpServletRequest, @PathVariable String token) {
        return registerService.resendMail(mail, httpServletRequest, token);
    }

    @PutMapping("/{jwt}")
    public Message mailCheck(@PathVariable String jwt) {
        return registerService.mailCheck(jwt);
    }

    @PostMapping
    public Message register(@Valid @RequestBody RegisterService.RegisterBean registerBean, HttpServletRequest httpServletRequest) {
        return registerService.register(registerBean, httpServletRequest);
    }
}

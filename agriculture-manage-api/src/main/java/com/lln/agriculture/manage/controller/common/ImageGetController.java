package com.lln.agriculture.manage.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/24 11:07
 */

@Controller
@RequestMapping("/img")
public class ImageGetController {


    @GetMapping
    public void img(String url, HttpServletResponse httpServletResponse) {

    }
}

package com.lln.agriculture.user.controller.agriculture;

import com.lln.agriculture.common.agriculture.AgricultureDetail;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.user.service.agriculture.AgricultureDetailService;
import org.springframework.web.bind.annotation.*;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/12/13 16:44
 */


@RestController
@RequestMapping("/v1/agriculture/detail")
public class AgricultureDetailController {

    private final AgricultureDetailService agricultureDetailService;

    public AgricultureDetailController(AgricultureDetailService agricultureDetailService) {
        this.agricultureDetailService = agricultureDetailService;
    }

    @GetMapping("/{id}")
    public Message selectDetail(@PathVariable("id") int id) {
        return agricultureDetailService.selectDetail(id);
    }

}

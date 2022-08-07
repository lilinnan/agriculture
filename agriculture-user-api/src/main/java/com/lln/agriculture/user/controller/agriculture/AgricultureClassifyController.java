package com.lln.agriculture.user.controller.agriculture;

import com.lln.agriculture.common.agriculture.AgricultureClassify;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.user.service.agriculture.AgricultureClassifyService;
import org.springframework.web.bind.annotation.*;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/11/16 10:18
 */

@RestController
@RequestMapping("/v1/agriculture/classify")
public class AgricultureClassifyController {

    private final AgricultureClassifyService agricultureClassifyService;

    public AgricultureClassifyController(AgricultureClassifyService agricultureClassifyService) {
        this.agricultureClassifyService = agricultureClassifyService;
    }

    @GetMapping("/{entityId}")
    public Message getAllAgricultureClassifyByEntityId(@PathVariable int entityId) {
        return agricultureClassifyService.selectAllClassifyByEntityId(entityId);
    }

}

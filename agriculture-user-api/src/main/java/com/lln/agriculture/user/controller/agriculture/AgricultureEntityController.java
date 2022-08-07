package com.lln.agriculture.user.controller.agriculture;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.user.service.agriculture.AgricultureEntityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/11/12 18:53
 */

@RestController
@RequestMapping("/v1/agriculture/entity")
public class AgricultureEntityController {

    private final AgricultureEntityService agricultureEntityService;

    public AgricultureEntityController(AgricultureEntityService agricultureEntityService) {
        this.agricultureEntityService = agricultureEntityService;
    }

    @GetMapping("/{type}")
    public Message getAllAgricultureEntity(@PathVariable String type, PageRequest pageRequest) {
        return agricultureEntityService.selectAllEntity(pageRequest, "plant".equals(type));
    }

}

package com.lln.agriculture.manage.controller.agriculture;

import com.lln.agriculture.common.agriculture.AgricultureEntity;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.manage.service.agriculture.AgricultureEntityService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasAuthority('AGRICULTURE_ENTITY_SELECT')")
    public Message getAllAgricultureEntity(@PathVariable String type, PageRequest pageRequest) {
        return agricultureEntityService.selectAllEntity(pageRequest, "plant".equals(type));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('AGRICULTURE_ENTITY_INSERT')")
    public Message addAgricultureEntity(@RequestBody AgricultureEntity agricultureEntity) {
        return agricultureEntityService.insertEntity(agricultureEntity);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('AGRICULTURE_ENTITY_UPDATE')")
    public Message editAgricultureEntity(@RequestBody AgricultureEntity agricultureEntity) {
        return agricultureEntityService.updateEntity(agricultureEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('AGRICULTURE_ENTITY_DELETE')")
    public Message deleteAgricultureEntity(@PathVariable int id) {
        return agricultureEntityService.deleteEntity(id);
    }

}

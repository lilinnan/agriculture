package com.lln.agriculture.manage.controller.agriculture;

import com.lln.agriculture.common.agriculture.AgricultureClassify;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.manage.service.agriculture.AgricultureClassifyService;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('AGRICULTURE_CLASSIFY_SELECT')")
    public Message getAllAgricultureClassifyByEntityId(@PathVariable int entityId) {
        return agricultureClassifyService.selectAllClassifyByEntityId(entityId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('AGRICULTURE_CLASSIFY_INSERT')")
    public Message addAgricultureClassify(@RequestBody AgricultureClassify agricultureClassify) {
        return agricultureClassifyService.insertClassify(agricultureClassify);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('AGRICULTURE_CLASSIFY_UPDATE')")
    public Message editAgricultureClassify(@RequestBody AgricultureClassify agricultureClassify) {
        return agricultureClassifyService.updateClassify(agricultureClassify);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('AGRICULTURE_CLASSIFY_DELETE')")
    public Message deleteAgricultureClassify(@PathVariable int id) {
        return agricultureClassifyService.deleteClassify(id);
    }
}

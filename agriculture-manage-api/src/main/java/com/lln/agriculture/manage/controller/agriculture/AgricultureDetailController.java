package com.lln.agriculture.manage.controller.agriculture;

import com.lln.agriculture.common.agriculture.AgricultureDetail;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.manage.service.agriculture.AgricultureDetailService;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping
    @PreAuthorize("hasAuthority('AGRICULTURE_DETAIL_INSERT')")
    public Message insertDetail(@RequestBody AgricultureDetail agricultureDetail) {
        return agricultureDetailService.insertDetail(agricultureDetail);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('AGRICULTURE_DETAIL_UPDATE')")
    public Message updateDetail(@RequestBody AgricultureDetail agricultureDetail) {
        return agricultureDetailService.updateDetail(agricultureDetail);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('AGRICULTURE_DETAIL_SELECT')")
    public Message selectDetail(@PathVariable("id") int id) {
        return agricultureDetailService.selectDetail(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('AGRICULTURE_DETAIL_DELETE')")
    public Message deleteDetail(@PathVariable("id") int id) {
        return agricultureDetailService.deleteDetail(id);
    }
}

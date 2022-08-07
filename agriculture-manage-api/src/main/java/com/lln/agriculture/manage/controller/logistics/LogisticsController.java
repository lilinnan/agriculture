package com.lln.agriculture.manage.controller.logistics;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.common.domain.order.LogisticsCompany;
import com.lln.agriculture.manage.service.logistics.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/28 11:38
 */

@RestController
@RequestMapping("/v1/logistics")
public class LogisticsController {

    private final LogisticsService logisticsService;

    @Autowired
    public LogisticsController(LogisticsService logisticsService) {
        this.logisticsService = logisticsService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('LOGISTICS_COMPANY_INSERT')")
    public Message insert(@RequestBody LogisticsCompany logisticsCompany) {
        return logisticsService.insertLogistics(logisticsCompany);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('LOGISTICS_COMPANY_SELECT')")
    public Message selectAll(PageRequest pageRequest) {
        return logisticsService.selectAllLogistics(pageRequest);
    }

    @GetMapping("/simple")
    @PreAuthorize("hasAuthority('ORDER_UPDATE')")
    public Message selectAllSimple(PageRequest pageRequest) {
        return logisticsService.selectAllLogistics(pageRequest);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('LOGISTICS_COMPANY_UPDATE')")
    public Message update(@RequestBody LogisticsCompany logisticsCompany) {
        return logisticsService.updateLogistics(logisticsCompany);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('LOGISTICS_COMPANY_DELETE')")
    public Message delete(@PathVariable int id) {
        return logisticsService.deleteLogistics(id);
    }
}

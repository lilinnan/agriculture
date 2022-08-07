package com.lln.agriculture.manage.controller.authority;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.manage.service.authority.AdminAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/21 18:42
 */


@RestController
@RequestMapping("/v1/authority/authority")
public class AdminAuthorityController {

    private final AdminAuthorityService adminAuthorityService;

    @Autowired
    public AdminAuthorityController(AdminAuthorityService adminAuthorityService) {
        this.adminAuthorityService = adminAuthorityService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('AUTHORITY_SELECT')")
    public Message getAllAuthority(PageRequest pageRequest) {
        return adminAuthorityService.selectAllAuthority(pageRequest);
    }

    @GetMapping("/simple")
    @PreAuthorize("hasAnyAuthority('ROLE_INSERT','ROLE_UPDATE')")
    public Message getAllAuthoritySimpleInfo(PageRequest pageRequest) {
        return adminAuthorityService.selectAllAuthoritySimple(pageRequest);
    }

}

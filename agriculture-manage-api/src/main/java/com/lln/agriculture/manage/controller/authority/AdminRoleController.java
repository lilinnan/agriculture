package com.lln.agriculture.manage.controller.authority;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.manage.domain.authority.AdminRole;
import com.lln.agriculture.manage.service.authority.AdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/24 19:22
 */
@RestController
@RequestMapping("/v1/authority/role")
public class AdminRoleController {

    private final AdminRoleService adminRoleService;

    @Autowired
    public AdminRoleController(AdminRoleService adminRoleService) {
        this.adminRoleService = adminRoleService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SELECT')")
    public Message getAllRole(PageRequest pageRequest) {
        return adminRoleService.selectAllRole(pageRequest);
    }

    @GetMapping("/simple")
    @PreAuthorize("hasAnyAuthority('ADMIN_USER_INSERT','ADMIN_USER_UPDATE')")
    public Message getAllRoleSimpleInfo(PageRequest pageRequest) {
        return adminRoleService.selectAllRoleSimple(pageRequest);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_INSERT')")
    public Message addRole(@Valid @RequestBody AdminRole adminRole) {
        return adminRoleService.insertRole(adminRole);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public Message updateRole(@Valid @RequestBody AdminRole adminRole) {
        return adminRoleService.updateRole(adminRole);
    }

    @DeleteMapping("/{adminRoleId}")
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    public Message deleteRole(@PathVariable Integer adminRoleId) {
        return adminRoleService.deleteRole(adminRoleId);
    }

}

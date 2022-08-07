package com.lln.agriculture.manage.controller.authority;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.manage.domain.admininfo.AdminUser;
import com.lln.agriculture.manage.service.authority.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/27 17:51
 */


@RestController
@RequestMapping("/v1/authority/admin-user")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Autowired
    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }


    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN_USER_INSERT')")
    public Message addUser(@Valid @RequestBody AdminUser adminUser) {
        return adminUserService.insertUser(adminUser);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN_USER_SELECT')")
    public Message getAllUser(PageRequest pageRequest) {
        return adminUserService.getAllUser(pageRequest);
    }


    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN_USER_UPDATE')")
    public Message updateRole(@Valid @RequestBody AdminUser adminUser) {
        return adminUserService.updateUser(adminUser);
    }

    @DeleteMapping("/{adminUserId}")
    @PreAuthorize("hasAuthority('ADMIN_USER_DELETE')")
    public Message deleteRole(@PathVariable Integer adminUserId) {
        return adminUserService.deleteUser(adminUserId);
    }


}

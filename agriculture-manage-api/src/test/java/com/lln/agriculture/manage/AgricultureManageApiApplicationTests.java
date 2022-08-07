package com.lln.agriculture.manage;

import com.lln.agriculture.manage.mapper.authority.AdminRoleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AgricultureManageApiApplicationTests {

    @Autowired
    AdminRoleMapper adminRoleMapper;

    @Test
    void contextLoads() {
    }

}

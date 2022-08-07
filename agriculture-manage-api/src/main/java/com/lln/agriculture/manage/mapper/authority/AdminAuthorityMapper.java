package com.lln.agriculture.manage.mapper.authority;

import com.lln.agriculture.manage.domain.authority.AdminAuthority;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminAuthorityMapper {
    List<AdminAuthority> selectAllPage(String key);
}
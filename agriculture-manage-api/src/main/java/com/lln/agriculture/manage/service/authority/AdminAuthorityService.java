package com.lln.agriculture.manage.service.authority;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/21 18:52
 */

public interface AdminAuthorityService {

    Message selectAllAuthority(PageRequest pageRequest);

    Message selectAllAuthoritySimple(PageRequest pageRequest);
}

package com.lln.agriculture.user.service.userinfo.impl;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.util.CommonUtils;
import com.lln.agriculture.user.service.userinfo.UserInfoService;
import org.springframework.stereotype.Service;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/19 14:53
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Override
    public Message getUserInfo() {
        return Message.builder()
                .data(CommonUtils.getCurrentUsername())
                .build();
    }
}

package com.lln.agriculture.user.service.agriculture;

import com.lln.agriculture.common.agriculture.AgricultureEntity;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/11/12 17:04
 */

public interface AgricultureEntityService {

    Message selectAllEntity(PageRequest pageRequest,boolean plant);

}

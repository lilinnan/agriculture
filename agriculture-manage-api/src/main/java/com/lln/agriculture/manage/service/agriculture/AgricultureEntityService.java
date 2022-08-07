package com.lln.agriculture.manage.service.agriculture;

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

    Message updateEntity(AgricultureEntity agricultureEntity);

    Message deleteEntity(int id);

    Message insertEntity(AgricultureEntity agricultureEntity);

}

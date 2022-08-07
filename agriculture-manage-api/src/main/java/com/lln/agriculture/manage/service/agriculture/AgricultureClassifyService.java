package com.lln.agriculture.manage.service.agriculture;

import com.lln.agriculture.common.agriculture.AgricultureClassify;
import com.lln.agriculture.common.domain.Message;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/11/16 10:20
 */

public interface AgricultureClassifyService {

    Message selectAllClassifyByEntityId(int entityId);

    Message updateClassify(AgricultureClassify agricultureClassify);

    Message deleteClassify(int id);

    Message insertClassify(AgricultureClassify agricultureClassify);

}

package com.lln.agriculture.user.service.agriculture;

import com.lln.agriculture.common.agriculture.AgricultureClassify;
import com.lln.agriculture.common.domain.Message;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/11/16 10:20
 */

public interface AgricultureClassifyService {

    Message selectAllClassifyByEntityId(int entityId);


}

package com.lln.agriculture.manage.service.agriculture;

import com.lln.agriculture.common.agriculture.AgricultureClassify;
import com.lln.agriculture.common.agriculture.AgricultureDetail;
import com.lln.agriculture.common.domain.Message;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/12/13 13:55
 */

public interface AgricultureDetailService {


    Message selectDetail(int detailId);

    Message updateDetail(AgricultureDetail agricultureDetail);

    Message deleteDetail(int detailId);

    Message insertDetail(AgricultureDetail agricultureDetail);


}

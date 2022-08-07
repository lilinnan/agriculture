package com.lln.agriculture.manage.service.logistics;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.common.domain.order.LogisticsCompany;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/28 11:09
 */

public interface LogisticsService {

    Message selectAllLogistics(PageRequest pageRequest);

    Message selectAllLogisticsSimple(PageRequest pageRequest);

    Message updateLogistics(LogisticsCompany logisticsCompany);

    Message deleteLogistics(int id);

    Message insertLogistics(LogisticsCompany logisticsCompany);

}

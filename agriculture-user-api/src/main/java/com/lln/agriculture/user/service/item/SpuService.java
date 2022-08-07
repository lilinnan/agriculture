package com.lln.agriculture.user.service.item;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/17 13:27
 */

public interface SpuService {

    Message latestSimpleSpu(int num);

    Message selectBuyInfoBySkuId(int skuId);

    Message selectSpuById(int spuId);

    Message search(PageRequest pageRequest);

}

package com.lln.agriculture.manage.service.item;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.common.domain.item.Spu;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/11 20:11
 */

public interface SpuService {

    Message insertSpu(Spu spu);


    Message deleteSpu(int id);


    Message realDeleteSpu(int id);


    Message updateSpu(Spu spu);


    Message selectAllSpu(PageRequest pageRequest);


    Message selectAllSpuSimple(PageRequest pageRequest);


    Message selectOneSpu(int spuId);

    Message updateSaleable(Spu spu);
}

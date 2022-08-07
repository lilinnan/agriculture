package com.lln.agriculture.manage.service.item;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/15 9:43
 */

public interface SpuRecycleBinService {

    Message selectAllDeletedSpu(PageRequest pageRequest);

    Message restoreDeletedSpu(int id);

    Message realDeleteSpu(int id);
}

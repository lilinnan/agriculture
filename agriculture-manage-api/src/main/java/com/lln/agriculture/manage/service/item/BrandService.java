package com.lln.agriculture.manage.service.item;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.common.domain.item.Brand;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/4 15:21
 */

public interface BrandService {

    Message selectAllBrand(PageRequest pageRequest);

    Message selectAllBrandSimple(PageRequest pageRequest);

    Message updateBrand(Brand brand);

    Message deleteBrand(int id);

    Message insertBrand(Brand brand);
}

package com.lln.agriculture.common.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lln.agriculture.common.domain.PageRequest;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/22 10:16
 */

public class PageUtils {

    public static <E> Page<E> startPage(PageRequest pageRequest) {
        if (pageRequest.getPageSize() <= 0) {
            PageHelper.orderBy(pageRequest.getSort());
            return null;
        }
        return PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize(), pageRequest.getSort());
    }
}

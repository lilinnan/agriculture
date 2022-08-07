package com.lln.agriculture.manage.service.item;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.item.Category;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/3 16:58
 */

public interface CategoryService {


    /**
     * 插入分类
     *
     * @param category 分类
     * @return Message
     */
    Message insertCategory(Category category);


    /**
     * 删除分类
     *
     * @param id 要删除的分类id
     * @return Message
     */
    Message deleteCategory(int id);


    /**
     * 修改分类名称
     *
     * @param category 分类
     * @return Message
     */
    Message updateCategory(Category category);


    /**
     * 查询树
     *
     * @return Message
     */
    Message selectAllCategory();


    Message selectAllCategorySimple();


}

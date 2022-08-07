package com.lln.agriculture.manage.service.item.impl;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.item.Category;
import com.lln.agriculture.manage.mapper.item.CategoryMapper;
import com.lln.agriculture.manage.service.item.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/3 17:23
 */

@Service
public class CategoryServiceImpl implements CategoryService {


    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message insertCategory(Category category) {
        categoryMapper.insertCategory(category);
        categoryMapper.insertCategoryPathToTree(category);
        categoryMapper.insertCategoryNodeToTree(category);
        return Message.builder().message("插入成功").build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message deleteCategory(int id) {
        List<Integer> nodes = categoryMapper.selectAllNode(id);
        for (int node : nodes) {
            categoryMapper.deleteNodeById(node);
        }
        for (int node : nodes) {
            categoryMapper.deleteCategoryId(node);
        }
        return Message.builder().message("删除成功").build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message updateCategory(Category category) {
        categoryMapper.updateCategoryName(category);
        return Message.builder().message("更新成功").build();
    }

    @Override
    public Message selectAllCategory() {
        List<Category> categories = categoryMapper.selectAll(1);
        if (categories != null) {
            return Message.builder()
                    .message("查询成功")
                    .data(categories)
                    .build();
        }
        return Message.builder()
                .message("查询成功")
                .data("[]")
                .build();
    }

    @Override
    public Message selectAllCategorySimple() {
        return selectAllCategory();
    }
}

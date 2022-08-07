package com.lln.agriculture.manage.mapper.item;

import com.lln.agriculture.common.domain.item.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    int insertCategory(Category category);


    /**
     * 将路径插入到树中
     *
     * @param category 分类
     * @return 受影响行数
     */
    int insertCategoryPathToTree(Category category);

    /**
     * 将节点插入到树中
     *
     * @param category 分类
     * @return 受影响行数
     */
    int insertCategoryNodeToTree(Category category);


    /**
     * 查询全部
     *
     * @param parentId 父节点id
     * @return 结果
     */
    List<Category> selectAll(int parentId);

    int updateCategoryName(Category category);

    List<Integer> selectAllNode(int parentId);

    int deleteNodeById(int id);

    int deleteCategoryId(int id);
}
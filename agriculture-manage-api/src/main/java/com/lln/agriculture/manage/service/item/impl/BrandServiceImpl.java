package com.lln.agriculture.manage.service.item.impl;

import com.github.pagehelper.PageInfo;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.common.domain.item.Brand;
import com.lln.agriculture.manage.mapper.item.BrandMapper;
import com.lln.agriculture.manage.service.item.BrandService;
import com.lln.agriculture.common.util.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/4 15:26
 */
@Service
public class BrandServiceImpl implements BrandService {

    private final BrandMapper brandMapper;

    public BrandServiceImpl(BrandMapper brandMapper) {
        this.brandMapper = brandMapper;
    }


    @Override
    public Message selectAllBrand(PageRequest pageRequest) {
        PageUtils.startPage(pageRequest);
        return Message.builder()
                .message("查询成功")
                .data(PageInfo.of(brandMapper.selectAllPage(pageRequest.getKey())))
                .build();
    }

    @Override
    public Message selectAllBrandSimple(PageRequest pageRequest) {
        return selectAllBrand(pageRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message updateBrand(Brand brand) {
        brandMapper.updateByIdSelective(brand);
        return Message.builder()
                .message("更新成功")
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message deleteBrand(int id) {
        brandMapper.deleteById(id);
        return Message.builder()
                .message("删除成功")
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message insertBrand(Brand brand) {
        brandMapper.insert(brand);
        return Message.builder()
                .message("插入成功")
                .build();
    }
}

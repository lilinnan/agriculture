package com.lln.agriculture.manage.controller.item;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.common.domain.item.Brand;
import com.lln.agriculture.manage.service.item.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/4 15:37
 */

@RestController
@RequestMapping("/v1/item/brand")
public class BrandController {


    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('BRAND_SELECT')")
    public Message selectAll(PageRequest pageRequest) {
        return brandService.selectAllBrand(pageRequest);
    }

    @GetMapping("/simple")
    @PreAuthorize("hasAnyAuthority('SPU_INSERT','SPU_UPDATE')")
    public Message selectAllSimple(PageRequest pageRequest) {
        return brandService.selectAllBrandSimple(pageRequest);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('BRAND_INSERT')")
    public Message insert(@RequestBody Brand brand) {
        return brandService.insertBrand(brand);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('BRAND_UPDATE')")
    public Message update(@RequestBody Brand brand) {
        return brandService.updateBrand(brand);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('BRAND_DELETE')")
    public Message delete(@PathVariable int id) {
        return brandService.deleteBrand(id);
    }
}

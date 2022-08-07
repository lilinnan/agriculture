package com.lln.agriculture.manage.controller.item;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.item.Category;
import com.lln.agriculture.manage.service.item.CategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/3 19:02
 */

@RestController
@RequestMapping("/v1/item/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CATEGORY_SELECT')")
    public Message getAllCategory() {
        return categoryService.selectAllCategory();
    }

    @GetMapping("/simple")
    @PreAuthorize("hasAnyAuthority('SPU_INSERT','SPU_UPDATE')")
    public Message getAllCategorySimple() {
        return categoryService.selectAllCategorySimple();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CATEGORY_INSERT')")
    public Message insertCategory(@RequestBody Category category) {
        return categoryService.insertCategory(category);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('CATEGORY_UPDATE')")
    public Message update(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    public Message delete(@PathVariable int id) {
        return categoryService.deleteCategory(id);
    }
}

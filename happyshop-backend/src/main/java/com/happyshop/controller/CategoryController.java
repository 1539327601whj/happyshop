package com.happyshop.controller;

import com.happyshop.common.Result;
import com.happyshop.entity.Category;
import com.happyshop.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Result<List<Category>> getAll() {
        return Result.success(categoryService.getAllCategories());
    }

    @GetMapping("/tree")
    public Result<List<Category>> getTree() {
        return Result.success(categoryService.getCategoryTree());
    }

    @PostMapping
    public Result<?> add(@RequestBody Category category) {
        categoryService.addCategory(category);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        categoryService.updateCategory(category);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
}

package com.happyshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.happyshop.entity.Category;
import com.happyshop.mapper.CategoryMapper;
import com.happyshop.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.selectList(
                new LambdaQueryWrapper<Category>().eq(Category::getStatus, 1).orderByAsc(Category::getSort));
    }

    @Override
    public List<Category> getCategoryTree() {
        List<Category> all = getAllCategories();
        List<Category> roots = all.stream().filter(c -> c.getParentId() == 0).collect(Collectors.toList());
        roots.forEach(root -> root.setChildren(findChildren(root.getId(), all)));
        return roots;
    }

    private List<Category> findChildren(Long parentId, List<Category> all) {
        List<Category> children = all.stream()
                .filter(c -> c.getParentId().equals(parentId))
                .collect(Collectors.toList());
        children.forEach(child -> child.setChildren(findChildren(child.getId(), all)));
        return children.isEmpty() ? null : children;
    }

    @Override
    public void addCategory(Category category) {
        categoryMapper.insert(category);
    }

    @Override
    public void updateCategory(Category category) {
        categoryMapper.updateById(category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryMapper.deleteById(id);
    }
}

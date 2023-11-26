package com.api.storemanagement.mapper;

import com.api.storemanagement.dto.CategoryDTO;
import com.api.storemanagement.entities.Category;

public class CategoryMapper {
    public static Category toEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        return category;
    }

    public static CategoryDTO toDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        return categoryDTO;
    }
}

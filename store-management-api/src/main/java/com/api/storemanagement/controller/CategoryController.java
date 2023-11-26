package com.api.storemanagement.controller;

import com.api.storemanagement.dto.CategoryDTO;
import com.api.storemanagement.entities.Category;
import com.api.storemanagement.mapper.CategoryMapper;
import com.api.storemanagement.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO category){
        Category savedCategory = categoryService.addCategory(category);
        CategoryDTO savedCategoryDTO = CategoryMapper.toDTO(savedCategory);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }
}

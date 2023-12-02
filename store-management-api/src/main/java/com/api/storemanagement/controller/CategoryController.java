package com.api.storemanagement.controller;

import com.api.storemanagement.dto.ApiResponse;
import com.api.storemanagement.dto.CategoryDTO;
import com.api.storemanagement.entities.Category;
import com.api.storemanagement.mapper.CategoryMapper;
import com.api.storemanagement.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO category) {
        Category savedCategory = categoryService.addCategory(category);
        CategoryDTO savedCategoryDTO = CategoryMapper.toDTO(savedCategory);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        Optional<CategoryDTO> categoryDTO = categoryService.getCategoryByName(name);
        return categoryDTO
                .map(dto -> new ResponseEntity<>(
                        new ApiResponse( "Category found", dto),
                        HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(
                        new ApiResponse( "Category with name " + name + " not found", null),
                        HttpStatus.NOT_FOUND));
    }
}

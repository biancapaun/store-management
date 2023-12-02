package com.api.storemanagement.service;

import com.api.storemanagement.dto.CategoryDTO;
import com.api.storemanagement.entities.Category;
import com.api.storemanagement.exceptions.CategoryAlreadyExistsException;
import com.api.storemanagement.mapper.CategoryMapper;
import com.api.storemanagement.repositories.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category addCategory(CategoryDTO categoryDTO) {
        logger.info("Attempting to add new category: {}", categoryDTO.getName());

        var existingCategory = categoryRepository.findByName(categoryDTO.getName());
        existingCategory.ifPresent(c -> {
            throw new CategoryAlreadyExistsException("Category with name " + categoryDTO.getName() + " already exists");
        });

        Category category = CategoryMapper.toEntity(categoryDTO);
        return categoryRepository.save(category);
    }

    public List<CategoryDTO> getAllCategories() {
        logger.info("Fetching all categories");

        List<CategoryDTO> categories =  categoryRepository.findAll().stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());

        logger.info("Fetched {} categories", categories.size());
        return categories;
    }

    public Optional<CategoryDTO> getCategoryByName(String name) {
        logger.info("Attempting to fetch category with name: {}", name);

        Optional<CategoryDTO> categoryDTO = categoryRepository.findByName(name)
                .map(CategoryMapper::toDTO);

        if (categoryDTO.isPresent()) {
            logger.info("Category with name '{}' found", name);
        } else {
            logger.info("Category with name '{}' not found", name);
        }

        return categoryDTO;
    }


}

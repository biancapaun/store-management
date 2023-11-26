package com.api.storemanagement.services;

import com.api.storemanagement.dto.CategoryDTO;
import com.api.storemanagement.entities.Category;
import com.api.storemanagement.exceptions.CategoryAlreadyExistsException;
import com.api.storemanagement.repositories.CategoryRepository;
import com.api.storemanagement.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addCategory_ifCategoryDoesNotExist_shouldSaveNewCategory() {
        CategoryDTO newCategoryDTO = new CategoryDTO("Electronics", "Electronic gadgets");
        Category newCategory = new Category("Electronics", "Electronic gadgets");

        when(categoryRepository.findByName(newCategoryDTO.getName())).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);

        Category savedCategory = categoryService.addCategory(newCategoryDTO);

        assertNotNull(savedCategory, "The saved category should not be null");
        assertEquals(newCategory.getName(), savedCategory.getName(), "The name of the saved category should match the expected name");

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void addCategory_ifCategoryExists_ShouldThrowCategoryAlreadyExistsException() {
        CategoryDTO existingCategoryDTO = new CategoryDTO("Electronics", "Electronic gadgets");
        Category existingCategory = new Category("Electronics", "Electronic gadgets");

        when(categoryRepository.findByName(existingCategoryDTO.getName())).thenReturn(Optional.of(existingCategory));

        assertThrows(CategoryAlreadyExistsException.class, () -> {
            categoryService.addCategory(existingCategoryDTO);
        }, "A CategoryAlreadyExistsException should be thrown");

        verify(categoryRepository, never()).save(any(Category.class));
    }
}

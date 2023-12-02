package com.api.storemanagement.services;

import com.api.storemanagement.dto.CategoryDTO;
import com.api.storemanagement.entities.Category;
import com.api.storemanagement.exceptions.ResourceAlreadyExistsException;
import com.api.storemanagement.mapper.CategoryMapper;
import com.api.storemanagement.repositories.CategoryRepository;
import com.api.storemanagement.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    public void addCategory_ifCategoryExists_shouldThrowCategoryAlreadyExistsException() {
        CategoryDTO existingCategoryDTO = new CategoryDTO("Electronics", "Electronic gadgets");
        Category existingCategory = new Category("Electronics", "Electronic gadgets");

        when(categoryRepository.findByName(existingCategoryDTO.getName())).thenReturn(Optional.of(existingCategory));

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            categoryService.addCategory(existingCategoryDTO);
        }, "A CategoryAlreadyExistsException should be thrown");

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void getAllCategories_whenCalled_shouldReturnListOfCategoryDTOs(){
        Category category1 = new Category("Electronics", "Electronic gadgets");
        Category category2 = new Category("Clothes", "Winter clothes");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(2, result.size());
    }
    @Test
    public void getAllCategories_whenNoCategoriesExist_shouldReturnEmptyList() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertNotNull(result, "The result should not be null");
        assertTrue(result.isEmpty(), "The result should be an empty list");
    }
    @Test
    public void getCategoryByName_whenCalled_shouldReturnCategoryDTO() {
        String categoryName = "Electronics";
        Category category = new Category("Electronics", "Electronic gadgets");
        CategoryDTO categoryDTO = new CategoryDTO("Electronics", "Electronic gadgets");

        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(category));

        try (MockedStatic<CategoryMapper> mockedStatic = Mockito.mockStatic(CategoryMapper.class)) {
            mockedStatic.when(() -> CategoryMapper.toDTO(category)).thenReturn(categoryDTO);

            Optional<CategoryDTO> result = categoryService.getCategoryByName(categoryName);

            assertTrue(result.isPresent());
            assertEquals(categoryDTO, result.get());
        }

        verify(categoryRepository).findByName(categoryName);
    }

    @Test
    public void getCategoryByName_whenCategoryNotFound_shouldReturnEmptyOptional() {
        String categoryName = "NonExistentCategory";

        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.empty());

        Optional<CategoryDTO> result = categoryService.getCategoryByName(categoryName);

        assertFalse(result.isPresent(), "Result should be empty when the category is not found");

        verify(categoryRepository).findByName(categoryName);
    }


}

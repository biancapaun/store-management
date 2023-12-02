package com.api.storemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private Integer id;
    @NotBlank(message = "Category name must not be blank")
    private String name;
    private String description;

    public CategoryDTO(String name, String description){
        this.name = name;
        this.description = description;
    }
}

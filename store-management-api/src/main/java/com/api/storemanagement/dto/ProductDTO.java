package com.api.storemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductDTO {

    @NotBlank(message = "Product name must not be blank")
    private String name;
    private String description;
    private BigDecimal rating;
    private List<PriceDTO> prices;
    private CategoryDTO category;
}

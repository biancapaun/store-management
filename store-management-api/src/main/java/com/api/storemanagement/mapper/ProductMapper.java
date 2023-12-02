package com.api.storemanagement.mapper;

import com.api.storemanagement.dto.PriceDTO;
import com.api.storemanagement.dto.ProductDTO;
import com.api.storemanagement.entities.Price;
import com.api.storemanagement.entities.Product;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductMapper {

    public static Product toEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setRating(productDTO.getRating());

        if (productDTO.getCategory() != null && productDTO.getCategory().getId() != null) {
            product.setCategory(CategoryMapper.toEntity(productDTO.getCategory()));
        }

        if (productDTO.getPrices() != null) {
            Set<Price> prices = productDTO.getPrices().stream()
                    .map(PriceMapper::toEntity)
                    .collect(Collectors.toSet());
            product.setPrices(prices);
        }

        return product;
    }

    public static ProductDTO toDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setRating(product.getRating());

        if (product.getCategory() != null) {
            productDTO.setCategory(CategoryMapper.toDTO(product.getCategory()));
        }

        if (product.getPrices() != null) {
            List<PriceDTO> priceDTOs = product.getPrices().stream()
                    .map(PriceMapper::toDTO)
                    .collect(Collectors.toList());
            productDTO.setPrices(priceDTOs);
        }

        return productDTO;
    }
}

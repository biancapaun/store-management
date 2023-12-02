package com.api.storemanagement.controller;

import com.api.storemanagement.dto.ApiResponse;
import com.api.storemanagement.dto.ProductDTO;
import com.api.storemanagement.entities.Product;
import com.api.storemanagement.mapper.ProductMapper;
import com.api.storemanagement.service.ProductService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {
        Product addedProduct = productService.addProduct(productDTO);
        ProductDTO addedProductDTO = ProductMapper.toDTO(addedProduct);
        return new ResponseEntity<>(addedProductDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}/prices/{priceId}/make-current")
    public ResponseEntity<ApiResponse> updateCurrentPrice(@PathVariable Integer productId, @PathVariable Integer priceId) {
        Optional<Product> updatedProduct = productService.updateCurrentPrice(productId, priceId);

        return updatedProduct
                .map(product -> new ResponseEntity<>(
                        new ApiResponse("Price updated to current successfully", null),
                        HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(
                        new ApiResponse("Product or Price not found", null),
                        HttpStatus.NOT_FOUND));
    }


}

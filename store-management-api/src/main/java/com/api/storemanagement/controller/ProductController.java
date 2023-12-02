package com.api.storemanagement.controller;

import com.api.storemanagement.dto.ProductDTO;
import com.api.storemanagement.entities.Product;
import com.api.storemanagement.mapper.ProductMapper;
import com.api.storemanagement.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

package com.api.storemanagement.service;

import com.api.storemanagement.dto.ProductDTO;
import com.api.storemanagement.entities.Category;
import com.api.storemanagement.entities.Price;
import com.api.storemanagement.entities.Product;
import com.api.storemanagement.exceptions.ResourceAlreadyExistsException;
import com.api.storemanagement.mapper.PriceMapper;
import com.api.storemanagement.mapper.ProductMapper;
import com.api.storemanagement.repositories.CategoryRepository;
import com.api.storemanagement.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Product addProduct(ProductDTO productDTO) {
        logger.info("Attempting to add new product: {}", productDTO.getName());
        checkIfProductExists(productDTO.getName());
        Category category = fetchCategory(productDTO.getCategory().getId());

        Product product = ProductMapper.toEntity(productDTO);
        product.setCategory(category);
        setProductPrices(productDTO, product);

        return productRepository.save(product);
    }

    private void checkIfProductExists(String productName) {
        productRepository.findByName(productName).ifPresent(p -> {
            throw new ResourceAlreadyExistsException("Product with name " + productName + " already exists");
        });
    }

    private Category fetchCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    private void setProductPrices(ProductDTO productDTO, Product product) {
        if (productDTO.getPrices() != null && !productDTO.getPrices().isEmpty()) {
            Set<Price> prices = productDTO.getPrices().stream()
                    .map(PriceMapper::toEntity)
                    .peek(price -> price.setProduct(product))
                    .collect(Collectors.toSet());
            product.setPrices(prices);
        }
    }

    @Transactional
    public Optional<Product> updateCurrentPrice(Integer productId, Integer priceId) {
        logger.info("Attempting to update current price for product ID: {}, price ID: {}", productId, priceId);

        Product product = findProduct(productId);
        Price newCurrentPrice = findPriceInProduct(product, priceId);

        closeCurrentPricePeriod(product);
        setNewCurrentPrice(newCurrentPrice);

        Product updatedProduct = productRepository.save(product);
        logger.info("Updated current price for Product ID: {}", productId);
        return Optional.of(updatedProduct);
    }

    private Product findProduct(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> {
                    logger.warn("Product not found for ID: {}", productId);
                    throw new EntityNotFoundException("Product not found");
                });
    }

    private Price findPriceInProduct(Product product, Integer priceId) {
        return product.getPrices().stream()
                .filter(price -> price.getId().equals(priceId))
                .findFirst()
                .orElseThrow(() -> {
                    logger.warn("Price not found for ID: {} in Product ID: {}", priceId, product.getId());
                    throw new EntityNotFoundException("Price not found");
                });
    }

    private void closeCurrentPricePeriod(Product product) {
        product.getPrices().stream()
                .filter(price -> price.getEndDate() == null)
                .forEach(price -> {
                    price.setEndDate(Timestamp.valueOf(LocalDateTime.now()));
                    logger.info("Closing current price period for Price ID: {}", price.getId());
                });
    }

    private void setNewCurrentPrice(Price price) {
        price.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        price.setEndDate(null);
        logger.info("Setting new current price for Price ID: {}", price.getId());
    }

    public List<ProductDTO> getAllProducts() {
        logger.info("Fetching all products");

        List<ProductDTO> products =  productRepository.findAll().stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());

        logger.info("Fetched {} products", products.size());
        return products;
    }

    public Optional<ProductDTO> getProductByName(String name) {
        logger.info("Attempting to fetch product with name: {}", name);

        Optional<ProductDTO> productDTO = productRepository.findByName(name)
                .map(ProductMapper::toDTO);

        if (productDTO.isPresent()) {
            logger.info("Product with name '{}' found", name);
        } else {
            logger.info("Product with name '{}' not found", name);
        }

        return productDTO;
    }


}

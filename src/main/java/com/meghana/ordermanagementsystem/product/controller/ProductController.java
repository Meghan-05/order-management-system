package com.meghana.ordermanagementsystem.product.controller;

import com.meghana.ordermanagementsystem.product.enums.ProductCategoryType;
import com.meghana.ordermanagementsystem.product.dto.ProductPaginationResponse;
import com.meghana.ordermanagementsystem.product.dto.ProductRequest;
import com.meghana.ordermanagementsystem.product.dto.ProductResponse;
import com.meghana.ordermanagementsystem.product.dto.UpdateStockRequest;
import com.meghana.ordermanagementsystem.product.entity.Product;
import com.meghana.ordermanagementsystem.product.mapper.ProductRequestMapper;
import com.meghana.ordermanagementsystem.product.mapper.ProductResponseMapper;
import com.meghana.ordermanagementsystem.product.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path="/api/products")
@AllArgsConstructor
@Validated
public class ProductController {
    private ProductService productService;
    private ProductRequestMapper productRequestMapper;
    private ProductResponseMapper productResponseMapper;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest request) {
        Product product = productRequestMapper.dtoToEntity(request);
        Product createdProduct = productService.createProduct(product);
        ProductResponse productResponse = productResponseMapper.entityToDto(createdProduct);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable @Positive long id) {
        Product product = productService.findProduct(id);
        ProductResponse productResponse = productResponseMapper.entityToDto(product);

        return ResponseEntity.ok(productResponse);
    }

    @GetMapping
    public ResponseEntity<ProductPaginationResponse> getAllProducts(
            // Filter parameters (all optional)
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(required = false) ProductCategoryType category,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) LocalDateTime createdFrom,
            @RequestParam(required = false) LocalDateTime createdTo,

            // Pagination & Sort parameters
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Page<Product> productPage = productService.getAllProducts(
                name, sku, minPrice, maxPrice, inStock, category, isActive, createdFrom, createdTo,
                page, size, sortBy, direction
        );

        Page<ProductResponse> productResponsesPage = productPage.map(productResponseMapper::entityToDto);
        List<ProductResponse> productResponses = productResponsesPage.getContent();
        ProductPaginationResponse paginationResponse = new ProductPaginationResponse(productResponsesPage, productResponses);

        return ResponseEntity.ok(paginationResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable @Positive long id,
                                                         @Valid @RequestBody ProductRequest request) {
        Product product = productRequestMapper.dtoToEntity(request);
        product.setId(id);

        Product updatedProduct = productService.updateProduct(product);
        ProductResponse productResponse = productResponseMapper.entityToDto(updatedProduct);

        return ResponseEntity.ok(productResponse);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Void> updateStockQuantity(@PathVariable @Positive long id,
                                                    @Valid @RequestBody UpdateStockRequest request) {
        productService.updateStockQuantity(id, request.getQuantity());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable @Positive long id) {
        productService.deleteProduct(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

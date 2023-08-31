package com.jd.dev.app.products.models.controller;

import com.jd.dev.app.commons.models.entity.Product;
import com.jd.dev.app.products.models.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<List<Product>> findAll() {

        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable() Long id) {

        if (id.equals(10L)) {
            throw new IllegalStateException();
        }

        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Product> save(@RequestBody Product product) {

        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletedById(@PathVariable("id") Long productId) {
        productService.deleteById(productId);

        return ResponseEntity.noContent().build();
    }
}

package com.jd.dev.app.products.models.service.impl;

import com.jd.dev.app.commons.models.entity.Product;
import com.jd.dev.app.products.models.repository.ProductRepository;
import com.jd.dev.app.products.models.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Value("${server.port}")
    private Integer port;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {

        return productRepository.findAll()
                .stream()
                .peek(product -> product.setPort(port))
                .collect(Collectors.toList());
    }

    @Override
    public Product findById(Long id) {
        Product product = productRepository.findById(id)
                .orElse(null);

        assert product != null;
        product.setPort(port);

        return product;
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}

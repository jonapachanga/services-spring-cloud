package com.jd.dev.app.products.models.service;


import com.jd.dev.app.commons.models.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();
    Product findById(Long id);
    Product save(Product product);
    void deleteById(Long id);
}

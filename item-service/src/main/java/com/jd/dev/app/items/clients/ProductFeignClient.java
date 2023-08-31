package com.jd.dev.app.items.clients;

import com.jd.dev.app.commons.models.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductFeignClient {

    @GetMapping("")
    List<Product> findAll();

    @GetMapping("/{id}")
    Product findById(@PathVariable Long id);
}

package com.jd.dev.app.items.models.service.impl;

import com.jd.dev.app.commons.models.entity.Product;
import com.jd.dev.app.items.models.Item;
import com.jd.dev.app.items.models.service.ItemService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final RestTemplate restTemplate;
    private final String URL_PRODUCT_SERVICE = "http://localhost:8001/products";

    public ItemServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Item> findAll() {
        List<Product> productList = Arrays.asList(
                Objects.requireNonNull(restTemplate.getForObject(this.URL_PRODUCT_SERVICE, Product[].class))
        );

        return productList.stream()
                .map(product -> new Item(product, 1))
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer qty) {
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", id.toString());
        Product product = restTemplate.getForObject(this.URL_PRODUCT_SERVICE + "/{id}", Product.class, pathVariables);

        return new Item(product, qty);
    }
}

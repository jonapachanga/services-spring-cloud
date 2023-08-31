package com.jd.dev.app.items.models.service.impl;

import com.jd.dev.app.commons.models.entity.Product;
import com.jd.dev.app.items.clients.ProductFeignClient;
import com.jd.dev.app.items.models.Item;
import com.jd.dev.app.items.models.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("feignService")
public class ItemServiceFeignImpl implements ItemService {
    private final ProductFeignClient feignClient;

    public ItemServiceFeignImpl(ProductFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    @Override
    public List<Item> findAll() {
        List<Product> productList = feignClient.findAll();

        return productList.stream()
                .map(product -> new Item(product, 1))
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer qty) {
        Product product = feignClient.findById(id);

        return new Item(product, qty);
    }
}

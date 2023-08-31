package com.jd.dev.app.items.models.service;

import com.jd.dev.app.items.models.Item;

import java.util.List;

public interface ItemService {
    List<Item> findAll();
    Item findById(Long id, Integer qty);
}

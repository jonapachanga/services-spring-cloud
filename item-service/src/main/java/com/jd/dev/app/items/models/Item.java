package com.jd.dev.app.items.models;

import com.jd.dev.app.commons.models.entity.Product;

public class Item {
    private Product product;
    private Integer qty;

    public Item() {
    }

    public Item(Product product, Integer qty) {
        this.product = product;
        this.qty = qty;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getTotal() {
        return this.product.getPrice() * qty.doubleValue();
    }
}

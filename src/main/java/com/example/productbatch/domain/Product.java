package com.example.productbatch.domain;

import lombok.Getter;

@Getter
public class Product {
    private Long id;
    private String name;
    private boolean isCanView;

    public Product(String name, boolean isCanView) {
        this.name = name;
        this.isCanView = isCanView;
    }
}

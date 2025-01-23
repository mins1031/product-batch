package com.example.productbatch.domain;

import lombok.Getter;

@Getter
public class Product {
    private Long id;
    private String name;
    private boolean isCanView;
    private double totalStartPoint;

    public Product(String name, boolean isCanView, double totalStartPoint) {
        this.name = name;
        this.isCanView = isCanView;
        this.totalStartPoint = totalStartPoint;
    }
}

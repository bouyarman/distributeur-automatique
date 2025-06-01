package com.example.distAuto.DTO;

import java.math.BigDecimal;

public class ProductPriceRequest {
    private BigDecimal productPrice;
    private int productId;

    // Getters and Setters
    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}


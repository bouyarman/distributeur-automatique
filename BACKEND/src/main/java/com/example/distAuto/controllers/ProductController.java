package com.example.distAuto.controllers;

import com.example.distAuto.DTO.ProductDTO;
import com.example.distAuto.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class ProductController {
    ProductService pService;

    @Autowired
    public ProductController(ProductService pService) {
        this.pService = pService;
    }

    @GetMapping("/products")
    public List<ProductDTO> getProducts() {
        return pService.getAllProducts();
    }

    @PostMapping("/add-to-cart")
    public BigDecimal addToCart(@RequestBody Long productId) {
        return pService.addToCart(productId);
    }
}

package com.example.distAuto.controllers;

import com.example.distAuto.DTO.ProductDTO;
import com.example.distAuto.services.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class CartController {
    CartItemService cIService;
    @Autowired
    public CartController(CartItemService cIService) {
        this.cIService = cIService;

    }

    @PostMapping("/remove-all-cart-items")
    public ResponseEntity<String> removeAllCartItems() {
        cIService.RemoveAllItems();
        return ResponseEntity.ok("Remove all cart items");
    }


    @PostMapping("/subtract-quantity")
    public void subtractQuantity(@RequestBody int productId) {
        cIService.subtractQuantity(productId);
    }

    // List to show after clicking on mon panier
    @GetMapping("/list-added-to-cart")
    public List<ProductDTO> addedToCartProducts() {
        return cIService.addedToCartProducts();
    }
}

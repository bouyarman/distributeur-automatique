package com.example.distAuto.controllers;

import com.example.distAuto.DTO.ProductPriceRequest;
import com.example.distAuto.services.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class BalanceController {
    BalanceService bService;

    @Autowired
    public BalanceController(BalanceService bService) {

        this.bService = bService;
    }

    // Insert to balance
    @PostMapping("/insert")
    public BigDecimal insert(@RequestBody BigDecimal coinEntered) {
        return bService.insert(coinEntered);
    }

    @PostMapping("/extract")
    public BigDecimal extract(@RequestBody BigDecimal coinEntered ) {
        return bService.extract(coinEntered);
    }

    @GetMapping("/reset-balance")
    public BigDecimal resetBalance() {
        return bService.resetBalance();
    }

    @GetMapping("/balance")
    public BigDecimal getBalance() {
        return bService.getCurrentBalance();
    }
    @GetMapping("/return-exchange")
    public String exchangeReturn() {
        return bService.exchangeReturn();
    }

    @PostMapping("/add-product-price")
    public BigDecimal addProductPrice(@RequestBody ProductPriceRequest request) {
        return bService.addProductPrice(request.getProductPrice(), request.getProductId());
    }


}

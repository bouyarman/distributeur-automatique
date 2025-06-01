package com.example.distAuto.services;

import com.example.distAuto.DTO.ProductDTO;
import com.example.distAuto.models.CartItem;
import com.example.distAuto.models.Product;
import com.example.distAuto.repositories.CartItemRepository;
import com.example.distAuto.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    ProductRepository pRepository;
    CartItemRepository cIRepository;
    BalanceService balanceService;

    @Autowired
    public ProductService(ProductRepository pRepository, CartItemRepository cIRepository, BalanceService balanceService) {

        this.pRepository = pRepository;
        this.cIRepository = cIRepository;
        this.balanceService = balanceService;

    }
    // get all products and map it to productDTO
    public List<ProductDTO> getAllProducts() {
        List<Product> products = pRepository.findAll();
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setPrice(product.getPrice());
            productDTO.setPurchasable(balanceService.getCurrentBalance().compareTo(product.getPrice()) > 0 || balanceService.getCurrentBalance().compareTo(product.getPrice()) == 0);
            productDTOs.add(productDTO);

        }
        return productDTOs;

    }

    public Product getProductById(Long id) {
        return pRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }


 // add to items to purchase by incrementing the quantity and for the substraction from balance and increment quantity by 1
    public BigDecimal addToCart(Long productId) {

        Optional<CartItem> existingItem = cIRepository.findByProductId(productId.intValue());

        Product product = pRepository.findById(productId).get();
        balanceService.extractProductPrice(product.getPrice());
        BigDecimal balance = balanceService.getCurrentBalance();

        CartItem cartItem;

        if (existingItem.isPresent()) {
            // Product already in cart → increment quantity
            cartItem = existingItem.get();
            cartItem.incrementQuantity();

        }else if(balance.compareTo(product.getPrice())<0){
            throw new IllegalArgumentException("Not enough balance");

        } else {
            // Product not in cart → add new item with quantity = 1
            cartItem = new CartItem();
            cartItem.setProductId(productId.intValue());
            cartItem.setQuantity(1);
        }

        cIRepository.save(cartItem);
        return balance;
    }




}

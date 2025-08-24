package com.example.distAuto.services;

import com.example.distAuto.DTO.ProductDTO;
import com.example.distAuto.models.CartItem;
import com.example.distAuto.models.Product;
import com.example.distAuto.repositories.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {
CartItemRepository cIRepository;
ProductService pService;
BalanceService bService;

@Autowired
CartItemService(CartItemRepository cIRepository, ProductService pService, BalanceService bService) {
    this.cIRepository = cIRepository;
    this.pService = pService;
    this.bService = bService;

}

// remove all the items
public void RemoveAllItems(){
    cIRepository.deleteAll();
}

// substract 1 from quantity in each decreament of quantity
public void subtractQuantity(int productId){
        Optional<CartItem> item = cIRepository.findByProductId(productId);

        Product product = pService.getProductById((long) productId);
        bService.insert(product.getPrice());
        //bService.extract(product.getPrice());
        if(item.isPresent() && item.get().getQuantity() > 0){
            item.get().decreamenteQuantity();
            cIRepository.save(item.get());
        }
    }
    // this will get the products id and the quantity of each in this form {productId: 1, quantioty: 3}
    public List<CartItem> getItemsWithProductIdsAndQuantities() {
        return cIRepository.findAll();
    }
// this will get for real product(based on the {productId: 1, quantioty: 3} ) object and affect the quantity to productDTO
public List<ProductDTO> addedToCartProducts() {
    // list of products + their quantities and created at
        List<ProductDTO> addedProducts = new ArrayList<>();
        List<CartItem> items = this.getItemsWithProductIdsAndQuantities();
        // here we do the mapping product to productDTO
        for (CartItem item : items) {
            Product product = pService.getProductById((long) item.getProductId());

            ProductDTO newProduct = new ProductDTO();
            newProduct.setId(product.getId());
            newProduct.setName(product.getName());
            newProduct.setPrice(product.getPrice());
            newProduct.setCreatedAt(item.getCreatedAt());
            newProduct.setQuantity(item.getQuantity());
            //newProduct.setPurchasable(bService.getCurrentBalance().compareTo(product.getPrice()) > 0);
            addedProducts.add(newProduct);

        }
    addedProducts.sort(Comparator.comparing(ProductDTO::getCreatedAt));

    return addedProducts;
    }
}

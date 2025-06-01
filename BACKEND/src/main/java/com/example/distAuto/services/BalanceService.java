package com.example.distAuto.services;

import com.example.distAuto.models.CartItem;
import com.example.distAuto.repositories.CartItemRepository;
import com.example.distAuto.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BalanceService {
    ProductRepository productRepository;
    CartItemRepository cartItemRepository;

    @Autowired
    BalanceService(ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    private BigDecimal balance = BigDecimal.ZERO;

    List<BigDecimal> validCoins = List.of(
            new BigDecimal("0.25"),
            new BigDecimal("0.5"),
            new BigDecimal("1"),
            new BigDecimal("2"),
            new BigDecimal("5"),
            new BigDecimal("10")
    );
    // isnert an amount to the balance
    public BigDecimal insert(BigDecimal coinEntered) {
        //Check if the value entered is in the validcoins and grater than 0
        if(validCoins.contains(coinEntered) && coinEntered.compareTo(BigDecimal.ZERO) > 0) {
            balance = balance.add(coinEntered); // update the balance
        }
        return balance;
    }

    public BigDecimal getCurrentBalance() {
        return balance;
    }
    // extract product price to the balance after substract method is called per example
    public BigDecimal extract(BigDecimal coinEntered ) {
        if(validCoins.contains(coinEntered) && coinEntered.compareTo(BigDecimal.ZERO) > 0) {
            if(balance.subtract(coinEntered).compareTo(BigDecimal.ZERO) == 0 || balance.subtract(coinEntered).compareTo(BigDecimal.ZERO) > 0 ){
                balance = balance.subtract(coinEntered); // update the balance
            } else {
                throw new IllegalArgumentException("Insufficient balance.");

            }
        }else {
            throw new IllegalArgumentException("Invalid coin or coin must be greater than zero.");
        }
        return balance;
    }

    // extract product price to the balance after a increasing in quantity or a buy is called per example
    // this is different from extract method just by the coinsValid this accept all the positive coins but extract only extract if the coin is in valid coins
    public BigDecimal extractProductPrice(BigDecimal productPrice ) {
        if(productPrice.compareTo(BigDecimal.ZERO) > 0) {
            // check if the balance after substraction will be greater ior equalm 0
            if(balance.subtract(productPrice).compareTo(BigDecimal.ZERO) == 0 || balance.subtract(productPrice).compareTo(BigDecimal.ZERO) > 0 ){
                balance = balance.subtract(productPrice); // update the balance
            }else {
                throw new IllegalArgumentException("Insufficient balance.");
            }

        }else {
            throw new IllegalArgumentException("Product price is less than zero. (impossible)");
        }
        return balance;
    }
    // add product price to the balance after a decreament in quantity is called per example
    public BigDecimal addProductPrice(BigDecimal productPrice, int productId ) {
        // find the product item using product id
        CartItem product = cartItemRepository.findByProductId(productId).get();

        // check if product price is greater than 0 which is always correct and quantity >= 1 if quanity == 0 stop
        if(productPrice.compareTo(BigDecimal.ZERO) > 0 && product.getQuantity() == 1   ) {
                balance = balance.add(productPrice); // update the balance
        }else if (productPrice.compareTo(BigDecimal.ZERO) > 0 && product.getQuantity() > 1  ) {
            balance = balance.add(productPrice); // update the balance

        }else {
            throw new IllegalArgumentException("Quantity is 0");
        }
        return balance;
    }

    //set balance to 0
    public BigDecimal resetBalance() {
        balance = BigDecimal.ZERO;
        return balance;
    }

    // for the exchange return changes
    public String exchangeReturn() {
        // get the current balance
        BigDecimal currentBalance = this.getCurrentBalance();
        String resultAsString = "";
        BigDecimal multiple;
        // sorting the valid coins descending order
        List<BigDecimal> validCoinsSorted = validCoins.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        for (BigDecimal coin : validCoinsSorted) {
            if (currentBalance.compareTo(coin) >= 0) {
                // like 10/3 multiple will take 3
                multiple = currentBalance.divide(coin, 0, RoundingMode.DOWN);
                resultAsString = resultAsString.concat(multiple + "x " + coin + "MAD\n");
                // like 10/3 the new current balance will be 10-(3*3)=1
                currentBalance = currentBalance.subtract(coin.multiply(multiple));
            }
        }

        //System.out.println(resultAsString);
        return resultAsString;
    }


}

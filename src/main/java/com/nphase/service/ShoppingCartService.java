package com.nphase.service;

import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Properties;

public class ShoppingCartService {
    private final int MIN_DISCOUNT_QUANTITY;
    private final double DISCOUNT_PERCENTAGE;

    public ShoppingCartService() {
        try (InputStream propertiesPath = new FileInputStream("src/main/resources/config.properties")) {
            Properties discountProperties = new Properties();
            discountProperties.load(propertiesPath);
            //Also we can do null protection here, but I would rather prefer use Injecting Properties with Spring
            MIN_DISCOUNT_QUANTITY = Integer.parseInt(discountProperties.getProperty("discount.min.quantity"));
            DISCOUNT_PERCENTAGE = Double.parseDouble(discountProperties.getProperty("discount.percentage"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Task 1
    public BigDecimal calculateTotalPriceTask1(ShoppingCart shoppingCart) {
        return shoppingCart.getProducts()
                .stream()
                .map(product -> product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    //Task 2
    public BigDecimal calculateTotalPriceTask2(ShoppingCart shoppingCart) {
        return shoppingCart.getProducts()
                .stream()
                .map(this::calculateProductPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    //Task 3
    public BigDecimal calculateTotalPriceTask3(ShoppingCart shoppingCart) {
        return shoppingCart.getProducts()
                .stream().collect(Collectors.groupingBy(Product::getCategory))
                .values().stream()
                .map(this::calculateProductsByCategory)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

    }

    public BigDecimal calculateProductsByCategory(List<Product> productsByCategory) {
        BigDecimal calculatedPrice = productsByCategory.stream()
                .map(this::calculateProductPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        Integer overallCategoryQuantity = productsByCategory.stream().map(Product::getQuantity).reduce(0, Integer::sum);
        return calculateWithPossibleDiscount(overallCategoryQuantity, calculatedPrice);
    }

    private BigDecimal calculateWithPossibleDiscount(int product, BigDecimal calculatedPrice) {
        return product > MIN_DISCOUNT_QUANTITY ? calculatedPrice.subtract(calculatedPrice.multiply(BigDecimal.valueOf(DISCOUNT_PERCENTAGE))) : calculatedPrice;
    }

    public BigDecimal calculateProductPrice(Product product) {
        BigDecimal calculatedPrice = product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity()));
        return calculateWithPossibleDiscount(product.getQuantity(), calculatedPrice);
    }


}

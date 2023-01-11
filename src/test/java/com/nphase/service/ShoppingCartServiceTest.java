package com.nphase.service;


import com.nphase.entity.Category;
import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

public class ShoppingCartServiceTest {
    private final ShoppingCartService service = new ShoppingCartService();

    @Test
    public void calculatesPrice() {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 2, Category.DRINKS),
                new Product("Coffee", BigDecimal.valueOf(6.5), 1, Category.DRINKS)
        ));

        BigDecimal result = service.calculateTotalPriceTask1(cart);
        Assertions.assertEquals(0, result.compareTo(BigDecimal.valueOf(16.5)));
    }

    @Test
    public void calculatesPriceWithDiscount() {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 5, Category.DRINKS),
                new Product("Coffee", BigDecimal.valueOf(3.5), 3, Category.DRINKS)
        ));

        BigDecimal result = service.calculateTotalPriceTask2(cart);
        Assertions.assertEquals(0, result.compareTo(BigDecimal.valueOf(33.00)));
    }

    @Test
    public void calculatesPriceWith3Categories() {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.3), 2, Category.DRINKS),
                new Product("Coffee", BigDecimal.valueOf(3.5), 2, Category.DRINKS),
                new Product("Cheese", BigDecimal.valueOf(8), 2, Category.FOOD)
        ));

        BigDecimal result = service.calculateTotalPriceTask3(cart);
        Assertions.assertEquals(0, result.compareTo(BigDecimal.valueOf(31.84)));
    }

}
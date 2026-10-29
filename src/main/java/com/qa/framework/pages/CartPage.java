package com.qa.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/** Page Object: shopping cart. */
public class CartPage extends BasePage {

    private final By itemNames      = By.cssSelector("[data-test='inventory-item-name']");
    private final By checkoutButton = By.cssSelector("[data-test='checkout']");

    public CartPage() {
        waitForUrlContains("cart.html");
    }

    public List<String> getItemNames() {
        return waitForAllVisible(itemNames).stream().map(WebElement::getText).map(String::trim).toList();
    }

    public CheckoutInfoPage proceedToCheckout() {
        click(checkoutButton);
        return new CheckoutInfoPage();
    }
}

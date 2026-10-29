package com.qa.framework.pages;

import org.openqa.selenium.By;

/** Page Object: product listing (inventory) shown after login. */
public class ProductsPage extends BasePage {

    private final By pageTitle = By.cssSelector("[data-test='title']");
    private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By cartLink  = By.cssSelector("[data-test='shopping-cart-link']");

    public ProductsPage() {
        waitForUrlContains("inventory.html");
    }

    public String getTitle() {
        return getText(pageTitle);
    }

    /** "Sauce Labs Bolt T-Shirt" -> [data-test='add-to-cart-sauce-labs-bolt-t-shirt'] */
    public ProductsPage addToCart(String productName) {
        click(By.cssSelector("[data-test='add-to-cart-" + toSlug(productName) + "']"));
        return this;
    }

    /** Badge is absent (not just hidden) when the cart is empty — check without waiting the full timeout. */
    public int getCartCount() {
        return driver.findElements(cartBadge).isEmpty() ? 0 : Integer.parseInt(getText(cartBadge));
    }

    public CartPage openCart() {
        click(cartLink);
        return new CartPage();
    }

    /** Mirrors the app's own rule: lowercase, whitespace -> hyphen, punctuation kept. */
    private static String toSlug(String productName) {
        return productName.trim().toLowerCase().replaceAll("\\s+", "-");
    }
}

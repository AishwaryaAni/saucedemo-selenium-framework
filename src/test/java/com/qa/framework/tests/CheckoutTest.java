package com.qa.framework.tests;

import com.qa.framework.base.BaseTest;
import com.qa.framework.config.ConfigReader;
import com.qa.framework.pages.CartPage;
import com.qa.framework.pages.CheckoutCompletePage;
import com.qa.framework.pages.CheckoutOverviewPage;
import com.qa.framework.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

/**
 * End-to-end purchase journey: Login → Add to cart → Cart → Checkout → Confirmation.
 * Each Assert maps to a "Verify" step you would write in a manual test case.
 */
public class CheckoutTest extends BaseTest {

    private static final List<String> PRODUCTS = List.of("Sauce Labs Backpack", "Sauce Labs Bolt T-Shirt");

    @Test(groups = {"smoke", "regression"},
          description = "Standard user can buy two products and sees a correct price summary")
    public void userCanCompletePurchase() {
        // Step 1: Login
        ProductsPage products = openApp()
                .loginAs(ConfigReader.get("standard.user"), ConfigReader.get("password"));

        // Step 2: Add items — verify cart badge updates
        PRODUCTS.forEach(products::addToCart);
        Assert.assertEquals(products.getCartCount(), PRODUCTS.size(), "Cart badge count");

        // Step 3: Cart — verify exactly the chosen items are present
        CartPage cart = products.openCart();
        Assert.assertEquals(cart.getItemNames(), PRODUCTS, "Items in cart");

        // Step 4: Checkout information
        CheckoutOverviewPage overview = cart.proceedToCheckout()
                .enterDetailsAndContinue("Aishwarya", "Anil", "411001");

        // Step 5: Price summary — soft asserts report every mismatch, not just the first
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(overview.getSubtotal(), overview.sumOfItemPrices(),
                "Subtotal equals sum of line items");
        soft.assertEquals(overview.getTotal(), overview.getSubtotal().add(overview.getTax()),
                "Total equals subtotal + tax");
        soft.assertAll();

        // Step 6: Place order — verify confirmation
        CheckoutCompletePage complete = overview.finishOrder();
        Assert.assertEquals(complete.getConfirmationMessage(), "Thank you for your order!", "Confirmation header");
    }
}

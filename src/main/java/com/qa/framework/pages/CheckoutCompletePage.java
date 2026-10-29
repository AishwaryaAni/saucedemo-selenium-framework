package com.qa.framework.pages;

import org.openqa.selenium.By;

/** Page Object: order confirmation. */
public class CheckoutCompletePage extends BasePage {

    private final By confirmationHeader = By.cssSelector("[data-test='complete-header']");

    public CheckoutCompletePage() {
        waitForUrlContains("checkout-complete.html");
    }

    public String getConfirmationMessage() {
        return getText(confirmationHeader);
    }
}

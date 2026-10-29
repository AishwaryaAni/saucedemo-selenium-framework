package com.qa.framework.pages;

import org.openqa.selenium.By;

/** Page Object: Checkout step one — customer information form. */
public class CheckoutInfoPage extends BasePage {

    private final By firstNameInput  = By.id("first-name");
    private final By lastNameInput   = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton  = By.cssSelector("[data-test='continue']");

    public CheckoutInfoPage() {
        waitForUrlContains("checkout-step-one.html");
    }

    public CheckoutOverviewPage enterDetailsAndContinue(String firstName, String lastName, String postalCode) {
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(postalCodeInput, postalCode);
        click(continueButton);
        return new CheckoutOverviewPage();
    }
}

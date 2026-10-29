package com.qa.framework.pages;

import com.qa.framework.config.ConfigReader;
import org.openqa.selenium.By;

/**
 * Page Object: SauceDemo login screen.
 * Locators are private; tests only see intent-revealing actions.
 */
public class LoginPage extends BasePage {

    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton   = By.id("login-button");
    private final By errorMessage  = By.cssSelector("[data-test='error']");

    public LoginPage open() {
        driver.get(ConfigReader.get("baseUrl"));
        waitForVisible(loginButton); // page is "ready" only when login button renders
        return this;
    }

    /** Happy path: returns the next page so tests read as a fluent user journey. */
    public ProductsPage loginAs(String username, String password) {
        submitCredentials(username, password);
        return new ProductsPage();
    }

    /** Negative path: stay on login page so the error can be asserted. */
    public LoginPage loginExpectingError(String username, String password) {
        submitCredentials(username, password);
        return this;
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    private void submitCredentials(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
    }
}

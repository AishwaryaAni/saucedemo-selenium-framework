package com.qa.framework.tests;

import com.qa.framework.base.BaseTest;
import com.qa.framework.config.ConfigReader;
import com.qa.framework.pages.LoginPage;
import com.qa.framework.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(groups = {"smoke", "regression"},
          description = "Valid user lands on the Products page")
    public void validUserCanLogin() {
        ProductsPage products = openApp()
                .loginAs(ConfigReader.get("standard.user"), ConfigReader.get("password"));

        Assert.assertTrue(products.getCurrentUrl().contains("inventory.html"), "Should redirect to inventory");
        Assert.assertEquals(products.getTitle(), "Products", "Page title after login");
    }

    /**
     * Negative cases as data — same steps as a manual test case, different inputs.
     * Columns: username | password | expected error message
     */
    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        return new Object[][]{
                {"locked_out_user", "secret_sauce", "Epic sadface: Sorry, this user has been locked out."},
                {"standard_user",   "wrong_pass",   "Epic sadface: Username and password do not match any user in this service"},
                {"",                "secret_sauce", "Epic sadface: Username is required"},
                {"standard_user",   "",             "Epic sadface: Password is required"},
        };
    }

    @Test(dataProvider = "invalidLogins", groups = {"regression"},
          description = "Invalid credentials show the correct error and keep user on login page")
    public void invalidLoginShowsError(String username, String password, String expectedError) {
        LoginPage login = openApp().loginExpectingError(username, password);

        Assert.assertTrue(login.isErrorDisplayed(), "Error banner should be visible");
        Assert.assertEquals(login.getErrorMessage(), expectedError, "Error message text");
        Assert.assertFalse(login.getCurrentUrl().contains("inventory.html"), "User must not be logged in");
    }
}

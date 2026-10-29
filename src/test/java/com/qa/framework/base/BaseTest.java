package com.qa.framework.base;

import com.qa.framework.driver.DriverFactory;
import com.qa.framework.listeners.TestListener;
import com.qa.framework.pages.LoginPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

/**
 * Every test class extends this.
 * Fresh browser per test method = no shared state, tests run in any order or in parallel.
 */
@Listeners(TestListener.class)
public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.initDriver();
    }

    /** alwaysRun ensures the browser closes even when setup or the test fails. */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    /** Entry point for every flow: open the app at the login screen. */
    protected LoginPage openApp() {
        return new LoginPage().open();
    }
}

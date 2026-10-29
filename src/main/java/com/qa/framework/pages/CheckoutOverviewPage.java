package com.qa.framework.pages;

import org.openqa.selenium.By;

import java.math.BigDecimal;

/** Page Object: Checkout step two — order summary with price breakdown. */
public class CheckoutOverviewPage extends BasePage {

    private final By itemPrices    = By.cssSelector("[data-test='inventory-item-price']");
    private final By subtotalLabel = By.cssSelector("[data-test='subtotal-label']");
    private final By taxLabel      = By.cssSelector("[data-test='tax-label']");
    private final By totalLabel    = By.cssSelector("[data-test='total-label']");
    private final By finishButton  = By.cssSelector("[data-test='finish']");

    public CheckoutOverviewPage() {
        waitForUrlContains("checkout-step-two.html");
    }

    /** Sum of each line item price, read from the page. BigDecimal avoids double rounding errors. */
    public BigDecimal sumOfItemPrices() {
        return waitForAllVisible(itemPrices).stream()
                .map(e -> parseMoney(e.getText()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getSubtotal() { return parseMoney(getText(subtotalLabel)); }
    public BigDecimal getTax()      { return parseMoney(getText(taxLabel)); }
    public BigDecimal getTotal()    { return parseMoney(getText(totalLabel)); }

    public CheckoutCompletePage finishOrder() {
        click(finishButton);
        return new CheckoutCompletePage();
    }

    /** "Item total: $39.98" -> 39.98 */
    private static BigDecimal parseMoney(String text) {
        return new BigDecimal(text.substring(text.indexOf('$') + 1).trim());
    }
}

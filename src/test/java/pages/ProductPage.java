package pages;

import org.openqa.selenium.By;
import utils.TestUtils;

/** Product Details Page (PDP) actions */
public class ProductPage extends BasePage {

    private static final By ADD_TO_CART = By.xpath("//a[normalize-space()='Add to cart']");
    private static final By PRODUCT_TITLE = By.cssSelector(".name"); // product title on PDP

    public void waitReady() {
        TestUtils.waitVisible(PRODUCT_TITLE, 10);
        TestUtils.waitVisible(ADD_TO_CART, 10);
    }

    /** Click Add to cart and wait for/accept the browser alert. Returns the alert text (or null). */
    public String addToCartAndGetAlert() {
        waitReady();
        TestUtils.clickWithFallback(ADD_TO_CART);

        // Alerts on this site can lag; wait up to 8s, then one retry
        String txt = TestUtils.acceptAlertIfPresent(8);
        if (txt == null) {
            // Passive wait small and retry once more
            try { Thread.sleep(600); } catch (InterruptedException ignored) {}
            txt = TestUtils.acceptAlertIfPresent(5);
        }
        return txt;
    }
}

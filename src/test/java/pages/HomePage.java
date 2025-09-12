package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import utils.DriverFactory;
import utils.TestUtils;

/** Site-wide header + sidebar actions for Demoblaze */
public class HomePage extends BasePage {

    // Header
    private static final By BRAND        = By.id("nava");       // "PRODUCT STORE"
    private static final By CART_LINK    = By.id("cartur");
    private static final By SIGNUP_LINK  = By.id("signin2");
    private static final By LOGIN_LINK   = By.id("login2");
    private static final By NAME_OF_USER = By.id("nameofuser"); // "Welcome <user>"

    // Sidebar category link by its label (e.g., "Laptops")
    private By categoryLink(String name) {
        return By.xpath("//a[@id='itemc' and normalize-space()='" + name + "']");
    }

    /* ===== Modals ===== */

    /** Opens the Sign up modal from the header (robust even if intercepted). */
    public void openSignupModal() {
        By link = SIGNUP_LINK;
        TestUtils.waitVisible(link, 10);
        try {
            TestUtils.clickWithFallback(link);
        } catch (Exception ignore) {
            var el = TestUtils.find(link);
            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript("arguments[0].click();", el);
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        }
    }

    /** Opens the Log in modal from the header (robust even if intercepted). */
    public void openLoginModal() {
        By link = LOGIN_LINK;
        TestUtils.waitVisible(link, 10);
        try {
            TestUtils.clickWithFallback(link);
        } catch (Exception ignore) {
            var el = TestUtils.find(link);
            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript("arguments[0].click();", el);
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        }
    }

    /** Returns true if the header says: Welcome <username>. */
    public boolean isLoggedInAs(String username) {
        // Wait for the welcome badge to become visible and non-empty
        String txt = TestUtils.waitNonEmptyText(NAME_OF_USER, 10);
        // Expected format: "Welcome bob123"
        return txt.trim().equals("Welcome " + username);
    }

    /* ===== Category & Cart navigation ===== */

    /** Open a category via the left sidebar (e.g., "Laptops"). Resilient even when on a PDP. */
    public void openCategory(String name) {
        By link = categoryLink(name);

        // Try directly; if not visible (e.g., we're on a product page), click brand to go home and retry
        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                TestUtils.scrollIntoView(link);
                TestUtils.clickWithFallback(link);
                return;
            } catch (Exception ignored) {
                // go Home once, then retry
                try { TestUtils.clickWithFallback(BRAND); } catch (Exception e2) { /* ignore */ }
            }
        }

        // Final attempt with an explicit scroll+click
        TestUtils.scrollIntoView(link);
        TestUtils.clickWithFallback(link);
    }

    /** Opens the Cart page from the header. */
    public void openCart() {
        TestUtils.clickWithFallback(CART_LINK);
    }
}

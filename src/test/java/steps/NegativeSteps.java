package steps;

import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

public class NegativeSteps {

    private static WebDriver driver;
    private WebDriverWait wait;

    // Cache last successful signup to reuse for the “existing user” negative
    private static String lastUsername;
    private static String lastPassword;

    /* ===================== Infra ===================== */

    private WebDriver d() {
        if (driver == null) {
            driver = new ChromeDriver();
            driver.manage().window().maximize();
        }
        return driver;
    }

    private WebDriverWait w() {
        if (wait == null) wait = new WebDriverWait(d(), Duration.ofSeconds(12));
        return wait;
    }

    private JavascriptExecutor js() { return (JavascriptExecutor) d(); }

    private void nap(long ms) { try { Thread.sleep(ms); } catch (InterruptedException ignored) {} }

    /** Super-robust click: re-find, scroll, retry on stale/intercepted/timeout. */
    private void safeClick(By locator) {
        for (int i = 0; i < 6; i++) {
            try {
                WebElement el = w().until(ExpectedConditions.elementToBeClickable(locator));
                try { js().executeScript("arguments[0].scrollIntoView({block:'center',inline:'center'});", el); } catch (JavascriptException ignored) {}
                try { new Actions(d()).moveToElement(el).pause(Duration.ofMillis(60)).perform(); } catch (Exception ignored) {}
                try { el.click(); } catch (ElementClickInterceptedException e) { js().executeScript("arguments[0].click();", el); }
                return;
            } catch (StaleElementReferenceException | TimeoutException e) {
                if (i == 5) throw e;
                nap(150);
            }
        }
    }

    private void waitHomeGrid() {
        w().until(ExpectedConditions.visibilityOfElementLocated(By.id("navbarExample")));
        w().until(ExpectedConditions.visibilityOfElementLocated(By.id("tbodyid")));
        w().until(drv -> drv.findElements(By.cssSelector("#tbodyid .card a.hrefch")).size() > 0);
        nap(100);
    }

    private void goHome() {
        List<WebElement> home = d().findElements(By.linkText("Home (current)"));
        if (!home.isEmpty()) safeClick(By.linkText("Home (current)"));
        else d().get("https://www.demoblaze.com");
        waitHomeGrid();
    }

    private void waitProductReady(String name) {
        By link = By.linkText(name);
        w().until(ExpectedConditions.presenceOfElementLocated(link));
        w().until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(link)));
        nap(60);
    }

    private Alert waitForAlert() { return w().until(ExpectedConditions.alertIsPresent()); }

    private void openModal(String which) {
        String k = which.toLowerCase(Locale.ROOT);
        By trigger = k.contains("sign") ? By.id("signin2") : By.id("login2");
        safeClick(trigger);
        if (k.contains("sign")) w().until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-username")));
        else w().until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername")));
        nap(100);
    }

    private void clickModalPrimary(String which) {
        String k = which.toLowerCase(Locale.ROOT);
        By btn = k.contains("sign")
                ? By.xpath("//div[@id='signInModal']//button[text()='Sign up']")
                : By.xpath("//div[@id='logInModal']//button[text()='Log in']");
        safeClick(btn);
        nap(120);
    }

    private int readUnitPriceOnPDP() {
        String txt = w().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".price-container"))).getText();
        String digits = txt.replaceAll("[^0-9]", "");
        return digits.isEmpty() ? 0 : Integer.parseInt(digits);
    }

    /* ===================== Steps ===================== */

    @Given("I NEG open the Demoblaze home page")
    public void i_neg_open_the_demoblaze_home_page() {
        d().get("https://www.demoblaze.com");
        waitHomeGrid();
    }

    /* -------- Signup positive then negative with same user -------- */

    @When("I NEG sign up with a NEW username and password")
    public void i_neg_sign_up_with_a_new_username_and_password() {
        lastUsername = "user_" + System.currentTimeMillis();
        lastPassword = "abc123";

        openModal("Sign up");
        WebElement u = w().until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-username")));
        WebElement p = w().until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-password")));
        u.clear(); p.clear();
        u.sendKeys(lastUsername);
        nap(80);
        p.sendKeys(lastPassword);
        clickModalPrimary("Sign up");
    }

    @When("I NEG try to sign up again with the same credentials")
    public void i_neg_try_to_sign_up_again_with_the_same_credentials() {
        openModal("Sign up");
        WebElement u = w().until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-username")));
        WebElement p = w().until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-password")));
        u.clear(); p.clear();
        u.sendKeys(lastUsername);
        nap(80);
        p.sendKeys(lastPassword);
        clickModalPrimary("Sign up");
    }

    @When("I NEG sign up with username {string} and password {string}")
    public void i_neg_sign_up_with_username_and_password(String username, String password) {
        openModal("Sign up");
        WebElement u = w().until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-username")));
        WebElement p = w().until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-password")));
        u.clear(); p.clear();
        u.sendKeys(username);
        nap(80);
        p.sendKeys(password);
        clickModalPrimary("Sign up");
    }

    @Then("I NEG should see an alert containing {string}")
    public void i_neg_should_see_an_alert_containing(String expected) {
        Alert a = waitForAlert();
        String msg = a.getText();
        a.accept();
        nap(120);
        if (!msg.contains(expected)) {
            throw new AssertionError("Expected alert to contain: \"" + expected + "\" but got: \"" + msg + "\"");
        }
    }

    /* ------------------ Login negative ------------------ */

    @When("I NEG log in with username {string} and password {string}")
    public void i_neg_log_in_with_username_and_password(String username, String password) {
        openModal("Login");
        WebElement u = w().until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername")));
        WebElement p = w().until(ExpectedConditions.visibilityOfElementLocated(By.id("loginpassword")));
        u.clear(); p.clear();
        u.sendKeys(username);
        nap(80);
        p.sendKeys(password);
        clickModalPrimary("Login");
    }

    /* ------------- Add to cart & cart assertions ------------- */

    @When("I NEG add product {string} to the cart")
    public void i_neg_add_product_to_the_cart(String productName) {
        // Always reset to a stable grid
        goHome();

        // Narrow to Phones (prevents random category reflow)
        List<WebElement> phones = d().findElements(By.linkText("Phones"));
        if (!phones.isEmpty()) {
            safeClick(By.linkText("Phones"));
            w().until(dr -> dr.findElements(By.cssSelector("#tbodyid .card a.hrefch")).size() > 0);
        }

        // Click the product (fresh element)
        waitProductReady(productName);
        safeClick(By.linkText(productName));

        // PDP loaded
        w().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".name")));

        // Add + accept alert
        safeClick(By.xpath("//a[text()='Add to cart']"));
        Alert a = waitForAlert();
        a.accept();
        nap(120);

        // Back home ready for next action
        goHome();
    }

    @And("I NEG open the cart")
    public void i_neg_open_the_cart() {
        safeClick(By.id("cartur"));
        w().until(ExpectedConditions.visibilityOfElementLocated(By.id("tbodyid")));
        w().until(ExpectedConditions.visibilityOfElementLocated(By.id("totalp")));
        nap(100);
    }

    @Then("I NEG should see {int} rows for {string}")
    public void i_neg_should_see_rows_for(int expectedCount, String productName) {
        List<WebElement> rows = w().until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("#tbodyid > tr")));
        int count = 0;
        for (WebElement r : rows) if (r.getText().contains(productName)) count++;
        if (count != expectedCount) throw new AssertionError("Expected " + expectedCount + " rows for \"" + productName + "\" but saw " + count);
    }

    @Then("I NEG verify the total equals price of {string} multiplied by {int}")
    public void i_neg_verify_the_total_equals_price_multiplied_by(String productName, int qty) {
        // read unit on PDP
        goHome();
        waitProductReady(productName);
        safeClick(By.linkText(productName));
        w().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".name")));
        int unit = readUnitPriceOnPDP();

        // back to cart
        safeClick(By.id("cartur"));
        String totalTxt = w().until(ExpectedConditions.visibilityOfElementLocated(By.id("totalp"))).getText().trim();
        int total = totalTxt.isEmpty() ? 0 : Integer.parseInt(totalTxt);

        int expected = unit * qty;
        if (total != expected) {
            throw new AssertionError("Expected total " + expected + " but got " + total + " (unit " + unit + " x " + qty + ")");
        }
    }

    /* -------- Place Order (EMPTY card) — matches your Gherkin exactly -------- */

    // EXACT signature for:
    // When I NEG click Place Order with name "John Test" and EMPTY card
    @When("I NEG click Place Order with name {string} and EMPTY card")
    public void i_neg_click_place_order_with_name_and_empty_card(String name) {
        // must already be on cart page
        safeClick(By.xpath("//button[text()='Place Order']"));
        w().until(ExpectedConditions.visibilityOfElementLocated(By.id("orderModal")));
        // fill ONLY name, leave credit card empty
        WebElement nameField = w().until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        WebElement cardField = w().until(ExpectedConditions.visibilityOfElementLocated(By.id("card")));
        nameField.clear(); nameField.sendKeys(name);
        cardField.clear(); // empty
        // click Purchase; do NOT accept the alert here (Then step handles it)
        safeClick(By.xpath("//button[text()='Purchase']"));
    }

    /* -------- OPTIONAL: Overload to catch minor phrasing variations -------- */
    @When("I NEG click Place Order with EMPTY card and name {string}")
    public void i_neg_click_place_order_with_empty_card_and_name(String name) {
        i_neg_click_place_order_with_name_and_empty_card(name);
    }
}

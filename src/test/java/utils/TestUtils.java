package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class TestUtils {

    private static final ThreadLocal<String> LAST_ALERT = new ThreadLocal<>();

    /* =================== basic actions =================== */

    public static void click(By locator) {
        waitClickable(locator, 10).click();
    }

    public static void type(By locator, String text) {
        WebElement el = waitVisible(locator, 10);
        el.clear();
        el.sendKeys(text);
    }

    /* =================== waits =================== */

    public static WebElement waitVisible(By by, int seconds) {
        return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static WebElement waitClickable(By by, int seconds) {
        return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(seconds))
                .until(ExpectedConditions.elementToBeClickable(by));
    }

    /** Wait until NO element matching the locator is visible */
    public static void waitGone(By locator, int seconds) {
        new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(seconds))
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /** Wait until there are more than N elements matching locator (use 0 for “at least one”). */
    public static void waitForElementsMoreThan(By by, int moreThan, int seconds) {
        new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(seconds))
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(by, moreThan));
    }

    /** Wait until the element’s text is non-empty (after trimming). */
    public static String waitNonEmptyText(By by, int seconds) {
        return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(seconds))
                .until(driver -> {
                    String t = driver.findElement(by).getText();
                    return (t != null && !t.trim().isEmpty()) ? t : null;
                }).trim();
    }

    /* =================== find/get helpers =================== */

    public static WebElement find(By by) {
        return DriverFactory.getDriver().findElement(by);
    }

    public static List<WebElement> findAll(By by) {
        return DriverFactory.getDriver().findElements(by);
    }

    public static String getText(By by) {
        return DriverFactory.getDriver().findElement(by).getText();
    }

    /* =================== alerts =================== */

    /** Accept alert if present and remember its text. Returns the text or null. */
    public static String acceptAlertIfPresent(int seconds) {
        try {
            Alert a = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.alertIsPresent());
            String txt = a.getText();
            a.accept();
            LAST_ALERT.set(txt);
            return txt;
        } catch (TimeoutException e) {
            return null;
        }
    }

    /** Wait up to N seconds for an alert OR return the last cached alert text. */
    public static String getLastAlertOrWait(int seconds) {
        String cached = LAST_ALERT.get();
        if (cached != null) return cached;
        return acceptAlertIfPresent(seconds);
    }

    /* =================== misc =================== */

    // Scroll the element into view (useful for sidebar categories)
    public static WebElement scrollIntoView(By by) {
        WebElement el = DriverFactory.getDriver().findElement(by);
        ((JavascriptExecutor) DriverFactory.getDriver())
                .executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el);
        return el;
    }

    // Try normal click, fall back to JS click if intercepted
    public static void clickWithFallback(By by) {
        try {
            waitClickable(by, 10).click();
        } catch (ElementClickInterceptedException e) {
            WebElement el = waitVisible(by, 10);
            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript("arguments[0].click();", el);
        }

    }
    public static void shortPause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }

}

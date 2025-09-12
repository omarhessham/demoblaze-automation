package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.TestUtils;

public class CategoryPage extends BasePage {

    private static final By GRID       = By.id("tbodyid");
    private static final By PRODUCT_CARDS = By.cssSelector("#tbodyid .card");

    private By productLinkByName(String name) {
        return By.xpath("//div[@id='tbodyid']//a[contains(@class,'hrefch') and normalize-space()='" + name + "']");
    }

    public void waitGridReady() {
        TestUtils.waitVisible(GRID, 10);
        try { TestUtils.waitForElementsMoreThan(PRODUCT_CARDS, 0, 6); } catch (Exception ignored) {}
    }

    /** Opens a product’s PDP by its exact name. Robust with scroll + retry. */
    public void openProductByName(String name) {
        waitGridReady();
        By link = productLinkByName(name);

        // 2 attempts: direct, then scroll + click brand handled by HomePage logic
        for (int i = 0; i < 2; i++) {
            try {
                WebElement el = TestUtils.scrollIntoView(link);
                TestUtils.clickWithFallback(link);
                return;
            } catch (Exception ignored) {
                try { Thread.sleep(300); } catch (InterruptedException ignored2) {}
            }
        }
        // Final forced attempt
        TestUtils.scrollIntoView(link);
        TestUtils.clickWithFallback(link);
    }
}

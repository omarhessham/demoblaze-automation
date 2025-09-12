package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.TestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Cart page: read items, prices and computed total */
public class CartPage extends BasePage {

    private static final By GRID       = By.id("tbodyid");
    private static final By ROWS       = By.cssSelector("#tbodyid tr");          // one row per item
    private static final By CELL_NAME  = By.cssSelector("td:nth-child(2)");
    private static final By CELL_PRICE = By.cssSelector("td:nth-child(3)");
    private static final By TOTAL      = By.id("totalp");

    /** Wait until the cart table is visible and at least 1 row (or total text) is present. */
    public void waitReady() {
        TestUtils.waitVisible(GRID, 10);
        try {
            TestUtils.waitForElementsMoreThan(ROWS, 0, 6);
        } catch (Exception ignore) {
            try { TestUtils.waitNonEmptyText(TOTAL, 6); } catch (Exception ignored) {}
        }
    }

    public void waitForCart() { waitReady(); }

    public List<String> getItemTitles() {
        waitReady();
        List<String> out = new ArrayList<>();
        for (WebElement row : TestUtils.findAll(ROWS)) {
            out.add(row.findElement(CELL_NAME).getText().trim());
        }
        return out;
    }

    public List<Integer> getItemPrices() {
        waitReady();
        List<Integer> out = new ArrayList<>();
        for (WebElement row : TestUtils.findAll(ROWS)) {
            String t = row.findElement(CELL_PRICE).getText().trim();
            out.add(Integer.parseInt(t.replaceAll("[^0-9]", "")));
        }
        return out;
    }

    public Map<String, Integer> getItemsMap() {
        waitReady();
        Map<String, Integer> map = new HashMap<>();
        for (WebElement row : TestUtils.findAll(ROWS)) {
            String name = row.findElement(CELL_NAME).getText().trim();
            String p    = row.findElement(CELL_PRICE).getText().trim();
            int price   = Integer.parseInt(p.replaceAll("[^0-9]", ""));;
            map.put(name, price);
        }
        return map;
    }

    public int sumPrices() {
        return getItemPrices().stream().mapToInt(Integer::intValue).sum();
    }

    public int getDisplayedTotal() {
        waitReady();
        String t;
        try {
            t = TestUtils.waitNonEmptyText(TOTAL, 5);
        } catch (Exception e) {
            t = TestUtils.getText(TOTAL).trim();
        }
        return Integer.parseInt(t.replaceAll("[^0-9]", ""));
    }
}

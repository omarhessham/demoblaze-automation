package pages;

import utils.DriverFactory;

public abstract class BasePage {
    protected org.openqa.selenium.WebDriver driver() {
        return utils.DriverFactory.getDriver();
    }
}

package pages;

import org.openqa.selenium.By;
import utils.TestUtils;

public class LoginModal extends BasePage {

    private static final By MODAL     = By.id("logInModal");
    private static final By USERNAME  = By.id("loginusername");
    private static final By PASSWORD  = By.id("loginpassword");
    private static final By BTN_LOGIN = By.xpath("//div[@id='logInModal']//button[normalize-space()='Log in']");

    public void waitOpen() { TestUtils.waitVisible(MODAL, 10); }

    public void login(String user, String pass) {
        // If a previous alert is still open (from sign-up), consume it first
        TestUtils.acceptAlertIfPresent(1);

        waitOpen();
        TestUtils.type(USERNAME, user);
        TestUtils.type(PASSWORD, pass);
        TestUtils.clickWithFallback(BTN_LOGIN);

        // No alert is expected for login; just wait until modal gone / Welcome badge appears from HomePage check.
    }
}

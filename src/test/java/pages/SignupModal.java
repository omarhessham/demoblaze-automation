package pages;

import org.openqa.selenium.By;
import utils.TestUtils;

public class SignupModal extends BasePage {

    private static final By MODAL     = By.id("signInModal");
    private static final By USERNAME  = By.id("sign-username");
    private static final By PASSWORD  = By.id("sign-password");
    private static final By BTN_SIGN  = By.xpath("//div[@id='signInModal']//button[normalize-space()='Sign up']");
    private static final By BTN_CLOSE = By.xpath("//div[@id='signInModal']//button[@class='close']");

    public void waitOpen() { TestUtils.waitVisible(MODAL, 10); }

    public String[] signUpRandom() {
        waitOpen();
        String u = "user" + System.currentTimeMillis();
        String p = "P@ss" + (int)(Math.random()*10000);

        TestUtils.type(USERNAME, u);
        TestUtils.type(PASSWORD, p);
        TestUtils.clickWithFallback(BTN_SIGN);

        // Some runs show alert later; wait and accept it.
        String txt = TestUtils.acceptAlertIfPresent(8);
        if (txt == null) {
            // Try once more in case of latency
            txt = TestUtils.acceptAlertIfPresent(4);
        }
        // In case modal still visible, close it so header is clickable again.
        try { TestUtils.clickWithFallback(BTN_CLOSE); } catch (Exception ignored) {}
        return new String[]{u, p};
    }
}

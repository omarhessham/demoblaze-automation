// steps/Hooks.java
package steps;

import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import utils.TestUtils;

/** Global hooks for Cucumber */
public class Hooks {

    // Tweak this to speed up / slow down the whole run
    private static final int STEP_PAUSE_MS = 900; // ~0.9s after every step
    public static WebDriver driver;

    @AfterStep
    public void afterEachStep(Scenario scenario) {
        TestUtils.shortPause(STEP_PAUSE_MS);
    }
}

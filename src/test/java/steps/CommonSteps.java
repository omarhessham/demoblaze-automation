package steps;

import io.cucumber.java.en.Given;
import utils.DriverFactory;

public class CommonSteps {

    @Given("I open the Demoblaze home page")
    public void i_open_the_demoblaze_home_page() {
        DriverFactory.getDriver().navigate().to("https://www.demoblaze.com/");
    }
}

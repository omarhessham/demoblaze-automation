package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"steps"},
        tags = "@all",
        plugin = {
                "pretty",
                "html:target/cucumber-report.html",
                "json:target/cucumber.json",
                "junit:target/cucumber.xml"
        },
        monochrome = true
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
}


/*tags = "@two_products"/
/*
 @CucumberOptions(
        features = "src/test/resources/features",
        glue = {"steps"},
        plugin = {"pretty","html:target/cucumber-report.html"},
        monochrome = true,
        tags = "@neg"
)
 */



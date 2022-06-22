package cucumber.Options;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		stepNotifications = true,
		features = "src/test/java/features",
		monochrome = true,
		plugin = {
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "pretty",
                "json:target/cucumber-reports/",
                "html:target/cucumber-reports.html"},
		glue={"stepDefinitions"}
//		tags= "@DeleteIssue"  
		)

public class TestRunner {

}

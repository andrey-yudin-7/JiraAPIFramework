package cucumber.Options;

import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
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

public class TestRunnerTestNg {

}

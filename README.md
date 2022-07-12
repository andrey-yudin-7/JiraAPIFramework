<b><h3>[1. Project description](#description)</h3></b>

<b><h3>[2. How to run and use](#howtorun)</h3></b>

<b><h3>[3. Implemented Details](#details)</h3></b>
- [Feature file with test cases, step definition file](#feature)
- [Parametrizing with data sets, driving all global variables from global properties file](#parametrizing)
- [Reusable methods](#methods)
- [Tagging mechanism](#tagging)
- [Enum class with constants](#enum)
- [Data driven from feature files, external data files](#datadriven)
- [Extent Reports](#reports)
- [Logs](#logs)
- [OOPS concepts achieved](#oops)

<a id="description"></a>
## __1. Project description__

This Framework is a Rest API Automation project built using Java, Maven (build management tool), Cucumber, Rest Assured dependencies, Data Driven concepts from external resources, generating Logs, Reports.

As the test target of this project as an example used Atlassian Jira with Validating it's basic issue API's: the issue creating, add comment, add attachment, delete the issue.

Structure:

<img src="src\test\resources\readme_images\structure.png">

<img src="src\test\resources\readme_images\structure2.png">

In test cases used available API resources data from the official Atlassian web site:
https://developer.atlassian.com/server/jira/platform/cookie-based-authentication/
https://docs.atlassian.com/software/jira/docs/api/REST/1000.729.0/

<a id="howtorun"></a>
## __2. How to run and use__
To run this project you need to make sure you have installed Java, Eclipse or another ide, Maven with setting up Java, Maven home paths on your machine.

Also you need to setup Jira Server in Local system. You can download the latest free version from the official Atlassian web site: https://www.atlassian.com/software/jira/update. After the installation with choosing 8080 http port (default port) you can run Jira locally in web browser using http://localhost:8080/. On first start you'll need to login with your Jira account credentials or create a new account.

<img src="src\test\resources\readme_images\jira1.png">
Then you'll need to create a test project with the project Key
as 'RESAUT' as an example demonstrated in the image:
<img src="src\test\resources\readme_images\jira2.png">

you can start or stop jira server on running start_service.bat or stop_service.bat from the root directory of installed Jira.

Clone this project and import it from the file system using IDE (as Eclipse IDE).

Then you'll need to update global.properties file in the project (located in src\test\java\resources\) with your data for adminLogin, adminPassword, projectKey:
```
baseUrl=http://localhost:8080/
adminLogin=
adminPassword=
projectKey=
```

Run this project As Junit Test for junitCucumber.xml file (in root folder) or for _TestRunner.java_ (in src\test\java\cucumber\Options folder) file:

<img src="src\test\resources\readme_images\run1.png" width="500">


Or from command line using maven (project root folder).
To run all test cases:
```
andre@DESKTOP-0CPI2F2 MINGW64 /d/QA_projects/JiraAPIFramework (master)
$ mvn test -Dcucumber
```
to run Smoke test as example (Scenarios with Smoke tags):
<a id="filter"></a>
```
andre@DESKTOP-0CPI2F2 MINGW64 /d/QA_projects/JiraAPIFramework (master)
$ mvn test -Dcucumber.filter.tags="@Smoke"
```
<a id="details"></a>
## __3. Implemented Details__
<a id="feature"></a>
### __3.1 Feature file with test cases, step definition file__

BDD concepts with Cucumber framework implemented using feature files (with test cases), Step definitions files (with supported code) and TestRunner (class for running tests).

Feature File is an entry point to the Cucumber tests and cucumber proposes to write test scenario in the Given/When/Then/And format.  
Example of the feature file used in this API framework - _issueValidations.feature_ (from src\test\java\features forlder):

```
Feature: Validating Issue API's

@CreateIssue @Smoke
Scenario Outline: Verify if issue is being Succesfully created using createIssueAPI
	Given Created session with issue Payload as "<summary>" "<description>"
	When user calls "createIssueAPI" with "POST" http request
	Then the API call got success with status code "201"

Examples:
	|summary 	 		|description 			|
	|summary_test |description_test |

@AddComment @Regression
Scenario Outline: Verify add comment functionality is working
	Given Created session with issue Payload as "<summary>" "<description>"
	When user calls "createIssueAPI" with "POST" http request
	Then the API call got success with status code "201"
	Then user calls "addCommentAPI" with payload as "<comment>" and "POST" http request
	Then the API call got success with status code "201"
	
Examples:
	|summary 	 		|description 			|comment 	 		|
	|summary_test |description_test |comment_test |
	
@AddAttachment @Regression
Scenario Outline: Verify add attachment functionality is working
	Given Created session with issue Payload as "<summary>" "<description>"
	When user calls "createIssueAPI" with "POST" http request
	Then the API call got success with status code "201"
	Then user calls "addAttachmentAPI" with adding file and "POST" http request
	Then the API call got success with status code "200"
	
Examples:
	|summary 	 		|description 			|
	|summary_test |description_test |
	
@DeleteIssue @Smoke
Scenario Outline: Verify delete issue functionality is working
	Given Created session with issue Payload as "<summary>" "<description>"
	When user calls "createIssueAPI" with "POST" http request
	Then the API call got success with status code "201"
	Then user delete the issue by calling "deleteIssueAPI" with "DELETE" http request 
	Then the API call got success with status code "201"

Examples:
	|summary 	 		|description 			|
	|summary_test |description_test |
```
part of _StepDefinition.java_ (from src\test\java\stepDefinitions\ folder):

```
	.............
    @When("user calls {string} with {string} http request")
	public void user_calls_with_http_request(String resource, String method) {
		APIResources resourceAPI = APIResources.valueOf(resource);
		resourceAPI.getResource();
		resspec = new ResponseSpecBuilder().expectStatusCode(201).expectContentType(ContentType.JSON).build();
		
		if(method.equalsIgnoreCase("POST"))
		response = reqspec.when().filter(session).post(resourceAPI.getResource());
		else if(method.equalsIgnoreCase("GET"))
		response = reqspec.when().filter(session).get(resourceAPI.getResource());
	}
	@Then("the API call got success with status code {string}")
	public void the_api_call_got_success_with_status_code(String code) {
	    assertEquals(response.getStatusCode(), Integer.parseInt(code));
		//get issue id
		issueId = getJsonPath(response, "id");
	}
	@Then("user calls {string} with payload as {string} and {string} http request")
	public void user_calls_with_payload_as_and_http_request(String resource, String comment, String method) throws IOException {
		APIPathResources resourcePathAPI =  new APIPathResources();

		reqspec = given().spec(requestSpecification()).body(data.AddCommentPayload(comment)).filter(session);
		
		if(method.equalsIgnoreCase("POST"))
		response = reqspec.when().post(resourcePathAPI.getAddCommentAPI(issueId));
		else if(method.equalsIgnoreCase("GET"))
		response = reqspec.when().get(resourcePathAPI.getAddCommentAPI(issueId));
		//get comment id
		commentId = getJsonPath(response, "id");		
	}
    ............
```
<a id="parametrizing"></a>
## __3.2 Parametrizing with data sets, driving all global variables from global properties file__

Implemented parametrizing to run tests with multiple data sets using Cucumber Example Keyword:

```
@CreateIssue @Smoke
Scenario Outline: Verify if issue is being Succesfully created using createIssueAPI
	Given Created session with issue Payload as "<summary>" "<description>"
	When user calls "createIssueAPI" with "POST" http request
	Then the API call got success with status code "201"

Examples:
	|summary 	 		|description 			|
	|summary_test |description_test |
```

Implemented driving all global variables from _global.properties_ file:

```
baseUrl=http://localhost:8080/
adminLogin=
adminPassword=
projectKey=
```
supported getGlobalValue() method from _Utils.java_:
```
	public static String getGlobalValue(String key) throws IOException
	{
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") +"\\src\\test\\java\\resources\\global.properties");
		prop.load(fis);
		return prop.getProperty(key);
	}
```
<a id="methods"></a>
### __3.3 Reusable methods__

Created _Utilities.java class_ in resources folder to define all reusable requests and response specifications - as _requestSpecification()_, _getGlobalValue()_, _getJsonPath()_, _rowToJson()_:
```
public class Utils {
	
	public static RequestSpecification req;
	
	public RequestSpecification requestSpecification() throws IOException
	{
		if(req==null)
		{
		PrintStream log = new PrintStream(new FileOutputStream(System.getProperty("user.dir")+"\\logs\\logs.txt"));
		req = new RequestSpecBuilder().setBaseUri(getGlobalValue("baseUrl"))
				.setRelaxedHTTPSValidation()
				.addFilter(RequestLoggingFilter.logRequestTo(log))
				.addFilter(ResponseLoggingFilter.logResponseTo(log))
		.setContentType(ContentType.JSON).build();
		return req;
		}
		return req;
	}
	
	public static String getGlobalValue(String key) throws IOException
	{
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") +"\\src\\test\\java\\resources\\global.properties");
		prop.load(fis);
		return prop.getProperty(key);
	}
	
	public static String getJsonPath(Response response, String key)
	{
		   String resp = response.asString();
		   JsonPath js = new JsonPath(resp);
		   return js.get(key).toString();
	}
	
	public static JsonPath rowToJson(String response) {
		
		JsonPath js = new JsonPath(response);
		return js;
	}
}
```

<a id="tagging"></a>
### __3.4 Tagging mechanism__

Implemented tagging mechanism to run selected tests from Test Runner file or with Maven command using [filter](#filter). As example from issueValidations.feature file tags @CreateIssue, @Smoke:
```
@CreateIssue @Smoke
Scenario Outline: Verify if issue is being Succesfully created using createIssueAPI
	Given Created session with issue Payload as "<summary>" "<description>"
	When user calls "createIssueAPI" with "POST" http request
	Then the API call got success with status code "201"

Examples:
	|summary 	 		|description 			|
	|summary_test |description_test |
```
<a id="enum"></a>
### __3.5 Enum class with constants__
Created Enum class that represents a group of constants to centralize API resources details.

from _APIPathResources.java_:
```
public enum APIResources {
	createSessionAPI("/rest/auth/1/session"),
	createIssueAPI("/rest/api/2/issue");
	private String resource;
	
	APIResources(String resource)
	{
		this.resource=resource;
	}
	
	public String getResource()
	{
		return resource;
	}

}
```
<a id="datadriven"></a>
### __3.6 Data driven from feature files, external data files__
Implemented Data driven mechanism to drive data dinamically from Feature files - 
as from _issueValidations.feature_ file (from src\test\java\features forlder) where we are driven data from Examples for summary, description, comment:
```
@AddComment @Regression
Scenario Outline: Verify add comment functionality is working
	Given Created session with issue Payload as "<summary>" "<description>"
	When user calls "createIssueAPI" with "POST" http request
	Then the API call got success with status code "201"
	Then user calls "addCommentAPI" with payload as "<comment>" and "POST" http request
	Then the API call got success with status code "201"
	
Examples:
	|summary 	 		|description 			|comment 	 		|
	|summary_test |description_test |comment_test |
```
also to get payload data for API requests created a separate class _TestDataBuild.java_:
```
	......
	public String CreateSessionPayload(String username, String password)
	{
		String payload = "{\r\n"
				+ "  \"username\": \""+username+"\",\r\n"
				+ "  \"password\": \""+password+"\"\r\n"
				+ "}";
		return payload;
	}
	
	public String CreateIssuePayload(String summary, String description, String projectKey)
	{
		
		String payload = "{\r\n"
				+ "    \"fields\": {\r\n"
				+ "        \"project\": {\r\n"
				+ "            \"key\": \""+projectKey+"\"\r\n"
				+ "        },\r\n"
				+ "        \"summary\": \""+summary+"\",\r\n"
				+ "		\"description\": \""+description+"\",\r\n"
				+ "        \"issuetype\": {\r\n"
				+ "            \"name\": \"Bug\"\r\n"
				+ "        }\r\n"
				+ "	}\r\n"
				+ "}";
		return payload;
	}
	....
```


which is used in _StepDefinition.java_ (with supported code for feature file):
```
		given().spec(requestSpecification()).body(data.CreateSessionPayload(adminLogin, adminPassword)).filter(session)
		.when().post(resourceAPI.getResource())
		.then().assertThat().statusCode(200).extract().response().asString();
```
<a id="reports"></a>
### __3.7 Extent Reports__
Reports are generating using Extent Reports and Extentreports adapter dependencies defined in _pom.xml_, Reports configs are defined in _extent.properties, spark-config.xml_ files. Generated 2 types of reports - with html and pdf formats.

<img src="src\test\resources\readme_images\reports1.png">

<img src="src\test\resources\readme_images\reports2.png">

<img src="src\test\resources\readme_images\reports3.png">

<img src="src\test\resources\readme_images\reports4.png">

<a id="logs"></a>
### __3.8 Logs__
Logging mechanisme implemented using PrintStream class in requestSpecification() method. 
from _Utils.java_
```
	public RequestSpecification requestSpecification() throws IOException
	{
		if(req==null)
		{
		PrintStream log = new PrintStream(new FileOutputStream(System.getProperty("user.dir")+"\\logs\\logs.txt"));
		req = new RequestSpecBuilder().setBaseUri(getGlobalValue("baseUrl"))
				.setRelaxedHTTPSValidation()
				.addFilter(RequestLoggingFilter.logRequestTo(log))
				.addFilter(ResponseLoggingFilter.logResponseTo(log))
		.setContentType(ContentType.JSON).build();
		return req;
		}
		return req;
	}
```

Logs are stored in \src\logs folder.


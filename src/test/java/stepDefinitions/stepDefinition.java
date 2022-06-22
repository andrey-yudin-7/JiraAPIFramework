package stepDefinitions;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.APIPathResources;
import resources.APIResources;
import resources.TestDataBuild;
import resources.Utils;

public class stepDefinition extends Utils{
	
	RequestSpecification reqspec;
	ResponseSpecification resspec;
	Response response;
	TestDataBuild data = new TestDataBuild();
	SessionFilter session = new SessionFilter();
	static String issueId;
	static String commentId;
	
	@Given("Created session with issue Payload as {string} {string}")
	public void created_session_with_issue_payload_as(String summary, String description) throws IOException {

		APIResources resourceAPI = APIResources.valueOf("createSessionAPI");

		String adminLogin = getGlobalValue("adminLogin");
		String adminPassword = getGlobalValue("adminPassword");
		String projectKey = getGlobalValue("projectKey");
	
		//Create session
		given().spec(requestSpecification()).body(data.CreateSessionPayload(adminLogin, adminPassword)).filter(session)
		.when().post(resourceAPI.getResource())
		.then().assertThat().statusCode(200).extract().response().asString();
		
		//create issue payload
		reqspec = given().spec(requestSpecification())
		.body(data.CreateIssuePayload(summary, description, projectKey)).filter(session);
	}
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
	@Then("user updating comment calling {string} with payload as {string} and {string} http request")
	public void user_updating_comment_calling_with_payload_as_and_http_request(String resource, String comment, String method) throws IOException {
		APIPathResources resourcePathAPI =  new APIPathResources();
		
		reqspec = given().spec(requestSpecification()).body(data.UpdateCommentPayload(comment)).filter(session);
		
		if(method.equalsIgnoreCase("POST"))
		response = reqspec.when().put(resourcePathAPI.getUpdateCommentAPI(issueId, commentId));
		else if(method.equalsIgnoreCase("GET"))
		response = reqspec.when().get(resourcePathAPI.getUpdateCommentAPI(issueId, commentId));
	}
	@Then("user calls {string} with adding file and {string} http request")
	public void user_calls_with_adding_file_and_http_request(String resource, String method) throws IOException {

		APIPathResources resourcePathAPI =  new APIPathResources();
		
		reqspec = given().spec(requestSpecification()).header("X-Atlassian-Token","no-check").header("Content-Type","multipart/form-data").filter(session)
		.multiPart("file", new File(System.getProperty("user.dir")+"\\src\\test\\resources\\jira.txt"));
		
		if(method.equalsIgnoreCase("POST"))
		response = reqspec.when().post(resourcePathAPI.getAddAttachmentAPI(issueId));
		else if(method.equalsIgnoreCase("GET"))
		response = reqspec.when().get(resourcePathAPI.getAddAttachmentAPI(issueId));
	}
	@Then("user delete the issue by calling {string} with {string} http request")
	public void user_delete_the_issue_by_calling_with_http_request(String resource, String method) throws IOException {
	    // Write code here that turns the phrase above into concrete actions
		APIPathResources resourceDynamicAPI =  new APIPathResources();
		
		given().spec(requestSpecification()).filter(session)
		.when().delete(resourceDynamicAPI.getDeleteIssueAPI(issueId))
		.then().assertThat().statusCode(204).extract().response().asString();
	}
}

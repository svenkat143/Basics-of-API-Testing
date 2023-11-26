package Basics_of_Restassured;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
// import static io.restassured.RestAssured.*;

import static org.hamcrest.Matchers.*;

import java.util.Set;

import org.testng.*;
import org.testng.Assert;

import static org.hamcrest.MatcherAssert.assertThat;

import io.restassured.response.Response;
import io.restassured.config.LogConfig;
import io.restassured.path.json.JsonPath;
import java.util.HashSet;

public class AutomateGET {

	
	@org.testng.annotations.Test
	public void validate_get_status_code() {
		
		
		//RestAssured works on 3 Principles: -
		//given - all input details line url, headers, etc
		//when - Submit the API -resource,http method like get, put, delete,etc
		//Then - validate the response
		
		given(). 
				baseUri("https://api.postman.com").
				header("X-Api-Key","PMAK-65596072592c4f00389d5e7b-7c51b3c8919b8262394cce80a545215107").
		when().
				get("/workspaces").
		then().
				log().all()
				.assertThat()
				.statusCode(200)
				//hasItems, hasItem, equalTo, etc all these methods are coming from hamcrest matchers. 
				.body("workspaces.name", hasItems("Team Workspace","test","My R&D","My Workspace","My Workspace","My Workspace"), // hasItems checks the names of all workspaces.
						"workspaces.name", hasItem("test"), // hasItem check if any workspace name is test. 
						"workspaces.type", hasItems("team","team","team","team","team","personal"),
						"workspaces[0].name", equalTo("Team Workspace"),
						"workspaces[0].name", is(equalTo("Team Workspace")), // The purpose of adding is before equalTo is to make the code more readable.
						"workspaces.size()", equalTo(6));
	}
	
	
	
	// In order to perform other test operations on the response first we need to extract the response using restAssured then perform different assertions using testNg, jUnit, etc. 
	@org.testng.annotations.Test
	public void extract_response() {
		Response res = given(). 
				baseUri("https://api.postman.com").
				header("X-Api-Key","PMAK-65596072592c4f00389d5e7b-7c51b3c8919b8262394cce80a545215107").
		when().
				get("/workspaces").
		then().
				 assertThat()
				.statusCode(200)
				.extract()
				.response();
				
		System.out.println("Reponse =" +res.asString()); // Since the response is in JSON format so we need to convert it to string.
		
	}

	// In Order to extract a single value from the response we need to use .path() function or JsonPath.
	@org.testng.annotations.Test
	public void extract_single_value_from_response() {
		Response res = given(). 
				baseUri("https://api.postman.com").
				header("X-Api-Key","PMAK-65596072592c4f00389d5e7b-7c51b3c8919b8262394cce80a545215107").
		when().
				get("/workspaces").
		then().
				 assertThat()
				.statusCode(200)
				.extract()
				.response();
				
		// System.out.println("First Workspace name =" +res.path("workspaces[0].name"));
		// or we can also create an instance of the response using JsonPath.
		JsonPath js = new JsonPath(res.asString());
		System.out.println("First Workspace name using jsonpath =" +js.getString("workspaces[0].name"));
		 
		// or Initially we can store the response as string.
		// public void extract_single_value_from_response() {
		// String res = given(). 
		// 		baseUri("https://api.postman.com").
		// 		header("X-Api-Key","PMAK-65596072592c4f00389d5e7b-7c51b3c8919b8262394cce80a545215107").
		// when().
		// 		get("/workspaces").
		// then().
		// 		 assertThat()
		// 		.statusCode(200)
		// 		.extract()
		// 		.response().asString();
		// System.out.println("First Workspace name using String =" + JsonPath.from(res).getString("workspaces[0].name"));
	}


	// To Perform hamcrest or TestNg or JUnit assertions on the extracted response.
	@org.testng.annotations.Test
	public void hamcrest_assert_on_extracted_response() {
	String name = given(). 
				baseUri("https://api.postman.com").
				header("X-Api-Key","PMAK-65596072592c4f00389d5e7b-7c51b3c8919b8262394cce80a545215107").
		when().
				get("/workspaces").
		then().
				 assertThat()
				.statusCode(200)
				.extract()
				.response().path("workspaces[0].name");
		System.out.println("Workspace name =" + name);

		// Hamcrest assertions outside the response starts from here.
		assertThat(name, equalTo("Team Workspace"));

		// Testng assertions outside the response starts from here.
		Assert.assertEquals(name, "Team Workspace");
		
		System.out.println("--------------------------------------------------------------------------------");
	}



	// To Perform the logging operations for Request and Response.
	@org.testng.annotations.Test
	public void request_response_logging_operations() {
		Response res = given(). 
				baseUri("https://api.postman.com").
				header("X-Api-Key","PMAK-65596072592c4f00389d5e7b-7c51b3c8919b8262394cce80a545215107").
				log().all(). // For Request logging after the headers write log().all()
				// log().headers(). // If we want to log only the headers part of request.
		when().
				get("/workspaces").
		then().
				 log().all() // For Response logging before the assertThat() write log().all()
				// .log().body() // If we want to log only the body part of the response. 
				.assertThat()
				.statusCode(200)
				.extract()
				.response();
		System.out.println("Reponse =" +res.asString()); // Since the response is in JSON format so we need to convert it to string.
		System.out.println("Reponse =" +res.prettyPrint()); // Since the response is in JSON format so we need to convert it to string.
	}


	// To Perform the logging operations on response only when there is an error in response no logs will be printed when the error is not thrown.
	@org.testng.annotations.Test
	public void log_only_if_error() {
		given(). 
				baseUri("https://api.postman.com").
				header("X-Api-Key","PMAK-65596072592c4f00389d5e7b-7c51b3c8919b8262394cce80a545215107").
				log().all(). // For Request logging after the headers write log().all()
		when().
				get("/workspaces").
		then().
				 log().ifError() // It will prints the logs only if there is an error in response. log().ifError() only works for the response i.e then(). It doesnot work for Request i.e given(). 
				.assertThat()
				.statusCode(200);
	}

	// To Perform the logging operations on request and response when a validation fails.
	@org.testng.annotations.Test
	public void log_only_if_validation_fails() {
		given(). 
				baseUri("https://api.postman.com").
				header("X-Api-Key","PMAK-65596072592c4f00389d5e7b-7c51b3c8919b8262394cce80a545215107").
				// config(config.logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails())). // We use this if we don't want to write the log().ifValidationFails(). in both given() and then().
				log().ifValidationFails(). // It will log the Request if the validation fails.
		when().
				get("/workspaces").
		then().
				 log().ifValidationFails() // It will print the logs of the response only if the validation fails. 
				.assertThat()
				.statusCode(200);
	}


	// To Perform the logging operations by hiding the header as they contains the sensitive information.
	@org.testng.annotations.Test
	public void logs_hide_header() {
		given(). 
				baseUri("https://api.postman.com").
				header("X-Api-Key","PMAK-65596072592c4f00389d5e7b-7c51b3c8919b8262394cce80a545215107").
				config(config.logConfig(LogConfig.logConfig().blacklistHeader("X-Api-Key"))). // We use this if we don't want to show the header in the logs.
		when().
				get("/workspaces").
		then().
				 assertThat()
				.statusCode(200);
	}


	// To Perform the logging operations by hiding multiple headers as they contains the sensitive information.
	@org.testng.annotations.Test
	public void logs_hide_multiple_headers() {
		
		// create a collection of multiple headers that needs to be hidden.
		Set<String> headers = new HashSet<String>();
		headers.add("X-Api-Key");
		headers.add("Accept");

		given(). 
				baseUri("https://api.postman.com").
				header("X-Api-Key","PMAK-65596072592c4f00389d5e7b-7c51b3c8919b8262394cce80a545215107").
				config(config.logConfig(LogConfig.logConfig().blacklistHeaders(headers))). // We use this if we don't want to show multiple headers stored in the headers collection in the logs.
		when().
				get("/workspaces").
		then().
				 assertThat()
				.statusCode(200);
	
	}
	
}

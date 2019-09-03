package restapi.gorest;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import io.restassured.response.Response;


public class TC_GoRest {

	private String _baseURL = "https://gorest.co.in/";
	private String _validAccessToken = "tqgiwz-unv8OWaZXfyNqSzEqIp8nYBUi3Pgo";
	private String _invalidAccessToken = "InvalidToken";

	//*******************************************************************************************************
	//******************************************GET**********************************************************
	//*******************************************************************************************************
	

	
	@Test
	public void t_01_get_request_invalid_token() {

		Response resp = given().
				baseUri(_baseURL).
				auth().oauth2(_invalidAccessToken).
				when().get("/public-api/users").thenReturn();

		Reporter.log("HTTP Status Code: " + resp.getStatusCode() + resp.getBody().asString());

		//resp.jsonPath().getString("")
		//Get Code from the _meta object of the Json
		int statusCode = resp.jsonPath().getInt("_meta.code");
		String meta_message = resp.jsonPath().getString("_meta.message");
		
		Assert.assertEquals(statusCode,401,"Invalid Token Test case. Status code should come as 401");
		Assert.assertEquals(meta_message,"Authentication failed.","Invalid Token Test case. Message should come as Authentication failed.");

	}
	
	@Test
	public void t_02_get_request_valid_token() {

		Response resp = given().
				baseUri(_baseURL).
				auth().oauth2(_validAccessToken).
				when().get("/public-api/users").thenReturn();

		Reporter.log("HTTP Status Code: " + resp.getStatusCode() + resp.getBody().asString());

		//Get Code from the _meta object of the Json
		int statusCode = resp.jsonPath().getInt("_meta.code");
		Assert.assertEquals(statusCode,200,"Valid Token Test case. Status code should come as 200");

	}
	
	
	@Test
	public void t_03_get_request_fetch_all_names() {

		Response resp = given().
				baseUri(_baseURL).
				auth().oauth2(_validAccessToken).
				when().get("/public-api/users").
				thenReturn();

		Reporter.log("HTTP Status Code: " + resp.getStatusCode() + resp.getBody().asString());
		int statusCode = resp.jsonPath().getInt("_meta.code");
		Assert.assertEquals(statusCode,200,"Valid Token Test case. Status code should come as 200");

		//Get list of all the first name
		List<String> list_all_first_name = resp.jsonPath().getList("result.first_name");
		Reporter.log("List of all first name:" + list_all_first_name.toString(),true);

	}
	
	
	@Test
	public void t_04_get_request_assertthat_hamcrest_equalto() {

		given().
		baseUri(_baseURL).
		auth().oauth2(_validAccessToken).
		when().get("/public-api/users").
		then().
		assertThat().body("_meta.code", equalTo(200));

	}
	
	@Test(enabled=false)
	public void t_05_get_count_number_of_male_female() {

		Response resp = given().
				baseUri(_baseURL).
				auth().oauth2(_validAccessToken).
				when().get("/public-api/users").
				thenReturn();

		Reporter.log("HTTP Status Code: " + resp.getStatusCode() + resp.getBody().asString(),true);
		int statusCode = resp.jsonPath().getInt("_meta.code");
		Assert.assertEquals(statusCode,200,"Valid Token Test case. Status code should come as 200");
		
		//Get total number of male
		List<String> list_all_first_name = resp.jsonPath().getList("result.findAll{result->result.gender==female}");
		Reporter.log("List of all female:" + list_all_first_name.toString(),true);

	}
	
	//*******************************************************************************************************
	//****************************************POST***********************************************************
	//*******************************************************************************************************
	@Test
	public void t_06_post_create_new_user() {
		
		String req_body = "{\"first_name\":\"xyz\",\"last_name\":\"akt\"}";
		Response resp = given().
				baseUri(_baseURL).
				auth().oauth2(_validAccessToken).body(req_body).
				when().post("/public-api/users").
				thenReturn();
		Reporter.log("HTTP Status Code: " + resp.getStatusCode() + resp.getBody().asString(),true);
		
	}
	
	


}

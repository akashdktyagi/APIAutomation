package restapi.gorest;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONObject;
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
				when().get("/public-api/users/34").thenReturn();

		Reporter.log("HTTP Status Code: " + resp.getStatusCode() + resp.getBody().asString(),true);
		Reporter.log("Header Info:" + resp.getHeaders().toString(), true);

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
	/*
	 * Sending incomplete POST Data
		Sending Json data with incorrect syntax
		Sending incorrect Verb in the Request.
		Already taken

	 */
	@Test
	public void t_06_post_create_new_user() {

		//Create a JSON Object
		//Check the dependency in Pom xml for this Object
		JSONObject param = new JSONObject();
		String first_name = GetRandomString(5); 
		String second_name = GetRandomString(4); 
		String email = first_name + "." + second_name + "@monsterUniversity.com";
		param.put("first_name", first_name);
		param.put("last_name", second_name);
		param.put("email", email);
		param.put("gender", "male");

		//Set Header content type as Json-with out this record will not be created
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Content-Type", "application/json");

		//Get the Json String
		String req_body = param.toJSONString();

		Reporter.log("Json Req Body: " + req_body,true);
		Response resp = given().
				baseUri(_baseURL).body(req_body).headers(headers).
				auth().oauth2(_validAccessToken).
				when().post("/public-api/users").
				thenReturn();
		Reporter.log("HTTP Status Code: " + resp.getStatusCode() + resp.getBody().asString(),true);

		//************VALIDATION***************
		//Check1: Validate the Status Code
		int status_code_actual = resp.jsonPath().getInt("_meta.code");
		Assert.assertEquals(status_code_actual, 201,"Status code should come as 201");

		//Check2: Validate the message
		String meta_message_expected = "A resource was successfully created in response to a POST request. The Location header contains the URL pointing to the newly created resource.";
		String meta_message_actual = resp.jsonPath().getString("_meta.message");
		Assert.assertEquals(meta_message_actual, meta_message_expected,"Message in Meta");

		//Check3: Fetch ID and search the Id using get web request
		//Fetch Unique Id which is generated
		String id = resp.jsonPath().getString("result.id");
		Reporter.log("ID generated as: " + id, true);

		//Fire the web service again with the Id
		Response resp1 = given().
				baseUri(_baseURL).
				auth().oauth2(_validAccessToken).
				when().get("/public-api/users/" + id).
				thenReturn();
		Reporter.log("Response Returned as: " + resp1.asString(), true);

		Assert.assertEquals(resp.jsonPath().getString("result.first_name"), first_name);


	}
	
	@Test
	public void t_07_post_negative_mandatory_field_email_not_sent() {

		//Create a JSON Object
		//Check the dependency in Pom xml for this Object
		JSONObject param = new JSONObject();
		String first_name = GetRandomString(5); 
		String second_name = GetRandomString(4); 
		String email = first_name + "." + second_name + "@monsterUniversity.com";
		param.put("first_name", first_name);
		param.put("last_name", second_name);
	//	param.put("email", email); //email not sent
		param.put("gender", "male");

		//Set Header content type as Json-with out this record will not be created
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Content-Type", "application/json");

		//Get the Json String
		String req_body = param.toJSONString();

		Reporter.log("Json Req Body: " + req_body,true);
		
		//Validation can be done in a single statement as well, 
		//so in this test cases I am not returning any response back and rather
		//using hamcrest matcher functions equalTo for validation
		given().
			baseUri(_baseURL).
			body(req_body).
			headers(headers).
			auth().
				oauth2(_validAccessToken).
		when().
			post("/public-api/users").
		then().log().body().
			body("_meta.code", equalTo(422)).//json path 
			and().
			body("_meta.message", equalTo("Data validation failed. Please check the response body for detailed error messages.")).
			and().
			body("result[0].message", equalTo("Email cannot be blank."));
		
		Reporter.log("Response message: " , true);
			

	}

	//To get random Key
	public String GetRandomString(int n) {
		// lower limit for LowerCase Letters 
		int lowerLimit = 97; 

		// lower limit for LowerCase Letters 
		int upperLimit = 122; 

		Random random = new Random(); 

		// Create a StringBuffer to store the result 
		StringBuffer r = new StringBuffer(n); 

		for (int i = 0; i < n; i++) { 

			// take a random value between 97 and 122 
			int nextRandomChar = lowerLimit 
					+ (int)(random.nextFloat() 
							* (upperLimit - lowerLimit + 1)); 

			// append a character at the end of bs 
			r.append((char)nextRandomChar); 
		} 

		// return the resultant string 
		return r.toString(); 
	} 

}

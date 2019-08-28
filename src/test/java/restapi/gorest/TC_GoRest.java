package restapi.gorest;


import static io.restassured.RestAssured.*;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import io.restassured.response.Response;
public class TC_GoRest {

	private String _baseURL = "https://gorest.co.in/";
	private String _validAccessToken = "tqgiwz-unv8OWaZXfyNqSzEqIp8nYBUi3Pgo";
	private String _invalidAccessToken = "InvalidToken";

	@Test
	public void t_01_get_request_invalid_token() {

		Response resp = given().
				baseUri(_baseURL).
				auth().oauth2(_invalidAccessToken).
				when().get("/public-api/users").thenReturn();

		Reporter.log("Status Code: " + resp.getStatusCode() + resp.getBody().asString());

		//Get Code from the _meta object of the Json
		int statusCode = resp.jsonPath().getInt("_meta.code");
		Assert.assertEquals(statusCode,401,"Invalid Token status code should come as 401");

	}


}

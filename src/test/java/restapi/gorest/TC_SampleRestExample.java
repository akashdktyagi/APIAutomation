package restapi.gorest;

import static io.restassured.RestAssured.*;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import io.restassured.response.Response;
public class TC_SampleRestExample {
	
	@Test
	public void t_01_get_request() {
		
		Response resp = given().
						baseUri("https://gorest.co.in").
						auth().oauth2("tqgiwz-unv8OWaZXfyNqSzEqIp8nYBUi3Pgo").
						when().get("/public-api/users/").thenReturn();
		
		
		
		String resp_string = resp.asString();
		Reporter.log("Status code: " + resp.getStatusCode() + " and Response: " + resp_string, true );
	
		
	}

}

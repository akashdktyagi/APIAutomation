package restapi.reqres;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TC_ReqRes {
	private String _baseUrl = "https://reqres.in";

	@Test(dataProvider="DP_DataProvider_UsersInfo",enabled=false)
	public void t_01_rest_get_users_validate_status_code(String endPointUrl, String statusCodeExpected) {
		
		Reporter.log("Data Sent from Data Provider : End Point: " + endPointUrl  + " Expected Status Code: " + statusCodeExpected,true);
		
		//Get the response
		Response resp = given().
						baseUri(_baseUrl).
						when().
						get(endPointUrl).thenReturn();
		
		//Get the Status code from Response
		int statusCodeActual = resp.getStatusCode();
		
		//Assert Status code
		Assert.assertEquals(statusCodeActual, Integer.parseInt(statusCodeExpected),"Status code Validation");

	}
	
	@Test(enabled = false)
	public void t_02_get_fetch_all_users_info() {
		
		
		Response resp = given().
						baseUri(_baseUrl).
						when().
						get("/api/users/2").thenReturn();
		
		int statusCode = resp.getStatusCode();
		if (statusCode==200) {
			//Equal to is Hamcrest Matcher. Check the import at the top
			//Hamrest is a transitive dependency i.e. you do not have to download
			resp.then().body("data.first_name", equalTo("Janet")); 
			Reporter.log("Test case passed. First name returned correctly. Full Json string: " + resp.asString(),true);
			
		}else {
			Assert.assertTrue(false, "Status code is not 200");
		}
		
	}
	
	@DataProvider
	public Object[][] DP_DataProvider_UsersInfo() {
		
		String[][] result = {
				
				{"/api/users?page=1","200"},
				{"/api/users?page=2","200"},
				{"/api/users/1","200"},
				{"/api/users/2","200"},
				{"/api/users/3","200"},
				{"/api/users/4","200"},
				{"/api/users/5","200"},
				{"/api/users/6","200"},
				{"/api/users/7","200"},
				{"/api/users/8","200"},
				{"/api/users/9","200"},
				{"/api/users/10","200"},
				{"/api/users/11","200"},
				{"/api/users/12","200"},
				{"/api/users/23","404"}//User not found

				
		};
		
		return result;
		
		
	}
	
}

package restapi.restcountrieseu;


/*
 * Important Links
 * https://github.com/rest-assured/rest-assured/wiki/GettingStarted
 * 
 */

import static io.restassured.RestAssured.*;

import java.util.List;

import org.testng.Reporter;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
public class TC_RestApiCountries {

	
	@Test(enabled=false)
	public void t_01_rest_api() {
		
		List<String> list = given().
						baseUri("https://restcountries.eu").
						get("rest/v2/capital/delhi").jsonPath().getList("altSpellings",String.class);
		
		Reporter.log((String) list.get(0),true);

				
		
	
	}

}

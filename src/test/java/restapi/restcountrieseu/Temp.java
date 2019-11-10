package restapi.restcountrieseu;

import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Temp {

	//Example to demonstrate Single DP method can be used in multiple test
	
	@Test(dataProvider="dp_method")
	public void t1(String arg1, String arg2) {
		
		Reporter.log("Data sent from data Provider as: " + arg1 + "-" + arg2);
	}
	
	@Test(dataProvider="dp_method")
	public void t2(String arg1, String arg2) {
		
		Reporter.log("Data sent from data Provider as: " + arg1 + "-" + arg2);
	}
	
	@Test(dataProvider="dp_method")
	public void t3(String arg1, String arg2) {
		
		Reporter.log("Data sent from data Provider as: " + arg1 + "-" + arg2);
	}
	
	@DataProvider
	public Object[][] dp_method(){
		
		String[][] result = 
			{
					//You can write code to fetch data from Excel file
					{"Akash","Tyagi"},
					{"Sonali","Adik"},
					{"Sarang","Holey"}
			};
		
		return result;
	}

}

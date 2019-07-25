package MavenFramework;

import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import org.apache.logging.log4j.*;
import static MavenFramework.DataSetters.*;
import static MavenFramework.ExcelReader.setExcelSheet;
import static MavenFramework.ExcelReader.setRow;
import static MavenFramework.ExcelReader.numberOfRows;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Testers {

	private static Logger Log = LogManager.getLogger(Keywords.class.getName());
	@BeforeTest
	public void setSheet() throws Exception {
		setProperties();
		String TestSheet = (String) prop.getProperty("TESTSHEET"); 
		String TestWorkbook = (String) prop.getProperty("TESTWORKBOOK");
		setExcelSheet(TestWorkbook, TestSheet );
	}

	@Test(dataProvider = "requestAndResponse")
	public void runTest(Map<String, Object> testDataMap) throws Exception {
		Integer testNumber = (Integer) testDataMap.get("testNo");
		Log.info("-----------------TEST: " + testNumber+"-------------\n");
		String method = (String) testDataMap.get("method");
		String resource = (String) testDataMap.get("resource");
		String responseCode = (String) testDataMap.get("responseCode");
		RequestSpecification reqSpecs = (RequestSpecification) testDataMap.get("request");
		@SuppressWarnings("unchecked")
		Map<String, String> expectedResponses = (Map<String, String>) testDataMap.get("response");
		Response res = callMethod(method, reqSpecs, resource, responseCode);
		Log.info("EXPECTED RESPONSES AFTER CALLING API" + expectedResponses.keySet());
		JsonPath actual = new JsonPath(res.asString());
		for (String keys : expectedResponses.keySet()) {
			Assert.assertEquals(actual.get(keys), expectedResponses.get(keys));
		}

	}

	@DataProvider(name = "requestAndResponse")
	public Object[][] getTestData() {
		int n = numberOfRows();
		Object[][] testCases = new Object[n - 1][1];
		for (int i = 0; i < n - 1; i++) {
			setRow(i + 1); // Set Row number
			RequestSpecification reqBody = getSpec(); // get request details for that row
			Map<String, String> expectedResponses = getMapData("Expected Response"); // get the expected response values
			String expectedResponseCode = getData("Expected Response Code");
			String method = getData("Method"); // get the method to be called
			String resource = getData("Resource"); // get the resource to be used
			Map<String, Object> testDataMap = new HashMap<String, Object>(); // Add all the above to a map
			testDataMap.put("testNo", i + 1);
			testDataMap.put("method", method);
			testDataMap.put("resource", resource);
			testDataMap.put("request", reqBody);
			testDataMap.put("responseCode", expectedResponseCode);
			testDataMap.put("response", expectedResponses);
			testCases[i][0] = testDataMap;
			Log.info("TEST DATA: "+ testDataMap);
		}
		return testCases;
	}
	// After test to be written to close excel sheet and input stream
}

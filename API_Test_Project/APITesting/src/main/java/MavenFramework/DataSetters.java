package MavenFramework;

import static MavenFramework.ExcelReader.getCellData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import MavenFramework.Keywords;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DataSetters {

	public static Properties prop = new Properties();
	public static void setProperties() throws IOException, FileNotFoundException{
		FileInputStream fis = new FileInputStream("C:\\Users\\s0s04y3\\Desktop\\API_Test_Project\\APITesting\\src\\main\\java\\MavenFramework\\env.properties");
		prop.load(fis);
	}
	
	public static String getData(String columnName) {
		return getCellData(columnName);
	}

	public static Map<String, String> getMapData(String columnName) {
		String dataMap = getCellData(columnName);
		if (dataMap == null)
			return null;
		dataMap = StringUtils.substringBetween(dataMap, "{", "}");
		String[] keyValuePairs = dataMap.split(" +, +"); // FIGURE OUT BETTER WAY TO SEPARATE
		Map<String, String> jsonAsMap = new HashMap<String, String>();
		for (String pair : keyValuePairs) {
			String[] entry = pair.split(":");
			jsonAsMap.put(entry[0].trim(), entry[1].trim());
		}
		return jsonAsMap;
	}

	public static RequestSpecification getSpec() {
		String baseURI = prop.getProperty("BASEURL");
		Map<String, String> headerList = getMapData("Headers");
		Map<String, String> paramList = getMapData("Parameters");
		Map<String, String> queryParamList = getMapData("Query Parameters");
		Map<String, String> pathParamList = getMapData("Path Parameters");
		String givenBody = getData("Body");

		RequestSpecBuilder spec = new RequestSpecBuilder();
		spec.setBaseUri(baseURI);
		if (headerList != null)
			spec.addHeaders(headerList);
		if (paramList != null)
			spec.addParams(paramList);
		if (queryParamList != null)
			spec.addQueryParams(queryParamList);
		if (pathParamList != null)
			spec.addPathParams(pathParamList);
		if (givenBody != null)
			spec.setBody(givenBody);
		return spec.build();

//		System.out.println("BASE URL: " + RestAssured.baseURI);
//		if(method != null)
//			System.out.println("METHOD: "+ method);
//		if(resource != null)
//			System.out.println("RESOURCE: " + resource);
//		if(headerList != null)
//			System.out.println("HEADERS: " + headerList);
//		if(paramList != null)
//			System.out.println("PARAMETERS: " + paramList);
//		if(contentType != null)
//			System.out.println("CONTENT TYPE: "+contentType);
//		if(givenBody != null)
//			System.out.println("BODY: "+(givenBody));
//		if(expectedResponses != null)
//			System.out.println("EXPECTED: "+ expectedResponses);

	}

	public static Response callMethod(String methodName, RequestSpecification specs, String resource,
			String responseCode) throws Exception {
		Keywords keywords = new Keywords();
		int rc = Integer.parseInt(responseCode);
		Method method[] = keywords.getClass().getMethods();
		for (int i = 0; i < method.length; i++) {
			if (method[i].getName().equalsIgnoreCase(methodName)) {
				Response res = (Response) method[i].invoke(keywords, specs, resource, rc);
				return res;
			}
		}
		return null;
	}

	public static JsonPath rawToJson(Response r) {
		String respon = r.asString();
		JsonPath x = new JsonPath(respon);
		return x;
	}
}

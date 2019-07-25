package MavenFramework;

import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.io.IOException;
import static io.restassured.RestAssured.given;
import org.apache.logging.log4j.*;;

public class Keywords {

	private static Logger Log = LogManager.getLogger(Keywords.class.getName());

	public static Response GET(RequestSpecification spec, String resource, int responseCode) throws IOException {
		Log.info("KEYWORD GET");
		Response res = given().spec(spec).when().get(resource).then().statusCode(responseCode).extract().response();
		return res;
	}

	public static Response POST(RequestSpecification specs, String resource, int responseCode) {
		Log.info("KEYWORD POST");
		Response res = given().spec(specs).when().post(resource).then().statusCode(responseCode).extract().response();
		JsonPath actual = new JsonPath(res.asString());
		Log.debug(res.asString() + actual.get("id"));
		String deleteRecord = "api/v1/delete/" + actual.get("id");
		QueryableRequestSpecification queryable = SpecificationQuerier.query(specs);
		RestAssured.baseURI = queryable.getBaseUri();
		if(RestAssured.baseURI.equalsIgnoreCase("http://dummy.restapiexample.com"));
			given().spec(specs).when().delete(deleteRecord);
		return res;
	}

	public static Response PUT(RequestSpecification specs, String resource, int responseCode) {
		Log.info("KEYWORD PUT");
		Response res = given().spec(specs).when().put(resource).then().statusCode(responseCode).extract().response();
		return res;
	}

	public static Response DELETE(RequestSpecification specs, String resource, int responseCode) {
		Log.info("KEYWORD DELETE");
		Response res = given().spec(specs).when().delete(resource).then().statusCode(responseCode).extract().response();
		return res;
	}

}

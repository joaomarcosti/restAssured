package support;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;

public class Support implements Constants {

	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = URL_BASE;
		RestAssured.port =  PORT;
		RestAssured.basePath = BASE_PATH;
		
		
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.setContentType(Constants.CONTENT_TYPE);		
		RestAssured.requestSpecification= reqBuilder.build();
		
		
		ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
		resBuilder.expectResponseTime(Matchers.lessThan(Constants.TIMED_OUT));
		RestAssured.responseSpecification = resBuilder.build();
		
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}

}
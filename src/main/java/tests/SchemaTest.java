package tests;

import static io.restassured.RestAssured.given;

import org.junit.Test;

import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidationException;
import io.restassured.module.jsv.JsonSchemaValidator;

import org.xml.sax.SAXParseException;

public class SchemaTest {
	@Test
	public void validaSchemaXml() {
		given()
		.log().all()
		.when()
			.get("http://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(200)
			.body(RestAssuredMatchers.matchesXsdInClasspath("user.xsd"))	
		;
	}
	
	@Test(expected = SAXParseException.class)
	public void validaSchemaXmlInvalid() {
		given()
		.log().all()
		.when() 
			.get("http://restapi.wcaquino.me/invalidUsersXML")
		.then()
			.log().all()
			.statusCode(200)
			.body(RestAssuredMatchers.matchesXsdInClasspath("user.xsd"))	
		;
	}
	
	@Test
	public void validaSchemaJson() {
		given()
		.log().all()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(200)
			.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("user.json"))	
		;
	}
}

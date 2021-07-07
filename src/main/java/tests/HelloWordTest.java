package tests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class HelloWordTest {

	//utilizing JUnit//
		
	@Test	
	public void helloWordTest() {
		
		//make a request e compare with the response*//
		Response response = RestAssured.request(Method.GET,"http://restapi.wcaquino.me:80/ola");
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue(response.statusCode() == 200);
		Assert.assertTrue("Status Code Should be 200", response.statusCode() == 200);
		Assert.assertEquals(200, response.statusCode());
		
		//return a validate response//
		ValidatableResponse check = response.then();
		check.statusCode(200);	
	}
	
	@Test
	public void otherFormsRestAssured() {
		Response response = RestAssured.request(Method.GET,"http://restapi.wcaquino.me/ola");
		ValidatableResponse check = response.then();
		check.statusCode(200);	
		
		get("http://restapi.wcaquino.me/ola").then().statusCode(200);	
		
		//BDD mode//
		given()
			//pre condicoes
		.when()
			.get("http://restapi.wcaquino.me/ola")
		.then()
			.statusCode(200);	
	}
	
	@Test
	
	public void matchersHamcrest() {
		
		Assert.assertThat("Maria", Matchers.is("Maria"));
		Assert.assertThat(128, Matchers.is(128));
		Assert.assertThat(128, Matchers.isA(Integer.class));
		Assert.assertThat(128d, Matchers.isA(Double.class));
		Assert.assertThat(128d, Matchers.greaterThan(10d));
		Assert.assertThat(128d, Matchers.lessThan(130d));
		
		List<Integer> impares = Arrays.asList(1,3,5,7,9);
		assertThat(impares, hasSize(5));
		//all elements
		assertThat(impares, contains(1,3,5,7,9));
		//any order
		assertThat(impares, containsInAnyOrder(1,3,5,9,7));
		assertThat(impares, hasItem(1));
		assertThat(impares, hasItems(1,5));
		
		assertThat("Maria", not("João"));
		assertThat("Maria", anyOf(is("Maria"), is("Joaquina")));
		assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui")));
	}	
	
	@Test
	public void validarBody() {
		given()

		.when()
			.get("http://restapi.wcaquino.me/ola")
		.then()
			.statusCode(200)
			.body(is("Ola Mundo!"))
			.body(containsString("Mundo"))
			.body(is(not(nullValue())));
		
	
	}
}

package tests;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class HelloWord {
	
	public static void main(String[] args) {
		
		//make a request e compare with the response*//
		Response response = RestAssured.request(Method.GET,"http://restapi.wcaquino.me:80/ola");
		System.out.println(response.getBody().asString().equals("Ola Mundo!"));
		System.out.println(response.statusCode() == 200);
		
		//return a validate response//
		ValidatableResponse check = response.then();
		check.statusCode(200);	
	}
}

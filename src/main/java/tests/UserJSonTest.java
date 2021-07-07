package tests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import junit.framework.Assert;


public class UserJSonTest {
	
	@Test
	public void verificaPrimeiroNivel() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/1")
		.then()
			.statusCode(200) 
			.body("id", is(1))
			.body("name", containsString("Silva"))
			.body("age", greaterThan(18));		
		
	}
	
	@Test
	public void deveVerificarPrimeiroNivelOutrasFormas() {
		Response response = RestAssured.request(Method.GET,"http://restapi.wcaquino.me/users/1");
		
		//patch
		
		org.junit.Assert.assertEquals(new Integer (1), response.path("id"));
		org.junit.Assert.assertEquals(new Integer (1), response.path("%s", "id"));
		
		//jsonpath
		JsonPath  jpath = new JsonPath(response.asString());
		org.junit.Assert.assertEquals(1, jpath.getInt("id"));
		
		//from
		int id = JsonPath.from(response.asString()).getInt("id");
		org.junit.Assert.assertEquals(1, id);
	}
	
	@Test
	public void segundoNivel() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/2")
		.then()
			.statusCode(200)
			.body("name", containsString("Joaquina"))
			.body("endereco.rua", is("Rua dos bobos"));	
	}
	
	@Test
	public void validarLista() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/3")
		.then()
			.statusCode(200)
			.body("name", containsString("Ana"))
			.body("filhos", hasSize(2))
			.body("filhos", hasSize(2))
			.body("filhos[0].name", is("Zezinho"))
			.body("filhos[1].name", is("Luizinho"))
			.body("filhos.name", hasItems("Zezinho", "Luizinho"))
			;
	}	
	
	@Test
	public void mensagemErro() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/4")
		.then()
			.statusCode(404)
			.body("error", is("Usu�rio inexistente"))
			;
	}	
	
	@Test
	public void listaNaRaiz() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))
			.body("name", hasItems("Jo�o da Silva", "Maria Joaquina", "Ana J�lia"))
			.body("age[1]", is(25))
			.body("filhos.name", hasItem(Arrays.asList("Zezinho","Luizinho")))
			.body("salary", contains(1234,5678f,2500,null))
			;
	}	
	
	@Test
	public void verifacoesAvancadas() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))
			.body("age.findAll{it <= 25}.size()", is(2))
			.body("age.findAll{it <= 25 && it > 20}.size()", is(1))
			.body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))
			.body("findAll{it.age <= 25}[0].name", is("Maria Joaquina"))
			.body("findAll{it.age <= 25}[-1].name", is("Ana J�lia"))
			.body("find{it.age <= 25}.name", is("Maria Joaquina"))
			.body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina","Ana J�lia"))
			.body("findAll{it.name.length() > 10}.name", hasItems("Maria Joaquina","Jo�o da Silva"))
			.body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
//			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)))
			.body("age.collect{it * 2}", hasItems(60,50,40))
			.body("id.max()", is(3))
			.body("salary.min()", is(1234.5678f))
			.body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678f,0.001)))
			.body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d), lessThan(5000d)))
			;
	}	
	
	@Test
	public void simplificandoComUniaoJsonPathComJava() {
		ArrayList<String> names =
			given()
			.when()
				.get("http://restapi.wcaquino.me/users")
			.then()
				.statusCode(200)
				.extract().path("name.findAll{it.startsWith('Maria')}")
			;
		Assert.assertEquals(1, names.size());
		Assert.assertTrue(names.get(0).equalsIgnoreCase("Maria joaquIna"));		
		Assert.assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());
	}
}
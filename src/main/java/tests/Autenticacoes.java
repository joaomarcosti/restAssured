package tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;

public class Autenticacoes {

	@Test
	public void deveAcessarSwapiPublica() {
		given()
			.log().all()
		.when()
			.get("https://swapi.dev/api/people/1") 
		.then()
			.log().all()
			.statusCode(200)	
			.body("name", is("Luke Skywalker"))
		;
	}
	
	@Test
	public void obterClima() {
		given()
			.log().all()
		.when()
			.queryParam("q", "Fortaleza,BR")
			.queryParam("appid", "36b49428af6d563140df82d06713f2af")
			.queryParam("units", "metric")
			.get("https://api.openweathermap.org/data/2.5/weather")
		.then()
			.log().all()
			.statusCode(200)	
			.body("name", is("Fortaleza"))
		;
	}
	
	@Test
	public void naoDeveAcessarSemSenha() {
		given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(401)	
		;
	}
	
	@Test
	public void deveAutenticarComUsuario() {
		given()
			.log().all()
		.when()
			.get("http://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)	
			.body("status", is("logado"))
		;
	}
	
	@Test
	public void deveAutenticarComUsuarioV2() {
		given()
			.log().all()
			.auth().basic("admin", "senha")
		.when()
			.get("http://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)	
			.body("status", is("logado"))
		;
	}
	
	@Test
	public void deveAutenticarComUsuarioChallenge() {
		given()
			.log().all()
			.auth().preemptive().basic("admin", "senha")
		.when()
			.get("http://restapi.wcaquino.me/basicauth2")
		.then()
			.log().all()
			.statusCode(200)	
			.body("status", is("logado"))
		;
	}
	
	@Test
	public void tokenJwt() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "joaomarcos.ti@gmail.com");
		login.put("senha", "123456");
		String token = given()
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
			.auth().preemptive().basic("admin", "senha")
		.when()
			.post("http://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)	
			.extract().path("token")
		;
		
		//obter contas
		
		given()
			.log().all()
			.header("Authorization", "JWT " + token)
		.when()
			.get("http://https://seubarriga.wcaquino.me/logout")
		.then()
			.log().all()
			.statusCode(200)	
			.body("nome", hasItem("Conta de teste"))
			;
	}
	
	@Test
	public void diretaoNaWeb() {
		String biscoito = given()
			.log().all()
			.formParam("email", "joaomarcos.ti@gmail.com")
			.formParam("senha", "123456")
			.contentType(ContentType.URLENC.withCharset("UTF-8"))
		.when()
			.post("http://seubarriga.wcaquino.me/logar")
		.then()
			.log().all()
			.statusCode(200)
			.extract().header("set-cookie")
		;
		
		biscoito = biscoito.split("=")[1].split(";")[0];
		System.out.println(biscoito);
		
		//obter a conta
		
		given()
			.log().all()
			.cookie("connect.sid", biscoito)
		.when()
			.get("http://seubarriga.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)	
		;
	}


}

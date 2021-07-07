package tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.io.File;
import io.restassured.http.ContentType;

import org.junit.Test;

public class UploadArquivo {

	@Test
	public void obrigaEnvioArquivo() {
		given()
		.log().all()
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404) //should be 400
			.body("error", is("Arquivo não enviado"))
		;
	}
	
	@Test
	public void enviarArquivo() {
		given()
		.log().all()
		.multiPart("arquivo", new File("src/main/resources/arquivo.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200) 
			.body("name", is("arquivo.pdf"))
		;
	}
	
	@Test
	public void maiorQue1mb() {
		given()
		.log().all()
		.multiPart("arquivo", new File("src/main/resources/maior1mb.docx"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
//			.time(lessThan(5000))
			.statusCode(413) 
//			.body("name", is("arquivo.pdf"))
		;
	}
}

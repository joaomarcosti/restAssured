package tests;

import static io.restassured.RestAssured.given;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import io.restassured.RestAssured;
import support.ManipuladorData;
import support.Movimentacao;
import support.Support;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntegrateFlow extends Support {
	
	private static String CONTA_NAME = "Conta " + System.nanoTime();
	private static Integer CONTA_ID;
	private static Integer MOV_ID;
		
	@BeforeClass
	public static void login() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "joaomarcos.ti@gmail.com");
		login.put("senha", "123456");
		String TOKEN = given()
			.body(login)		
		.when()
			.post("/signin")
		.then()
			.statusCode(200)
			.extract().path("token");
		
		RestAssured.requestSpecification.header("Authorization", "JWT "  + TOKEN);
		
		}
	
	@Test
	public void c01_deveIncluirContaComSucesso() {
		CONTA_ID = given()		
			.body("{ \"nome\": \""+CONTA_NAME+"\" }")
		.when()
			.post("/contas")
		.then()
			.statusCode(201)
			.extract().path("id")
	;
	}
	
	@Test
	public void c02_deveAlterarContaSucesso() {
		given()		
			.body("{ \"nome\": \""+CONTA_NAME+" alterada\" }")
			.pathParam("id", CONTA_ID)
		.when()
			.put("/contas/{id}")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", Matchers.is(CONTA_NAME+" alterada"))
	;
	}
	
	@Test
	public void c03_naoDeveInserirContaMesmoNome() {
		given()	
			.body("{ \"nome\": \""+CONTA_NAME+" alterada\" }")
		.when()
			.post("/contas")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", Matchers.is("Já existe uma conta com esse nome!"))
	;
	}
	
	@Test
	public void c04_deveInserirMovimentacaoSucesso() {
	Movimentacao mov = getMovimentacaoValida();
		
		MOV_ID = given()		
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.log().all()
			.statusCode(201)
			.extract().path("id");
	;
	}
	
	@Test
	public void c05_validaCamposObrigatoriosMovimentacao() {;
		given()		
			.body("{}")
		.when()
			.post("/transacoes")
		.then()
			.log().all()
			.statusCode(400)
			.body("$", Matchers.hasSize(8))
			.body("msg", Matchers.hasItems(
					"Data da Movimentação é obrigatório",
					"Data do pagamento é obrigatório",
					"Descrição é obrigatório",
					"Interessado é obrigatório",
					"Valor é obrigatório",
					"Valor deve ser um número",
					"Conta é obrigatório",
					"Situação é obrigatório"
					))
	;
	}
	
	@Test
	public void c06_naoDeveInsetirMovimentacaComDataFutura() {
	
		Movimentacao mov = getMovimentacaoValida();
		
		mov.setData_transacao(ManipuladorData.getDataDiferencaDias(2));
		given()			
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.log().all()
			.statusCode(400)
			.body("$", Matchers.hasSize(1))
			.body("msg", Matchers.hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
	;
	}
	
	
	@Test
	public void c07_naoDeveRemoverContaComMovimetnacao() {
		given()
			.pathParam("id", CONTA_ID)
		.when()
			.delete("/contas/{id}")
		.then()
			.log().all()
			.statusCode(500)
			.body("constraint", Matchers.is("transacoes_conta_id_foreign"))
	;
	}
	
	@Test
	public void c08_deveCalcualarSaldoContas() {
		given()	
		.when()
			.get("/saldo")
		.then()
			.log().all()
			.statusCode(200)
			.body("find{it.conta_id == "+CONTA_ID+"}.saldo", Matchers.is("100.00"))
	;
	}
	
	@Test
	public void c09_deveRemoverMovimentacao() {
		given()		
			.pathParam("id", MOV_ID)
		.when()
			.delete("/transacoes/{id}")
		.then()
			.log().all()
			.statusCode(204)
	;
	}
	
	
	private Movimentacao getMovimentacaoValida() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(CONTA_ID);
//		mov.setUsuario_id();
		mov.setDescricao("Descrição da Movimentação");
		mov.setEnvolvido("Envolvida na mov");
		mov.setTipo("REC");
		mov.setData_transacao(ManipuladorData.getDataDiferencaDias(-1));
		mov.setData_pagamento(ManipuladorData.getDataDiferencaDias(5));
		mov.setValor(100f);
		mov.setStatus(true);
		return mov;
	}

}


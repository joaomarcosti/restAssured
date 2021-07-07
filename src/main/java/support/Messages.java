package support;

public enum Messages {
	
	CAMPO_NAO_PODE_ESTAR_EM_BRANCO("can't be blank"),
	DADO_JA_UTILIZADO("has already been taken"),
	USUARIO_NAO_ENCONTRADO("Resource not found"),
	AUTHENTICATION_FAILED("Authentication failed");

	private final String message;

	private Messages(String message) {
		this.message = message;
	}
	
	public String getMensagem(){
		return message;
	}
}

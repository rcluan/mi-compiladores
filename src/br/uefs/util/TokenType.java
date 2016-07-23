package br.uefs.util;

/**
 * Should have both well and bad-formatted tokens' pattern
 *
 */
public enum TokenType {
	
	PALAVRARESERVADA("\\b(programa|const|var|funcao|inicio|fim|se|entao|"
			+ "senao|enquanto|faca|leia|escreva|inteiro|real|booleano|"
			+ "verdadeiro|falso|cadeia|caractere)\\b", ""),
	IDENTIFICADOR("([a-z]|[A-Z])+([0-9]|\\_)*", ""),
	NUMERO("[0-9]+(\\.[0-9]+)?", ""),
	COMENTARIO("\\{\\s*(.*?)\\s*\\}", ""),
	OPERADORLOGICO("\\b(nao|e|ou)\\b", ""),
	OPERADORARITMETICO("\\+|\\-|\\*|\\/", ""),
	OPERADORRELACIONAL("[<>]?=|[<?]>|[<>]", ""),
	DELIMITADOR("\\;|\\,|\\(|\\)", ""),
	CADEIA("\"([a-z]|[A-Z])+([0-9]| )*\"", ""),
	CARACTERE("\'([a-z]|[A-Z]|[0-9])\'", ""),
	
	VALORINESPERADO("\\.|\\!|\\:|\\^|\\~|\\_|\\!|\\?|\\@|\\#|\\$|\\%|\\¬|\\&|\\[|\\]|\\º|\\ª|\\|","Valor inesperado"),
	CADEIAMALFORMADA("\"(^[a-z]|^[A-Z])*(.*?)\"", "A cadeia possui caracteres inválidos ou está vazia"),
	CADEIANAOFECHADA("\"([a-z]|[A-Z])+([0-9]| )*(?!\")", "A cadeia de caracteres não foi fechada"),
	CARACTEREMALFORMADO("\'(^[a-z]|^[A-Z]|^[0-9])*\'", "O caractere contém um valor inválido ou está vazio"),
	CARACTERENAOFECHADO("\'([a-z]|[A-Z]|[0-9])(?!\')", "O caractere não foi encerrado com aspas simples"),
	NUMEROMALFORMADO("", "O número está mal formado");
	
	public final String pattern;
	public final String message;
	
	private TokenType(String pattern, String message){
		
		this.pattern = pattern;
		this.message = message;
	}
}

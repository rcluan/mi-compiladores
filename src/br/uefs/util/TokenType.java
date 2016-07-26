package br.uefs.util;

/**
 * Should have both well and bad-formatted tokens' pattern
 *
 */
public enum TokenType {
	
	PALAVRARESERVADA("\\b(programa|const|var|funcao|inicio|fim|se|entao|"
			+ "senao|enquanto|faca|leia|escreva|inteiro|real|booleano|"
			+ "verdadeiro|falso|cadeia|caractere)\\b", ""),
	OPERADORLOGICO("\\b(nao|e|ou)\\b", ""),
	IDENTIFICADOR("((?<=[\"\'\\+\\-\\*\\/;,\\s[<>]?=[<?]>[<>](){0-9]|^)[a-zA-Z][a-zA-Z0-9_]*(?=[\"\'\\+\\-\\*\\/;,\\s[<>]?=[<?]>[<>](){0-9]|$))", ""),
	NUMERO("((?<=[\"\'\\+\\-\\*\\/;,\\s[<>]?=[<?]>[<>](){a-zA-Z]|^)[0-9]+([.][0-9]+)?(?=[\"\'\\+\\-\\*\\/;,\\s[<>]?=[<?]>[<>](){a-zA-Z]|$))", ""),
	COMENTARIO("[{]\\s*(.*?)\\s*[}]", ""),
	OPERADORARITMETICO("\\+|\\-|\\*|\\/", ""),
	OPERADORRELACIONAL("[<>]?=|[<?]>|[<>]", ""),
	DELIMITADOR("\\;|\\,|\\(|\\)", ""),
	CADEIA("\"([a-z]|[A-Z])+([0-9]| )*\"", ""),
	CARACTERE("\'([a-z]|[A-Z]|[0-9])\'", ""),
	
	VALORINESPERADO("[.|!|:|^|~|´|`|_|!|?|@|#|$|%|¬|&|\\[|\\]|º|ª|\\|]","Valor inesperado"),
	
	CADEIAMALFORMADA("\"([^a-zA-Z])*(.*?)\"", "A cadeia possui caracteres inválidos ou está vazia"),
	CADEIANAOFECHADA("\"([a-zA-Z]*([0-9]| )*(?=[\'\\+\\-\\*\\/;,\\s[<>]?=[<?]>[<>](){]|$))", "A cadeia de caracteres não foi fechada"),
	
	CARACTEREMUITOGRANDE("\'([a-z]|[A-Z]|[0-9])+\'", "O caractere possui valores permitidos mas seu comprimento é inválido"),
	CARACTEREVAZIO("\'( )*\'", "O caractere não pode ser vazio"),
	CARACTEREINVALIDO("\'([+|-|*|/|.|!|:|^|~|´|`|_|!|?|@|#|$|%|¬|&|\\[|\\]|º|ª|\\|])(.*?)\'", "O caractere possui valores inválidos"),
	CARACTERENAOFECHADO("\'([a-zA-Z]|[0-9](?=[\"\\+\\-\\*\\/;,\\s[<>]?=[<?]>[<>](){]|$))", "O caractere não foi encerrado com aspas simples"),
	
	NUMEROMALFORMADO("([0-9]+\\.[.|!|:|^|~|´|`|_|!|?|@|#|$|%|¬|&|\\[|\\]|º|ª|\\|a-zA-Z]*(?=[\"\'\\+\\-\\*\\/;,\\s[<>]?=[<?]>[<>](){a-zA-Z]|$))", "O número está mal formado"),
	
	IDENTIFICADORMALFORMADO("([a-zA-Z]+[.|!|:|^|~|´|`|_|!|?|@|#|$|%|¬|&|\\[|\\]|º|ª|\\|]+[a-zA-Z0-9_]*(?=[\"\'\\+\\-\\*\\/;,\\s[<>]?=[<?]>[<>](){a-zA-Z]|$))", "O identificador está mal formado");
	
	public final String pattern;
	public final String message;
	
	private TokenType(String pattern, String message){
		
		this.pattern = pattern;
		this.message = message;
	}
}

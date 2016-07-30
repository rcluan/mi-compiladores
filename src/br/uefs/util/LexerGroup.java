package br.uefs.util;

/**
 * Should have both well and bad-formatted tokens' pattern
 *
 */
public enum LexerGroup {
	
	PALAVRARESERVADA("\\b(programa|const|var|funcao|inicio|fim|se|entao|"
			+ "senao|enquanto|faca|leia|escreva|inteiro|real|booleano|"
			+ "verdadeiro|falso|cadeia|caractere)\\b", ""),
	OPERADORLOGICO("\\b(nao|e|ou)\\b", ""),
	IDENTIFICADOR("((?<=[\"\'\\+\\-\\*\\/;,\\s[<>]?=[<?]>[<>](){0-9]|^)[a-zA-Z][a-zA-Z0-9_]*(?=[\\.\"\'\\+\\-\\*\\/;,\\s[<>]?=[<?]>[<>](){0-9]|$))", ""),
	NUMERO("((?<=[\"\'\\+\\-\\*\\/;,\\s[<>]?=[<?]>[<>](){a-zA-Z]|^)[0-9]+([.][0-9]+)?(?=[\"\'\\+\\-\\*\\/;,\\s[<>]?=[<?]>[<>](){a-zA-Z]|$))", ""),
	COMENTARIO("\\{(.|[\r\n])*?\\}", ""),
	OPERADORARITMETICO("\\+|\\-|\\*|\\/", ""),
	OPERADORRELACIONAL("[<>]?=|[<?]>|[<>]", ""),
	DELIMITADOR("\\;|\\,|\\(|\\)", ""),
	CADEIA("\"[a-zA-Z][a-zA-Z0-9 ]*\"", ""),
	CARACTERE("\'([a-z]|[A-Z]|[0-9])\'", ""),
	QUEBRALINHA("\\r\\n", ""),
	
	VALORINESPERADO("([\\}|.|!|:|^|~|�|`|_|!|?|@|#|$|%|�|&|\\[|\\]|�|�|\\|]+(.*?)(?=[\"\'\\+\\-\\*\\/;,\\s[<>]?=[<?]>[<>](){]|$))","Valor inesperado"),
	
	COMENTARIOMALFORMADO("\\{(.|[\r\n])*", "O coment�rio est� mal formado"),
	CADEIAMALFORMADA("\"([^a-zA-Z])*(.*?)\"", "A cadeia possui caracteres inv�lidos ou est� vazia"),
	CADEIANAOFECHADA("(\"(.*?)(?=[\n]|$))", "A cadeia de caracteres n�o foi fechada"),
	
	CARACTEREMUITOGRANDE("\'([a-z]|[A-Z]|[0-9])+\'", "O caractere possui valores permitidos mas seu comprimento � inv�lido"),
	CARACTEREVAZIO("\'( )*\'", "O caractere n�o pode ser vazio"),
	CARACTEREINVALIDO("\'([+|-|*|/|.|!|:|^|~|�|`|_|!|?|@|#|$|%|�|&|\\[|\\]|�|�|\\|])(.*?)\'", "O caractere possui valores inv�lidos"),
	CARACTERENAOFECHADO("\'([a-zA-Z]|[0-9](?=[\"\\+\\-\\*\\/;,\\s[<>]?=[<?]>[<>](){]|$))", "O caractere n�o foi encerrado com aspas simples"),
	
	NUMEROMALFORMADO("([0-9]*\\.[.|!|:|^|~|�|`|_|!|?|@|#|$|%|�|&|\\[|\\]|�|�|\\|a-zA-Z0-9]*(?=[\"\'\\+\\-\\*\\/;,\\s[<>]?=[<?]>[<>](){a-zA-Z]|$))", "O n�mero est� mal formado"),
	
	IDENTIFICADORMALFORMADO("([a-zA-Z][.|!|:|^|~|�|`|!|?|@|#|$|%|�|&|\\[|\\]|�|�|\\|a-zA-Z0-9_]+(?=[\"\'\\+\\-\\*\\/;,\\s[<>]?=[<?]>[<>](){a-zA-Z]|$))", "O identificador est� mal formado");
	
	public final String pattern;
	public final String message;
	
	private LexerGroup(String pattern, String message){
		
		this.pattern = pattern;
		this.message = message;
	}
}

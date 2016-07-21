package br.uefs.util;

/**
 * Should have both well and bad-formatted tokens' pattern
 *
 */
public enum TokenType {
	
	PALAVRARESERVADA("\\b(programa|const|var|funcao|inicio|fim|se|entao|"
			+ "senao|enquanto|faca|leia|escreva|inteiro|real|booleano|"
			+ "verdadeiro|falso|cadeia|caractere)\\b"),
	NUMERO("[0-9]+(\\.[0-9]+)?"),
	COMENTARIO("\\{\\s*(.*?)\\s*\\}");
	
	public final String pattern;
	
	private TokenType(String pattern){
		
		this.pattern = pattern;
	}
}

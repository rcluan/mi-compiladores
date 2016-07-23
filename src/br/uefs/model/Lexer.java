package br.uefs.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.uefs.util.TokenType;

public class Lexer {
	
	private List<Token> tokens;
	
	public Lexer(){
		
		this.setTokens(new ArrayList<>());
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}
	
	/**
	 * It identifies a word that belongs to a group (e.g. PALAVRARESERVADA)
	 * by matching the word and the group's pattern (i.e., its regex). Therefore,
	 * for each token, it needs to create a named group associating a token type pattern 
	 * to its name in the following format (?<TYPE>PATTERN>).
	 * 
	 * The source code is represented in its full-length by the parameter input.
	 * 
	 * If a well-formatted value is identified, a token <TYPE, VALUE> is produced.
	 * 
	 * If by any chance the matcher finds a bad-formatted value when analysing the input,
	 * then the erroneous value is identified by a token <TYPE, VALUE, LINE, COLUMN>. Afterwards,
	 * the input should be split and the analysis should restart disregard the previous error. This
	 * is due so the Lexer can identify as many correct token as possible.
	 */
	public void regexAnalyse(String input){
		
		StringBuffer patternBuffer = new StringBuffer();
		
		for(TokenType type : TokenType.values()){
			
			patternBuffer.append(String.format("|(?<%s>%s)", type.name(), type.pattern));
		}
		
		Pattern tokenPatterns = Pattern.compile(new String(patternBuffer.substring(1)));
		
		Matcher matcher = tokenPatterns.matcher(input);
		
		while(matcher.find()){
			
			if(matcher.group(TokenType.PALAVRARESERVADA.name()) != null){
				
				tokens.add(new Token(TokenType.PALAVRARESERVADA, matcher.group(TokenType.PALAVRARESERVADA.name())));
			}
			if(matcher.group(TokenType.IDENTIFICADOR.name()) != null){
				
				tokens.add(new Token(TokenType.IDENTIFICADOR, matcher.group(TokenType.IDENTIFICADOR.name())));
			}
			if(matcher.group(TokenType.NUMERO.name()) != null){
				
				tokens.add(new Token(TokenType.NUMERO, matcher.group(TokenType.NUMERO.name())));
			}
			if(matcher.group(TokenType.DELIMITADOR.name()) != null){
				
				tokens.add(new Token(TokenType.DELIMITADOR, matcher.group(TokenType.DELIMITADOR.name())));
			}
			if(matcher.group(TokenType.CADEIA.name()) != null){
				
				tokens.add(new Token(TokenType.CADEIA, matcher.group(TokenType.CADEIA.name())));
			}
			if(matcher.group(TokenType.CARACTERE.name()) != null){
				
				tokens.add(new Token(TokenType.CARACTERE, matcher.group(TokenType.CARACTERE.name())));
			}
			if(matcher.group(TokenType.OPERADORARITMETICO.name()) != null){
				
				tokens.add(new Token(TokenType.OPERADORARITMETICO, matcher.group(TokenType.OPERADORARITMETICO.name())));
			}
			if(matcher.group(TokenType.OPERADORLOGICO.name()) != null){
				
				tokens.add(new Token(TokenType.OPERADORLOGICO, matcher.group(TokenType.OPERADORLOGICO.name())));
			}
			if(matcher.group(TokenType.OPERADORRELACIONAL.name()) != null){
				
				tokens.add(new Token(TokenType.OPERADORRELACIONAL, matcher.group(TokenType.OPERADORRELACIONAL.name())));
			}
			
			if(matcher.group(TokenType.CADEIAMALFORMADA.name()) != null){
				
				tokens.add(new Token(TokenType.CADEIAMALFORMADA, TokenType.CADEIAMALFORMADA.message, 0, matcher.start()));
			}
			if(matcher.group(TokenType.CADEIANAOFECHADA.name()) != null){
				
				tokens.add(new Token(TokenType.CADEIANAOFECHADA, TokenType.CADEIANAOFECHADA.message, 0, matcher.start()));
			}
			if(matcher.group(TokenType.VALORINESPERADO.name()) != null){
				
				tokens.add(new Token(TokenType.VALORINESPERADO, TokenType.VALORINESPERADO.message, 0, matcher.start()));
			}
			if(matcher.group(TokenType.CARACTEREMALFORMADO.name()) != null){
				
				tokens.add(new Token(TokenType.CARACTEREMALFORMADO, TokenType.CARACTEREMALFORMADO.message, 0, matcher.start()));
			}
		}
	}
}

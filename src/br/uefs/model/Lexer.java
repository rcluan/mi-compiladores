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
		
		// 
		for(TokenType type : TokenType.values()){
			
			patternBuffer.append(String.format("|(?<%s>%s)", type.name(), type.pattern));
		}
		
		Pattern tokenPatterns = Pattern.compile(new String(patternBuffer.substring(1)));
		
		Matcher matcher = tokenPatterns.matcher(input);
		
		while(matcher.find()){
			
			if(matcher.group(TokenType.PALAVRARESERVADA.name()) != null){
				
				tokens.add(new Token(TokenType.PALAVRARESERVADA, matcher.group(TokenType.PALAVRARESERVADA.name())));
			}
		}
	}
}

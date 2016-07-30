package br.uefs.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.uefs.util.LexerGroup;

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
	public void regexAnalyse(PreprocessedInput preprocessedInput){
		
		StringBuffer patternBuffer = new StringBuffer();
		
		for(LexerGroup type : LexerGroup.values()){
			
			patternBuffer.append(String.format("|(?<%s>%s)", type.name(), type.pattern));
		}
		
		Pattern tokenPatterns = Pattern.compile(new String(patternBuffer.substring(1)));
		
		String[] contents = preprocessedInput.getContent().split(LexerGroup.QUEBRALINHA.pattern);
		
		for(String input : contents){
			
			Matcher matcher = tokenPatterns.matcher(input);
			
			while(matcher.find()){
				
				if(matcher.group(LexerGroup.PALAVRARESERVADA.name()) != null){
					
					tokens.add(new Token(LexerGroup.PALAVRARESERVADA, matcher.group(LexerGroup.PALAVRARESERVADA.name())));
				}
				if(matcher.group(LexerGroup.IDENTIFICADOR.name()) != null){
					
					tokens.add(new Token(LexerGroup.IDENTIFICADOR, matcher.group(LexerGroup.IDENTIFICADOR.name())));
				}
				if(matcher.group(LexerGroup.NUMERO.name()) != null){
					
					tokens.add(new Token(LexerGroup.NUMERO, matcher.group(LexerGroup.NUMERO.name())));
				}
				if(matcher.group(LexerGroup.DELIMITADOR.name()) != null){
					
					tokens.add(new Token(LexerGroup.DELIMITADOR, matcher.group(LexerGroup.DELIMITADOR.name())));
				}
				if(matcher.group(LexerGroup.CADEIA.name()) != null){
					
					tokens.add(new Token(LexerGroup.CADEIA, matcher.group(LexerGroup.CADEIA.name())));
				}
				if(matcher.group(LexerGroup.CARACTERE.name()) != null){
					
					tokens.add(new Token(LexerGroup.CARACTERE, matcher.group(LexerGroup.CARACTERE.name())));
				}
				if(matcher.group(LexerGroup.OPERADORARITMETICO.name()) != null){
					
					tokens.add(new Token(LexerGroup.OPERADORARITMETICO, matcher.group(LexerGroup.OPERADORARITMETICO.name())));
				}
				if(matcher.group(LexerGroup.OPERADORLOGICO.name()) != null){
					
					tokens.add(new Token(LexerGroup.OPERADORLOGICO, matcher.group(LexerGroup.OPERADORLOGICO.name())));
				}
				if(matcher.group(LexerGroup.OPERADORRELACIONAL.name()) != null){
					
					tokens.add(new Token(LexerGroup.OPERADORRELACIONAL, matcher.group(LexerGroup.OPERADORRELACIONAL.name())));
				}
				
				if(matcher.group(LexerGroup.CADEIAMALFORMADA.name()) != null){
					
					String pattern = matcher.group(LexerGroup.CADEIAMALFORMADA.name());
					tokens.add(new Token(LexerGroup.CADEIAMALFORMADA, LexerGroup.CADEIAMALFORMADA.message, preprocessedInput.getOriginalLineNumber(pattern), matcher.start()));
				}
				if(matcher.group(LexerGroup.CADEIANAOFECHADA.name()) != null){
					
					String pattern = matcher.group(LexerGroup.CADEIANAOFECHADA.name());
					tokens.add(new Token(LexerGroup.CADEIANAOFECHADA, LexerGroup.CADEIANAOFECHADA.message, preprocessedInput.getOriginalLineNumber(pattern), matcher.start()));
				}
				if(matcher.group(LexerGroup.VALORINESPERADO.name()) != null){
					
					String pattern = matcher.group(LexerGroup.VALORINESPERADO.name());
					tokens.add(new Token(LexerGroup.VALORINESPERADO, LexerGroup.VALORINESPERADO.message, preprocessedInput.getOriginalLineNumber(pattern), matcher.start()));
				}
				if(matcher.group(LexerGroup.CARACTEREMUITOGRANDE.name()) != null){
					
					String pattern = matcher.group(LexerGroup.CARACTEREMUITOGRANDE.name());
					tokens.add(new Token(LexerGroup.CARACTEREMUITOGRANDE, LexerGroup.CARACTEREMUITOGRANDE.message, preprocessedInput.getOriginalLineNumber(pattern), matcher.start()));
				}
				if(matcher.group(LexerGroup.CARACTEREINVALIDO.name()) != null){
					
					String pattern = matcher.group(LexerGroup.CARACTEREINVALIDO.name());
					tokens.add(new Token(LexerGroup.CARACTEREINVALIDO, LexerGroup.CARACTEREINVALIDO.message, preprocessedInput.getOriginalLineNumber(pattern), matcher.start()));
				}
				if(matcher.group(LexerGroup.CARACTEREVAZIO.name()) != null){
					
					String pattern = matcher.group(LexerGroup.CARACTEREVAZIO.name());
					tokens.add(new Token(LexerGroup.CARACTEREVAZIO, LexerGroup.CARACTEREVAZIO.message, preprocessedInput.getOriginalLineNumber(pattern), matcher.start()));
				}
				if(matcher.group(LexerGroup.CARACTERENAOFECHADO.name()) != null){
					
					String pattern = matcher.group(LexerGroup.CARACTERENAOFECHADO.name());
					tokens.add(new Token(LexerGroup.CARACTERENAOFECHADO, LexerGroup.CARACTERENAOFECHADO.message, preprocessedInput.getOriginalLineNumber(pattern), matcher.start()));
				}
				if(matcher.group(LexerGroup.NUMEROMALFORMADO.name()) != null){
					
					String pattern = matcher.group(LexerGroup.NUMEROMALFORMADO.name());
					tokens.add(new Token(LexerGroup.NUMEROMALFORMADO, LexerGroup.NUMEROMALFORMADO.message, preprocessedInput.getOriginalLineNumber(pattern), matcher.start()));
				}
				if(matcher.group(LexerGroup.IDENTIFICADORMALFORMADO.name()) != null){
					
					String pattern = matcher.group(LexerGroup.IDENTIFICADORMALFORMADO.name());
					tokens.add(new Token(LexerGroup.IDENTIFICADORMALFORMADO, LexerGroup.IDENTIFICADORMALFORMADO.message, preprocessedInput.getOriginalLineNumber(pattern), matcher.start()));
				}
			}
		}

		
	}
}

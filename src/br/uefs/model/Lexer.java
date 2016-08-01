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
	 * 
	 * If a well-formatted value is identified, a token x [TYPE, VALUE] is produced, where
	 * x is the line number.
	 * 
	 * If by any chance the matcher finds a bad-formatted value when analysing the input,
	 * then the erroneous value is identified by a token x:y [TYPE, VALUE MSG], where x is
	 * the line number, y the column number and MSG a error message for the given TYPE.
	 * 
	 * @param preprocessedInput the preprocessed input containing both the original file content and the file content without commentaries
	 */
	public void regexAnalyse(PreprocessedInput preprocessedInput){
		
		StringBuffer patternBuffer = new StringBuffer();
		
		for(LexerGroup type : LexerGroup.values()){
			
			patternBuffer.append(String.format("|(?<%s>%s)", type.name(), type.pattern));
		}
		
		Pattern tokenPatterns = Pattern.compile(new String(patternBuffer.substring(1)));
		
		String[] contents = preprocessedInput.getContent().split(LexerGroup.QUEBRALINHA.pattern);
		int currentLine = 0, originalLine = 0;
		
		for(String input : contents){
			
			Matcher matcher = tokenPatterns.matcher(input);
			
			currentLine = originalLine;
			
			while(matcher.find()){
				
				if(matcher.group(LexerGroup.COMENTARIOMALFORMADO.name()) != null){
					
					String pattern = matcher.group(LexerGroup.COMENTARIOMALFORMADO.name()),
							badFormattedComment = "";
					
					for(String string : preprocessedInput.getOriginalContent().subList(originalLine, preprocessedInput.getOriginalContent().size())){
						
						badFormattedComment += string;
					}
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					
					tokens.add(new Token(LexerGroup.COMENTARIOMALFORMADO, badFormattedComment + " " + LexerGroup.COMENTARIOMALFORMADO.message, originalLine, matcher.start()));
					
					return;
				}
				if(matcher.group(LexerGroup.PALAVRARESERVADA.name()) != null){

					String pattern = matcher.group(LexerGroup.PALAVRARESERVADA.name());
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					
					tokens.add(new Token(LexerGroup.PALAVRARESERVADA, matcher.group(LexerGroup.PALAVRARESERVADA.name()), originalLine));
				}
				if(matcher.group(LexerGroup.IDENTIFICADOR.name()) != null){

					String pattern = matcher.group(LexerGroup.IDENTIFICADOR.name());
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					tokens.add(new Token(LexerGroup.IDENTIFICADOR, matcher.group(LexerGroup.IDENTIFICADOR.name()), originalLine));
				}
				if(matcher.group(LexerGroup.NUMERO.name()) != null){

					String pattern = matcher.group(LexerGroup.NUMERO.name());
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					tokens.add(new Token(LexerGroup.NUMERO, matcher.group(LexerGroup.NUMERO.name()), originalLine));
				}
				if(matcher.group(LexerGroup.DELIMITADOR.name()) != null){

					String pattern = matcher.group(LexerGroup.DELIMITADOR.name());
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					tokens.add(new Token(LexerGroup.DELIMITADOR, matcher.group(LexerGroup.DELIMITADOR.name()), originalLine));
				}
				if(matcher.group(LexerGroup.CADEIA.name()) != null){

					String pattern = matcher.group(LexerGroup.CADEIA.name());
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					tokens.add(new Token(LexerGroup.CADEIA, matcher.group(LexerGroup.CADEIA.name()), originalLine));
				}
				if(matcher.group(LexerGroup.CARACTERE.name()) != null){

					String pattern = matcher.group(LexerGroup.CARACTERE.name());
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					tokens.add(new Token(LexerGroup.CARACTERE, matcher.group(LexerGroup.CARACTERE.name()), originalLine));
				}
				if(matcher.group(LexerGroup.OPERADORARITMETICO.name()) != null){

					String pattern = matcher.group(LexerGroup.OPERADORARITMETICO.name());
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					tokens.add(new Token(LexerGroup.OPERADORARITMETICO, matcher.group(LexerGroup.OPERADORARITMETICO.name()), originalLine));
				}
				if(matcher.group(LexerGroup.OPERADORLOGICO.name()) != null){

					String pattern = matcher.group(LexerGroup.OPERADORLOGICO.name());
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					tokens.add(new Token(LexerGroup.OPERADORLOGICO, matcher.group(LexerGroup.OPERADORLOGICO.name()), originalLine));
				}
				if(matcher.group(LexerGroup.OPERADORRELACIONAL.name()) != null){

					String pattern = matcher.group(LexerGroup.OPERADORRELACIONAL.name());
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					tokens.add(new Token(LexerGroup.OPERADORRELACIONAL, matcher.group(LexerGroup.OPERADORRELACIONAL.name()), originalLine));
				}
				
				if(matcher.group(LexerGroup.CADEIAMALFORMADA.name()) != null){
					
					String pattern = matcher.group(LexerGroup.CADEIAMALFORMADA.name());
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					tokens.add(new Token(LexerGroup.CADEIAMALFORMADA, pattern + " " + LexerGroup.CADEIAMALFORMADA.message, originalLine, matcher.start()));
				}
				if(matcher.group(LexerGroup.CADEIANAOFECHADA.name()) != null){
					
					String pattern = matcher.group(LexerGroup.CADEIANAOFECHADA.name());
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					tokens.add(new Token(LexerGroup.CADEIANAOFECHADA, pattern + " " + LexerGroup.CADEIANAOFECHADA.message, originalLine, matcher.start()));
				}
				if(matcher.group(LexerGroup.VALORINESPERADO.name()) != null){
					
					String pattern = matcher.group(LexerGroup.VALORINESPERADO.name());
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					tokens.add(new Token(LexerGroup.VALORINESPERADO, pattern + " " + LexerGroup.VALORINESPERADO.message, originalLine, matcher.start()));
				}
				if(matcher.group(LexerGroup.CARACTEREMUITOGRANDE.name()) != null){
					
					String pattern = matcher.group(LexerGroup.CARACTEREMUITOGRANDE.name());
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					tokens.add(new Token(LexerGroup.CARACTEREMUITOGRANDE, pattern + " " + LexerGroup.CARACTEREMUITOGRANDE.message, originalLine, matcher.start()));
				}
				if(matcher.group(LexerGroup.CARACTEREINVALIDO.name()) != null){
					
					String pattern = matcher.group(LexerGroup.CARACTEREINVALIDO.name());
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					tokens.add(new Token(LexerGroup.CARACTEREINVALIDO, pattern + " " + LexerGroup.CARACTEREINVALIDO.message, originalLine, matcher.start()));
				}
				if(matcher.group(LexerGroup.CARACTEREVAZIO.name()) != null){
					
					String pattern = matcher.group(LexerGroup.CARACTEREVAZIO.name());
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					tokens.add(new Token(LexerGroup.CARACTEREVAZIO, pattern + " " + LexerGroup.CARACTEREVAZIO.message, originalLine, matcher.start()));
				}
				if(matcher.group(LexerGroup.CARACTERENAOFECHADO.name()) != null){
					
					String pattern = matcher.group(LexerGroup.CARACTERENAOFECHADO.name());
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					tokens.add(new Token(LexerGroup.CARACTERENAOFECHADO, pattern + " " + LexerGroup.CARACTERENAOFECHADO.message, originalLine, matcher.start()));
				}
				if(matcher.group(LexerGroup.NUMEROMALFORMADO.name()) != null){
					
					String pattern = matcher.group(LexerGroup.NUMEROMALFORMADO.name());
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					tokens.add(new Token(LexerGroup.NUMEROMALFORMADO, pattern + " " + LexerGroup.NUMEROMALFORMADO.message, originalLine, matcher.start()));
				}
				if(matcher.group(LexerGroup.IDENTIFICADORMALFORMADO.name()) != null){
					
					String pattern = matcher.group(LexerGroup.IDENTIFICADORMALFORMADO.name());
					originalLine = preprocessedInput.getOriginalLineNumber(currentLine, pattern);
					tokens.add(new Token(LexerGroup.IDENTIFICADORMALFORMADO, pattern + " " + LexerGroup.IDENTIFICADORMALFORMADO.message, originalLine, matcher.start()));
				}
			}
			
			currentLine++;
		}

		
	}
}

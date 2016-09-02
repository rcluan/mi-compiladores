package br.uefs.model;

import java.util.Iterator;

public class ParserRules {
	
	private Iterator<Token> tokens;
	private Token currentToken;
	
	public ParserRules(Iterator<Token> tokens) {
		
		this.setTokens(tokens);
	}

	public void constants(){
		//<constantes> ::=  'const'  'inicio' <bloco_constantes>  'fim'  | <lambda>
		parseTerminals("const", "inicio");
		nextToken();
		constantsBlock();
		nextToken();
		parseTerminal("fim");
	}
	
	public void constantsBlock(){
		//<bloco_constantes> ::=  <tipo_variavel> <declaracao_const> | <lambda>
		
		//algum parser pra primirtivo
		constantsDeclarate();
		
	}
	
	public void constantsDeclarate(){
		
		
	}
	
	public void globalVariable(){
		
		//TODO belongs to file rule
	}
	
	public void program(){
		
		//TODO belongs to file rule
	}
	
	public void functions(){
		
		//TODO belongs to file rule
	}
	

	/* leia(a,b);
	-> readCmd = leia( //calls readCmdContent
	-> readCmdContent = a //calls readCmdContentList
	-> readCmdContentList = , //calls readCmdContent
	-> readCmdContent = b //calls readCmdContentList
	-> readCmdContentList = ) //returns to second readCmdContent on stack
	-> readCmdContent = //returns to first readCmdContentList on stack
	-> readCmdContentList = //returns to first readCmdContent
	-> readCmdContent = //return to readCmd
	-> readCmd = ; //ends parsing
	*/
	
	public void readCmd(){
		
		switch(currentToken.getValue()){
			
			case "leia":
				nextToken();
				switch(currentToken.getValue()){
				
					case "(":
						nextToken();
						readCmdContent();
						
						semicolon();
						break;
					default:
						System.err.println("Esperado delimitador '(', mas obteve " + currentToken.getValue());
				}
			break;
			default:
				System.err.println("Esperado comando 'leia', mas obteve " + currentToken.getValue()); // produce generic err
				
		}
	}

	public void readCmdContent(){
		
		switch(currentToken.getType()){
		
			case IDENTIFICADOR:
				nextToken();
				readCmdContentList();
				return;
			default:
				break;
		}
	}
	
	public void readCmdContentList(){
		
		switch(currentToken.getValue()){
		
			case ",":
				nextToken();
				readCmdContent();
				return;
			case ")":
				nextToken();
				return;
			default:
				break;
		}
	}
	
	public void assign(){
		
		//TODO assignment without array
		switch(currentToken.getType()){
		
			case IDENTIFICADOR:
				nextToken();
				switch(currentToken.getValue()){
				
					case "=":
						nextToken();
						assignValue();
						
						semicolon();
						break;
				}
			default:
				break;
		}
	}
	
	public void assignValue(){
		
		switch(currentToken.getType()){
		
			case CADEIA:
				nextToken();
				semicolon();
				return;
			case CARACTERE:
				nextToken();
				semicolon();
				return;
			default:
				
				//checks whether is arithmetic expression or array
				// if none of them, throws error
				break;
		}
	}
	
	public void block(){
		
		switch(currentToken.getValue()){
		
			case "inicio":
				
				blockContent();
				break;
		}
	}
	
	public void blockContent(){
		
		lookahead();
		// TODO look next token to determine which rule should be followed
	}
	

	
	public void function(){
		
		switch(currentToken.getValue()){
		
			case "funcao":
				nextToken();
				
				switch(currentToken.getType()){
				
					case IDENTIFICADOR:
						nextToken();
						functionParam();
						break;
					default:
						break;
				}
		}
	}
	
	private void functionParam(){
		
		switch(currentToken.getValue()){
		
			case "(":
				nextToken();
				functionParamContent();
				
				block();
				return;
			default:
				break;
		}
	}
	
	public void functionParamContent(){
		
		if(hasType()){
			
			nextToken();
			switch(currentToken.getType()){
				
				case IDENTIFICADOR:
					nextToken();
					functionParamContentList();
					return;
				default:
					break;
			}
		}
	}
	
	private void functionParamContentList(){
		
		switch(currentToken.getValue()){
		
		case ",":
			nextToken();
			functionParamContent();
			return;
		case ")":
			nextToken();
			return;
		default:
			break;
	}
	}
	
	private boolean hasType(){
		
		if(currentToken.getValue() == "inteiro" || currentToken.getValue() == "real" ||
				currentToken.getValue() == "boolean" || currentToken.getValue() == "cadeia" ||
				currentToken.getValue() == "caractere")
			return true;
		
		return false;
	}
	
	private void parseTerminals(String... terminals) {
		for (String terminal : terminals ) {
			this.parseTerminal(terminal);
			nextToken();
		}
	}
	
	private void parseTerminal(String terminal) {
		System.out.println("Parsing " + terminal);
		Token current = this.getCurrentToken();
		if(!current.is(terminal)) {
			System.out.println("Esperava" + terminal + " mas veio " + current.getValue());
		};
	}
	
	private void semicolon(){
		
		switch(currentToken.getValue()){
			case ";":
				break;
			default:
				break;
		}
	}
	
	private void lookahead(){

		//TODO implement lookahead
		Iterator<Token> tokensCopy = tokens;
		
		
	}
	
	private void nextToken(){
		
		if(tokens.hasNext())
			currentToken = tokens.next();
	}

	public Iterator<Token> getTokens() {
		return tokens;
	}

	public void setTokens(Iterator<Token> tokens) {
		this.tokens = tokens;
	}

	public Token getCurrentToken() {
		return currentToken;
	}

	public void setCurrentToken(Token currentToken) {
		this.currentToken = currentToken;
	}
}

package br.uefs.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import br.uefs.util.LexerGroup;

public class Parser {

	private List<Token> tokens;
	private Iterator<Token> tokensIt;
	
	private Queue<String> parentheses;

	private Token currentToken;
	private int currentTokenId;

	private List<String> syntacticErrors;

	public Parser(List<Token> tokens) {

		this.setTokens(tokens);
		
		this.setTokensIt(tokens.iterator());
		
		this.setCurrentTokenId(-1);
		
		this.setSyntacticErrors(new ArrayList<>());
		
		this.setParentheses(new PriorityQueue<>());
		
		nextToken();
	}
	
	public void parse() {

		if(hasProgram(tokens)){
			
			parseLL();
		}else{
			
			syntacticErrors.add("O código não possui o trecho 'programa'");
		}
	}
	
	private void parseLL(){

		switch (currentToken.getValue()) {
		
		case "const":
			
			nextToken();
			declareConst();
			parseLL();
			return;
		case "var":
			
			nextToken();
			declareVar();
			parseLL();
			return;
		case "programa":
			
			nextToken();
			block();
			
			return;
		case "funcao":
			
			nextToken();
			return;
		default:
			return;
		}
	}

	private void block() {
		
		terminal("inicio");
		blockContent();
		terminal("fim");
		
	}
	private void blockContent() {
		
		switch(currentToken.getValue()){
		
		case "var":
			
			nextToken();
			declareVar();
			blockContent();
			return;
		case "se":
			
			nextToken();
			ifCommand();
			return;
		case "enquanto":
			
			nextToken();
			whileCommand();
			return;
		case "escreva":
			
			nextToken();
			writeCommand();
			return;
		case "leia":
			
			nextToken();
			readCommand();
			return;
		default:
			
			if(hasProgramEnd()){
				
				return;
			}

			isIdBlockContent();
			blockContent();
		}
		
	}
	
	private boolean hasProgramEnd(){
		
		if(currentToken.getValue().equals("fim"))
			return true;
		
		return false;
	}
	private void isIdBlockContent() {
		
		switch(currentToken.getType()){
		
		case IDENTIFICADOR:
			
			idBlockContentLookahead();
			return;
		default:
			
			int line = this.lookahead().getLine();
			String got = this.lookahead().getValue();
			
			syntacticErrors.add(buildErrorLog(line, "var", "se", "enquanto", "escreva", 
					"leia", LexerGroup.IDENTIFICADOR.name(), got));
			return;
		}
		
	}

	private void idBlockContentLookahead() {
		
		switch(this.lookahead().getValue()){
		
		case "(":
			
			openFunctionCall();
			terminal(")");
			terminal(";");
			
			return;
		default:
			
			nextToken();
			assignTerm();
			return;
		}
	}

	private void ifCommand() {
		// TODO Auto-generated method stub
		
	}

	private void whileCommand() {
		// TODO Auto-generated method stub
		
	}

	private void writeCommand() {
		// TODO Auto-generated method stub
		
	}

	private void readCommand() {
		// TODO Auto-generated method stub
		
	}

	private void functionCall() {
		// TODO Auto-generated method stub
		
	}

	private boolean hasProgram(List<Token> tokens) {
		
		for(Token token : tokens){
			
			if(token.getValue().equals("programa")){
				
				return true;
			}
		}
		
		return false;
	}
	private void declareVar() {
		
		if (type(currentToken)) {
			
			nextToken();
			
			switch(currentToken.getType()){
			
			case IDENTIFICADOR:
				
				varTerm();
				return;
			default:
				syntacticErrors.add(buildErrorLog(currentToken.getLine(), LexerGroup.IDENTIFICADOR.name(),
						currentToken.getType().name()));
				return;
			}
		}
	}

	private void varTerm() {
		
		switch(this.lookahead().getValue()){
		
		case "<":
			
			nextToken();
			array();
			
			varList();
			return;
		default:
			
			nextToken();
			varList();
			//syntacticErrors.add(buildErrorLog(currentToken.getLine(), "<", "=", currentToken.getValue()));
		}
	}

	private void varList() {
		
		switch(currentToken.getValue()){
		
		case ",":
			
			declareVar();
			return;
		case ";":
			
			nextToken();
			return;

		default:
			syntacticErrors.add(buildErrorLog(currentToken.getLine(), ",", ";", currentToken.getValue()));
			return;
		}
	}

	private void declareConst() {
		
		
		if (type(currentToken)) {
			
			nextToken();
			
			switch (currentToken.getType()) {

			case IDENTIFICADOR:
				
				constTerm();
				return;
			default:
				syntacticErrors.add(buildErrorLog(currentToken.getLine(), LexerGroup.IDENTIFICADOR.name(), 
						currentToken.getType().name()));
				return;
			}
		}else{
			
			syntacticErrors.add(buildErrorLog(currentToken.getLine(), "inteiro", "real", "cadeia", 
					"caractere", "booleano", currentToken.getValue()));
		}
	}

	private void constTerm() {
		
		switch(this.lookahead().getValue()){
		
		case "<":
			
			nextToken();
			array();
			terminal("=");
			
			assignValue();
			constList();
			return;
		case "=":
			
			nextToken();
			assignValue();
			constList();
			return;
		default:
			syntacticErrors.add(buildErrorLog(currentToken.getLine(), "<", "=", this.lookahead().getValue()));
		}
		
	}

	private void constList() {
		
		switch(currentToken.getValue()){
		
		case ",":
			
			declareConst();
			return;
		case ";":
			
			nextToken();
			return;
		}
		
	}

	private void terminal(String terminal) {
		
		if(currentToken.getValue().equals(terminal)){
			
			nextToken();
		}else{
			
			syntacticErrors.add(buildErrorLog(currentToken.getLine(), terminal, currentToken.getValue()));
		}
	}

	private void assignTerm() {
		
		switch(currentToken.getValue()){
		
		case "<":
			
			array();
			terminal("=");
			
			assignValue();
			terminal(";");
			return;
		case "=":

			assignValue();
			terminal(";");
			return;
		default:
			syntacticErrors.add(buildErrorLog(currentToken.getLine(), "<", "=", currentToken.getValue()));
		}
		
	}

	private void assignValue() {
		
		nextToken();

		switch(currentToken.getType()){
		
		case CADEIA:
			
			nextToken();
			return;
		case CARACTERE:
			
			nextToken();
			return;
		default:
			
			switch(currentToken.getValue()){
			
			case "<":
				
				array();
				return;
			case "verdadeiro":
				
				nextToken();
				return;
			case "falso":
				
				nextToken();
				return;
			
			default:
				
				//nextToken();
				aritExp();
				nextToken();
				return;
			}
		}
	}

	private void aritExp() {
		
		switch(currentToken.getValue()){
		
		case "(":
			
			parentheses.add("(");
			checkClosure();
			return;
		default:
			
			switch(currentToken.getType()){
			
			case IDENTIFICADOR:
				
				switch(lookahead().getValue()){
				
				case "(":

					openFunctionCall();
					return;
				default:
					
					aritExpHP();
					
					if(!parentheses.isEmpty()){
						
						checkClosure();
					}
					
					aritExpHP();
					return;
				}
			case NUMERO:
				
				aritExpHP();
				
				if(!parentheses.isEmpty()){
					
					checkClosure();
				}
				
				aritExpHP();
				return;
			default:
				
				syntacticErrors.add(buildErrorLog(currentToken.getLine(), LexerGroup.CADEIA.name(), LexerGroup.CARACTERE.name(), "(",
							  LexerGroup.IDENTIFICADOR.name(), LexerGroup.NUMERO.name(), currentToken.getValue()));
				return;
			}
		}
	}
	
	private void checkClosure() {

		switch(lookahead().getValue()){
		
		case ")":
			nextToken();
			parentheses.poll();
			return;
		default:
			nextToken();
			aritExp();
			return;
		}
	}

	private void aritExpHP(){
		
		switch(lookahead().getValue()){
		
		case "*":
			
			nextToken();
			nextToken();
			aritExp();
			return;
		case "/":
			
			nextToken();
			nextToken();
			aritExp();
			return;
		default:
			
			aritExpLP();
			return;
		}
	}
	private void aritExpLP() {
		
		switch(lookahead().getValue()){
		
		case "+":
			
			nextToken();
			nextToken();
			aritExp();
			return;
		case "-":
			
			nextToken();
			nextToken();
			aritExp();
			return;
		default:
			return;
		}
		
		
	}
	private void array() {
		
		nextToken();
		terminal("<");
		
		switch(currentToken.getType()){
		
		case IDENTIFICADOR:
			
			nextToken();
			arrayList();
			return;
		case NUMERO:
			
			nextToken();
			arrayList();
			return;
			
		default:
			syntacticErrors.add(buildErrorLog(currentToken.getLine(), LexerGroup.IDENTIFICADOR.name(),
					LexerGroup.NUMERO.name(), currentToken.getType().name()));
		}
	}


	private void arrayList() {
		
		switch(currentToken.getValue()){
		
		case ",":
			nextToken();
			array();
			return;
		case ">":
			
			nextToken();
			terminal(">");
			return;
		default:
			syntacticErrors.add(buildErrorLog(currentToken.getLine(), ",", ">", currentToken.getValue()));
		}
		
	}

	private void openFunctionCall() {
		
		nextToken();
		functionParamContent();
		
		if(!currentToken.getValue().equals(")"))
			nextToken();
		
		return;
	}
	
	private void functionParamContent(){
		
		aritExp();
		
		switch(lookahead().getValue()){
		
		case ",":

			nextToken();
			nextToken();
			functionParamContent();
			return;
		default:
			return;
		}
	}

	private String buildErrorLog(int line, String... names) {
		
		String msg = "Linha " + line+ " esperando ";
		for(int i = 0; i < names.length - 1; i++){
			
			msg += names[i] + ", ";
		}
		
		return msg += "mas obteve " + names[names.length-1];
	}

	private boolean type(Token token) {

		return (token.getValue().equals("inteiro") || token.getValue().equals("real")
				|| token.getValue().equals("cadeia") || token.getValue().equals("caractere")
				|| token.getValue().equals("booleano"));
	}

	private Token lookahead() {
		
		return tokens.get(currentTokenId+1);
	}

	private void  nextToken(){
		
		currentTokenId += 1;
		
		if(tokensIt.hasNext()){
			setCurrentToken(tokensIt.next());
		}
		else
			setCurrentToken(null);
	}

	public List<String> getSyntacticErrors() {
		return syntacticErrors;
	}

	public void setSyntacticErrors(List<String> syntacticErrors) {
		this.syntacticErrors = syntacticErrors;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	public Iterator<Token> getTokensIt() {
		return tokensIt;
	}
	public void setTokensIt(Iterator<Token> tokensIt) {
		this.tokensIt = tokensIt;
	}
	public Token getCurrentToken() {
		return currentToken;
	}

	public void setCurrentToken(Token currentToken) {

		this.currentToken = currentToken;
	}
	public int getCurrentTokenId() {
		return currentTokenId;
	}
	public void setCurrentTokenId(int currentTokenId) {
		this.currentTokenId = currentTokenId;
	}

	public Queue<String> getParentheses() {
		return parentheses;
	}

	public void setParentheses(Queue<String> parentheses) {
		this.parentheses = parentheses;
	}
}

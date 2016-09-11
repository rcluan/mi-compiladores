package br.uefs.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.uefs.util.LexerGroup;

public class Parser {

	private List<Token> tokens;
	private Iterator<Token> tokensIt;

	private Token currentToken;
	private int currentTokenId;

	private List<String> syntacticErrors;

	public Parser(List<Token> tokens) {

		this.setTokens(tokens);
		
		this.setTokensIt(tokens.iterator());
		
		this.setCurrentTokenId(-1);
		
		this.setSyntacticErrors(new ArrayList<>());
		
		nextToken();

		//parseLL(ParserRules.arquivo);
	}
/*
	public void parseLL(ParserRules rules) {

		if (rules.equals(ParserRules.arquivo)) {

		}

		for (String rule : rules.production) {

			flag = false;
			String productions[] = rule.split(" ");

			for (String production : productions) {

				if (notTerminal(production) && !flag) {

					parseLL(ParserRules.valueOf(production));
				} else if (tokens.get(currentToken).getValue().equals(production)) {

					System.out.println(production);
					;
					return;
				} else if (tokens.get(currentToken).getType().name().equals(production)) {

					System.out.println(production);
					;
					return;
				} else {

					flag = true;
				}
			}

		}
		return;
	}

	private boolean notTerminal(String production) {

		for (ParserRules rule : ParserRules.values()) {

			if (rule.name().equals(production))
				return true;
		}
		return false;
	}
*/
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
			return;
		case "funcao":
			
			nextToken();
			return;
		default:
			return;
		}
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
	
	private void assign(){
		
		switch (currentToken.getType()) {

		case IDENTIFICADOR:
			
			assignTerm();
			return;
		default:
			syntacticErrors.add(buildErrorLog(currentToken.getLine(), LexerGroup.IDENTIFICADOR.name(), 
					currentToken.getType().name()));
			return;
		}
	}

	private void assignTerm() {
		
		;
		
		switch(this.lookahead().getValue()){
		
		case "<":
			
			array();
			terminal("=");
			
			assignValue();
			terminal(";");
			return;
		case "=":
			
			nextToken();
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
				
				nextToken();
				aritExp();
				return;
			}
		}
	}

	private void aritExp() {
		
		switch(currentToken.getValue()){
		
		case "(":
			
			aritExp();
			terminal(")");
			return;
		default:
			
			switch(currentToken.getType()){
			
			case IDENTIFICADOR:
				switch(lookahead().getValue()){
				
				case "(":
					nextToken();
					openFunctionCall();
					return;
				default:
					
					aritExpHP();
					return;
				}
			case NUMERO:
				
				aritExpHP();
				return;
			default:
				
				syntacticErrors.add(buildErrorLog(currentToken.getLine(), LexerGroup.CADEIA.name(), LexerGroup.CARACTERE.name(), "(",
							  LexerGroup.IDENTIFICADOR.name(), LexerGroup.NUMERO.name(), currentToken.getValue()));
				return;
			}
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
		nextToken();
		terminal(")");
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
		setCurrentToken(tokensIt.next());
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
}

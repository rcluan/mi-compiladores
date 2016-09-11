package br.uefs.model;

import java.util.ArrayList;
import java.util.List;

import br.uefs.util.LexerGroup;

public class Parser {

	private List<Token> tokens;

	private boolean minCondition = false;
	private int currentToken;

	private List<String> syntacticErrors;

	public Parser(List<Token> tokens) {

		this.setTokens(tokens);
		this.setCurrentToken(0);
		this.setSyntacticErrors(new ArrayList<>());

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
					nextToken();
					return;
				} else if (tokens.get(currentToken).getType().name().equals(production)) {

					System.out.println(production);
					nextToken();
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

		Token token = tokens.get(currentToken);

		switch (token.getValue()) {

		case "const":

			declareConst();
			parse();
			return;
		case "var":
			
			declareVar();
			parse();
			return;
		case "programa":
			
			return;
		case "funcao":
			return;
		default:
			minCondition = false;
			return;
		}
	}

	private void declareVar() {
		
		Token token = nextToken();
		if (type(token)) {
			
			token = nextToken();
			
			switch(token.getType()){
			
			case IDENTIFICADOR:
				
				varTerm();
				return;
			default:
				syntacticErrors.add(buildErrorLog(LexerGroup.IDENTIFICADOR.name(), token.getType().name()));
				return;
			}
		}
	}

	private void varTerm() {
		
		Token token = nextToken();
		
		switch(this.lookahead().getValue()){
		
		case "<":
			
			array();
			
			varList();
			return;
		case "=":
			
			varList();
			return;
		default:
			syntacticErrors.add(buildErrorLog("<", "=", token.getValue()));
		}
	}

	private void varList() {
		
		Token token = tokens.get(currentToken);
		
		switch(token.getValue()){
		
		case ",":
			
			declareVar();
			return;
		case ";":
			
			nextToken();
			return;
		}
	}

	private void declareConst() {
		
		Token token = nextToken();
		if (type(token)) {
			
			token = nextToken();
			switch (token.getType()) {

			case IDENTIFICADOR:
				
				constTerm();
				return;
			default:
				syntacticErrors.add(buildErrorLog(LexerGroup.IDENTIFICADOR.name(), token.getType().name()));
				return;
			}
		}else{
			
			syntacticErrors.add(buildErrorLog("inteiro", "real", "cadeia", "caractere", "booleano", token.getValue()));
		}
	}

	private void constTerm() {
		
		Token token = nextToken();
		
		switch(this.lookahead().getValue()){
		
		case "<":
			
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
			syntacticErrors.add(buildErrorLog("<", "=", token.getValue()));
		}
		
	}

	private void constList() {
		
		Token token = tokens.get(currentToken);
		
		switch(token.getValue()){
		
		case ",":
			
			declareConst();
			return;
		case ";":
			
			nextToken();
			return;
		}
		
	}

	private void terminal(String terminal) {
		
		Token token = tokens.get(currentToken);
		
		if(token.getValue().equals(terminal)){
			
			nextToken();
		}else{
			
			syntacticErrors.add(buildErrorLog(terminal, token.getValue()));
		}
	}
	
	private void terminal(LexerGroup type){

		Token token = tokens.get(currentToken);
		
		if(token.getType().equals(type)){
			
			nextToken();
		}
	}
	
	private void assign(){
		
		Token token = tokens.get(currentToken);
		
		switch (token.getType()) {

		case IDENTIFICADOR:
			
			assignTerm();
			return;
		default:
			syntacticErrors.add(buildErrorLog(LexerGroup.IDENTIFICADOR.name(), token.getType().name()));
			return;
		}
	}

	private void assignTerm() {
		
		Token token = nextToken();
		
		switch(this.lookahead().getValue()){
		
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
			syntacticErrors.add(buildErrorLog("<", "=", token.getValue()));
		}
		
	}

	private void assignValue() {
		
		Token token = nextToken();
		
		
		
		return;
	}

	private void array() {
		
		terminal("<");
		
		Token token = tokens.get(currentToken);
		
		switch(token.getType()){
		
		case IDENTIFICADOR:
			arrayList();
			return;
		case NUMERO:
			arrayList();
			return;
			
		default:
			syntacticErrors.add(buildErrorLog(LexerGroup.IDENTIFICADOR.name(), LexerGroup.NUMERO.name(), token.getType().name()));
		}
	}


	private void arrayList() {
		
		Token token = nextToken();
		
		switch(token.getValue()){
		
		case ",":
			nextToken();
			array();
			return;
		case ">":
			
			terminal(">");
			return;
		default:
			syntacticErrors.add(buildErrorLog(",", ">", token.getValue()));
		}
		
	}

	private void openFunctionCall() {
		// TODO Auto-generated method stub
		
	}

	private String buildErrorLog(String... names) {
		
		String msg = "Esperando ";
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

		return (tokens.get(currentToken++));
	}

	private Token nextToken() {

		setCurrentToken(currentToken + 1);

		return tokens.get(currentToken);
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

	public int getCurrentToken() {
		return currentToken;
	}

	public void setCurrentToken(int currentToken) {

		this.currentToken = currentToken;
	}
}

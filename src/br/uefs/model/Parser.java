package br.uefs.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.uefs.util.LexerGroup;

public class Parser {

	private Iterator<Token> tokensIt;

	private Token currentToken;

	private List<String> syntacticErrors;

	public Parser(List<Token> tokens) {
		
		this.setTokensIt(tokens.iterator());
		
		this.setSyntacticErrors(new ArrayList<>());
		
		nextToken();
	}
	
	public void parse() {
		
		constant();
		variable();
		program();
	}

	private void constant() {
		
		switch (currentToken.getValue()) {
		
		case "const":
			
			nextToken();
			declareConst();
			terminal(";");
			constant();
		}
		
		return;
	}

	private void variable() {
		
		switch (currentToken.getValue()) {
		
		case "var":
			
			nextToken();
			declareVar();
			terminal(";");
			variable();
		}
		
		return;
	}

	private void program() {
		
		switch (currentToken.getValue()) {
		
		case "programa":
			
			nextToken();
			block();
			
			function();
			return;
		
		default:
			
			syntacticErrors.add(buildErrorLog(currentToken.getLine(), "programa", currentToken.getValue()));
			return;
		}
	}

	private void function() {
		
		if(currentToken != null){
			switch(currentToken.getValue()){
			
				case "funcao":
				
				nextToken();
				
				if(type(currentToken)){
	
					nextToken();
				}
	
				declareFunction();
				block();
				
				function();
				
				return;
			}
		}
		
		return;
	}

	private void declareFunction() {
		
		switch(currentToken.getType()){
		
		case IDENTIFICADOR:
			
			nextToken();
			terminal("(");
			declareFunctionParam();
			terminal(")");
			return;
		default:
			
			//TODO error expecting id
			return;
		}
	}

	private void declareFunctionParam() {
		
		if (type(currentToken)) {
			
			nextToken();
			
			switch(currentToken.getType()){
			
			case IDENTIFICADOR:
				
				nextToken();
				declareFunctParamList();
				return;
			default:
				
				return;
			}
		}
	}

	private void declareFunctParamList() {
		
		switch(currentToken.getValue()){
		
		case ",":
			
			nextToken();
			declareFunctionParam();
			return;
		case ")":
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
			terminal(";");
			blockContent();
			return;
		case "se":
			
			nextToken();
			terminal("(");
			
			logicExp();
			terminal(")");
			
			terminal("entao");
			block();
			
			elseCmd();
			
			blockContent();
			return;
		case "enquanto":
			
			nextToken();
			terminal("(");
			
			logicExp();
			terminal(")");
			
			terminal("faca");
			block();
			
			blockContent();
			return;
		case "escreva":
			
			nextToken();
			terminal("(");
			
			writeCmdContent();
			
			terminal(")");
			terminal(";");
			
			blockContent();
			return;
		case "leia":
			
			nextToken();
			terminal("(");
			
			readCmdContent();
			
			terminal(")");
			terminal(";");
			
			blockContent();
			return;
		case "fim":
			return;
		default:
			
			isIdBlockContent();
			blockContent();
			
			return;
		}
		
	}
	
	private void logicExp() {
		
		switch(currentToken.getValue()){
		
		
		case "(":
			
			nextToken();
			logicExp();
			terminal(")");
			logicExpTerm();
			return;
		case "nao":
			
			nextToken();
			logicExp();
			return;
		default:
			
			logicExpRoot();
			return;
		}
	}
	
	
	private void logicExpRoot(){
		switch(currentToken.getType()){
		
		case IDENTIFICADOR:
			
			nextToken();
			relatExpTerm();
			logicExpTerm();
			return;
			
		case NUMERO:
			
			nextToken();
			relatExpTerm();
			logicExpTerm();
			return;
			
		default:
			
			int line = currentToken.getLine();
			String got = currentToken.getValue();
			syntacticErrors.add(buildErrorLog(line, LexerGroup.IDENTIFICADOR.name(), 
					LexerGroup.NUMERO.name(), "(", "nao", got));
			
		}
	}
	
	private void logicExpTerm(){
		
		switch(currentToken.getValue()){
		
		case "e":
			
			nextToken();
			logicExp();
			return;
		case "ou":
			
			nextToken();
			logicExp();
			return;
			
		default:
			return;
		}
	}
		
	private void relatExpTerm(){

		switch(currentToken.getValue()){

		case "=":
			nextToken();
			identifierOrNumber();
			return;

		case ">":
			nextToken();
			identifierOrNumber();
			return;
			
		case "<":
			
			nextToken();
			
			if(currentToken.getValue().equals("<")){
				
				array();
				relatExpTerm();
				return;
			}
			
			identifierOrNumber();
			return;

		case "<>":
			nextToken();
			identifierOrNumber();
			return;
			
		case ">=":
			nextToken();
			identifierOrNumber();
			return;

		case "<=":
			nextToken();
			identifierOrNumber();
			return;	
		}

	}
	
	private void identifierOrNumber(){
		
		switch(currentToken.getType()){

		case IDENTIFICADOR:
			
			nextToken();
			if(currentToken.getValue().equals("<")){
				
				nextToken();
				array();
			}
			return;
		case NUMERO:
			
			nextToken();
			return;
			
		default:
			
			int line = currentToken.getLine();
			String got = currentToken.getValue();
			
			syntacticErrors.add(buildErrorLog(line, LexerGroup.IDENTIFICADOR.name(), 
					LexerGroup.NUMERO.name(), got));
			
			return;
		}
	}

	private void elseCmd() {
		
		switch(currentToken.getValue()){
		
		case "senao":
			
			nextToken();
			block();
			
			return;
		default:
			return;
		}
	}
	
	private void isIdBlockContent() {
		
		switch(currentToken.getType()){
		
		case IDENTIFICADOR:
			
			nextToken();
			idBlockContentLookahead();
			terminal(";");
			return;
		default:
			
			int line = currentToken.getLine();
			String got = currentToken.getValue();
			
			syntacticErrors.add(buildErrorLog(line, "var", "se", "enquanto", "escreva", 
					"leia", LexerGroup.IDENTIFICADOR.name(), got));
			return;
		}
		
	}

	private void idBlockContentLookahead() {
		
		switch(currentToken.getValue()){
		
		case "(":
			
			nextToken();
			functionParamContent();

			terminal(")");
			return;
		case "<":
			
			nextToken();
			array();
			terminal("=");
			
			assignValue();
			return;
		case "=":
			
			nextToken();
			assignValue();
			return;
		default:
			
			int line = currentToken.getLine();
			String got = currentToken.getValue();
			syntacticErrors.add(buildErrorLog(line, "(", "<", "=", got));
			
			errorRecovery(new LexerGroup[]{}, new String[]{";"});
			return;
		}
	}

	private void writeCmdContent() {
		
		switch(currentToken.getType()){
		
		case CARACTERE:
			
			nextToken();
			writeCmdContentList();
			return;
		case CADEIA:
			
			nextToken();
			writeCmdContentList();
			return;
		default:
			
			aritExp();
			writeCmdContentList();
			return;
		}
	}

	private void writeCmdContentList() {
		
		switch(currentToken.getValue()){
		
		case ",":
			
			nextToken();
			writeCmdContent();
			return;
		case ")":
			
			return;
		default:
			
			int line =  currentToken.getLine();
			String got = currentToken.getValue();
			
			syntacticErrors.add(buildErrorLog(line, ",", ")", got));
			return;
		}
	}

	private void readCmdContent() {
		
		switch(currentToken.getType()){
		
		case IDENTIFICADOR:
			
			nextToken();
			readCmdContentList();
			return;
		default:

			int line = currentToken.getLine();
			String got = currentToken.getType().name();
			
			syntacticErrors.add(buildErrorLog(line, LexerGroup.IDENTIFICADOR.name(), got));
			return;
		}
	}

	private void readCmdContentList() {
		
		switch(currentToken.getValue()){
		
		case ",":
			
			nextToken();
			readCmdContent();
			return;
		case ")":
			
			return;
		default:
			
			int line = currentToken.getLine();
			String got = currentToken.getValue();
			
			syntacticErrors.add(buildErrorLog(line, ",", ")", got));
			return;
		}
	}
	
	private void declareVar() {
		
		if (type(currentToken)) {
			
			nextToken();
			declareVarContent();
			return;
		}else{
			
			syntacticErrors.add(buildErrorLog(currentToken.getLine(), "inteiro", "real", "cadeia", 
					"caractere", "booleano", currentToken.getValue()));

			errorRecovery(new LexerGroup[]{LexerGroup.IDENTIFICADOR}, new String[]{});
			return;
		}
	}

	private void declareVarContent() {
		
		switch(currentToken.getType()){
		
		case IDENTIFICADOR:
			
			nextToken();
			varTerm();
			return;
		default:
			
			String got = currentToken.getType().name() + ": " + currentToken.getValue();
			syntacticErrors.add(buildErrorLog(currentToken.getLine(), LexerGroup.IDENTIFICADOR.name(),
					got));
			
			errorRecovery(new LexerGroup[]{}, new String[]{",",";"});
			varList();
			
			return;
		}
	}

	private void varTerm() {
		
		switch(this.currentToken.getValue()){
		
		case "<":
			
			nextToken();
			array();
			
			varList();
			return;
		default:
			
			varList();
			return;
		}
	}

	private void varList() {
		
		switch(currentToken.getValue()){
		
		case ",":
			
			nextToken();
			declareVarContent();
			return;
		case ";":
			
			return;
		default:
			
			syntacticErrors.add(buildErrorLog(currentToken.getLine(), ",", ";", currentToken.getValue()));
			
			errorRecovery(new LexerGroup[]{LexerGroup.IDENTIFICADOR}, new String[]{";"});
			declareVarContent();
			return;
		}
	}

	private void declareConst() {
		
		
		if (type(currentToken)) {
			
			nextToken();
			declareConstContent();
			return;
		}else{
			
			syntacticErrors.add(buildErrorLog(currentToken.getLine(), "inteiro", "real", "cadeia", 
					"caractere", "booleano", currentToken.getValue()));
			
			//next expected token
			errorRecovery(new LexerGroup[]{LexerGroup.IDENTIFICADOR}, new String[]{});
			declareConstContent();
			return;
		}
	}

	private void declareConstContent() {
		

		switch (currentToken.getType()) {

		case IDENTIFICADOR:
			
			nextToken();
			constTerm();
			return;
		default:
			
			String got = currentToken.getType().name() + ": " + currentToken.getValue();
			syntacticErrors.add(buildErrorLog(currentToken.getLine(), LexerGroup.IDENTIFICADOR.name(), 
					got));
			
			errorRecovery(new LexerGroup[]{}, new String[]{",", ";"});
			constList();
			return;
		}
	}

	private void constTerm() {
		
		switch(currentToken.getValue()){
		
		case "<":
			
			nextToken();
			array();
			assignConstTerm();
			
			return;
		default:
			
			assignConstTerm();
			return;
		}
		
	}
	
	private void assignConstTerm() {
		
		switch(currentToken.getValue()){
		
		case "=":
			
			nextToken();
			assignValue();
			constList();
			return;
		default:
			
			syntacticErrors.add(buildErrorLog(currentToken.getLine(), "=", currentToken.getValue()));
			errorRecovery(new LexerGroup[]{}, new String[]{",", ";"});
			constList();
		}
	}

	private void constList() {
		
		switch(currentToken.getValue()){
		
		case ",":
			
			nextToken();
			declareConstContent();
			return;
		case ";":
			
			return;
		default:
			
			syntacticErrors.add(buildErrorLog(currentToken.getLine(), ",", ";", currentToken.getValue()));
			errorRecovery(new LexerGroup[]{LexerGroup.IDENTIFICADOR}, new String[]{";"});
			declareConstContent();
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

	private void assignValue() {
		
		
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
				
				nextToken();
				array();
				return;
			case "verdadeiro":
				
				nextToken();
				return;
			case "falso":
				
				nextToken();
				return;
			
			default:
				
				aritExp();
				return;
			}
		}
	}

	private void aritExp() {
		
		switch(currentToken.getValue()){
		
		case "(":
			
			nextToken();
			aritExp();
			terminal(")");
			aritExpHP();
			return;
		default:
			
			aritExpTerm();
			return;
		}
	}
	
	private void aritExpTerm() {
		

		switch(currentToken.getType()){
		
		case IDENTIFICADOR:
			
			nextToken();
			switch(currentToken.getValue()){
			
			case "<":
				
				nextToken();
				array();
				return;
			case "(":

				nextToken();
				
				functionParamContent();
				terminal(")");
				return;
			case ")":
				return;
			default:

				aritExpHP();
				return;
			}
		case NUMERO:
			
			nextToken();
			aritExpHP();
			
			return;
		default:
			
			syntacticErrors.add(buildErrorLog(currentToken.getLine(), LexerGroup.CADEIA.name(), LexerGroup.CARACTERE.name(), "(",
						  LexerGroup.IDENTIFICADOR.name(), LexerGroup.NUMERO.name(), currentToken.getValue()));
			return;
		}
	}

	private void aritExpHP(){
		
		switch(currentToken.getValue()){
		
		case "*":
			
			nextToken();
			aritExp();
			return;
		case "/":
			
			nextToken();
			aritExp();
			return;
		default:
			
			aritExpLP();
			return;
		}
	}
	private void aritExpLP() {
		
		switch(currentToken.getValue()){
		
		case "+":
			
			nextToken();
			aritExp();
			return;
		case "-":
			
			nextToken();
			aritExp();
			return;
		default:
			return;
		}
		
		
	}
	private void array() {
		
		terminal("<");
		arrayContent();
	}


	private void arrayContent() {
		

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
			arrayContent();
			return;
		case ">":
			
			nextToken();
			terminal(">");
			return;
		default:
			syntacticErrors.add(buildErrorLog(currentToken.getLine(), ",", ">", currentToken.getValue()));
		}
		
	}
	
	private void functionParamContent(){
		
		if(currentToken.getValue().equals(")"))
				return;
		
		aritExp();
		
		switch(currentToken.getValue()){
		
		case ",":

			nextToken();
			functionParamContent();
			return;
		}
	}
	
	private void errorRecovery(LexerGroup[] syncTokensType, String[] syncTokens){
		
		while(!(findSyncToken(syncTokens) || findSyncToken(syncTokensType)) && currentToken != null){
			
			nextToken();
		}
		
		return;
	}
	
	private boolean findSyncToken(String[] syncTokens) {
		
		for(String syncToken : syncTokens){
			
			if(currentToken.getValue().equals(syncToken))
				return true;
		}
		
		return false;
	}
	
	private boolean findSyncToken(LexerGroup[] syncTokens) {
		
		for(LexerGroup syncToken : syncTokens){
			
			if(currentToken.getType().equals(syncToken))
				return true;
		}
		
		return false;
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

	private void  nextToken(){
		
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
}

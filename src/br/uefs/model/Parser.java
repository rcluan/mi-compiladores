package br.uefs.model;

import java.util.List;

public class Parser {
	
	public void parseFile(List<Token> tokens){
		
		ParserRules rules = new ParserRules(tokens.iterator());
		
		rules.constants();
		rules.globalVariable();
		rules.program();
		rules.functions();
	}
}

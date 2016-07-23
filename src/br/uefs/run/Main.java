package br.uefs.run;

import br.uefs.model.Lexer;
import br.uefs.model.Token;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Lexer lexer = new Lexer();
		
		lexer.regexAnalyse("\'1\'l");
		
		for(Token token : lexer.getTokens()){
			
			System.out.println(token);
		}
	}

}

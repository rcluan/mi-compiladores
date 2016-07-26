package br.uefs.run;

import br.uefs.model.Lexer;
import br.uefs.model.Token;
import br.uefs.util.FileHandler;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Lexer lexer = new Lexer();
		
		//String input = FileHandler.readFile("");
		lexer.regexAnalyse("\"a 2+2");
		
		for(Token token : lexer.getTokens()){
			
			System.out.println(token);
		}
	}

}

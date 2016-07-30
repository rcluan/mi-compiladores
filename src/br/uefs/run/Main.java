package br.uefs.run;

import br.uefs.model.Lexer;
import br.uefs.model.PreprocessedInput;
import br.uefs.model.Token;
import br.uefs.util.Preprocessor;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Lexer lexer = new Lexer();
		
		PreprocessedInput input = Preprocessor.processFile("code2.txt");
		
		lexer.regexAnalyse(input);
		for(Token token : lexer.getTokens()){
			
			System.out.println(token);
		}
	}

}

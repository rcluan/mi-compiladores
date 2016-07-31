package br.uefs.run;

import java.util.Scanner;

import br.uefs.model.Lexer;
import br.uefs.model.PreprocessedInput;
import br.uefs.util.FileHandler;
import br.uefs.util.Preprocessor;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		
		while(true){
			
			System.out.println("Digite o nome do arquivo, inclusive sua extensão (e.g. code.txt)");
			String file = scanner.nextLine();
			
			String[] fileSplitted = file.split("\\.");
			String filename = fileSplitted[0], extension = fileSplitted[1];
			
			Lexer lexer = new Lexer();
			
			PreprocessedInput input = Preprocessor.processFile(filename + "." + extension);
			
			lexer.regexAnalyse(input);
			
			FileHandler.writeFile(filename, lexer.getTokens());
		}
	}

}

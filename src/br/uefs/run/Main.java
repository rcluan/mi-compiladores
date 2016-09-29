package br.uefs.run;

import java.io.File;

import br.uefs.model.Lexer;
import br.uefs.model.Parser;
import br.uefs.model.PreprocessedInput;
import br.uefs.util.FileHandler;
import br.uefs.util.Preprocessor;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String absolutePath = new File("").getAbsolutePath().concat("/codes");
		
		File folder = new File(absolutePath);
		
		File[] files = folder.listFiles();
		
		System.out.println("Iniciando...");
		for(File file : files){
			
			if(file.getName().contains(".txt")){
				
				System.out.println("Analisando arquivo " + file.getName());
				String[] fileSplitted = file.getName().split("\\.");
				String filename = fileSplitted[0];
				
				Lexer lexer = new Lexer();
				
				PreprocessedInput input = Preprocessor.processFile(absolutePath + "/" + file.getName());
				
				lexer.regexAnalyse(input);
				FileHandler.writeFile(absolutePath + "/lexer/" + filename, lexer.getTokens());
				
				if(lexer.hasNoErrors()){
					
					Parser parser = new Parser(lexer.getTokens());
					parser.parse();
					
					/*
					if(parser.getSyntacticErrors().isEmpty() && parser.getCurrentToken() == null){
						parser.getSyntacticErrors().add("Sucesso");
					}
					
					System.out.println("Código: " + file.getName());
					
					for(String error : parser.getSyntacticErrors())
						System.err.println(error);
					*/
					
					FileHandler.writeFile(parser.getSyntacticErrors(), absolutePath + "/parser/" + filename);
				}else{
					
					System.err.println("Erros léxicos no código " + file.getName() + " análise sintática não ser feita");
				}
			}
		}
		System.out.println("Todos arquivos foram analisados");
	}

}

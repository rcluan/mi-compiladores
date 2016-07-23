package br.uefs.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import br.uefs.model.Token;

public class FileHandler {
	
	public static String readFile(String file){
		
		String fileContent = new String();
		
		try {
			
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			
			String line = null;
			
			while((line = bufferedReader.readLine()) != null){
				
				fileContent += line;
			}
			
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			
			System.err.println("File could not be found");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fileContent.replaceAll(TokenType.COMENTARIO.pattern, "");
		
		return fileContent;
	}
	
	public static void writeFile(String file, List<Token> tokens){
		
		
	}
}

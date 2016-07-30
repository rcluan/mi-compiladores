package br.uefs.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import br.uefs.model.Token;

public class FileHandler {
	
	public static BufferedReader readFile(String file){
		
		BufferedReader bufferedReader = null;
		
		try {
			
			bufferedReader = new BufferedReader(new FileReader(file));
			
		} catch (FileNotFoundException e) {
			
			System.err.println("File could not be found");
			
		}
		
		return bufferedReader;
	}
	
	public static void writeFile(String file, List<Token> tokens){
		
		
	}
}

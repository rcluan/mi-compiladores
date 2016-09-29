package br.uefs.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import br.uefs.model.Token;

public class FileHandler {

	public static BufferedReader readFile(String file) {

		BufferedReader bufferedReader = null;

		try {

			bufferedReader = new BufferedReader(new FileReader(file));

		} catch (FileNotFoundException e) {

			System.err.println("File could not be found");

		}

		return bufferedReader;
	}

	public static void writeFile(String file, List<Token> tokens) {

		BufferedWriter writer = null;
		
		try {
			
			writer = new BufferedWriter(new FileWriter(file + "_result.txt"));
			
			for(Token token : tokens){
				
				writer.write(token.toString());
				writer.newLine();
			}
			
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeFile(List<String> results) {

		BufferedWriter writer = null;
		
		try {
			
			writer = new BufferedWriter(new FileWriter("parser_result.txt"));
			
			for(String result : results){
				
				writer.write(result.toString());
				writer.newLine();
			}
			
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

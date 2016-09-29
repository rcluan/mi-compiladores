package br.uefs.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.uefs.model.PreprocessedInput;
import br.uefs.model.Token;

public class Preprocessor {

	/**
	 * This method produces a PreprocessedInput object containing both the
	 * original file's content (separated by line, i.e., each index of the list
	 * refers to a line of the file) and a string with all lines concatenated
	 * and without commentaries.
	 * 
	 * The list of lines should be used to find the absolute line number of a possible
	 * error in the source code, whilst the string is where the lexer tries to match
	 * its regex in order to produce tokens and identify errors.
	 * 
	 * @param file the file to process
	 * @return the PreprocessedInput object
	 */
	public static PreprocessedInput processFile(String file){
		
		List<String> originalContent = new ArrayList<>();
		String contents = new String();
		PreprocessedInput input = new PreprocessedInput();
		
		try {
			
			BufferedReader reader = FileHandler.readFile(file);
			String line = null;
			
			while((line = reader.readLine()) != null){
				
				originalContent.add(line);
				contents += line + "\r\n";
			}
			
			input.setOriginalContent(originalContent);
			input.setContent(contents.replaceAll(LexerGroup.COMENTARIO.pattern, ""));
			
			reader.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return input;
	}
	
	public static boolean hasProgramBlock(Iterator<Token> tokens){
		
		while(tokens.hasNext()){
			
			Token token = tokens.next();
			
			if(token.getValue().equals("programa"))
				return true;
		}
		
		return false;
	}
}

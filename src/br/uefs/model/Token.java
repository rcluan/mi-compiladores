package br.uefs.model;

import br.uefs.util.LexerGroup;

public class Token {

	private LexerGroup type;
	private String value;
	
	private int line;
	private int column;

	public Token(LexerGroup type, String value, int line) {

		this.setType(type);
		this.setValue(value);
		
		this.setLine(line);
		this.setColumn(-1);
	}
	
	public Token(LexerGroup type, String value, int line, int column) {

		this.setType(type);
		this.setValue(value);
		
		this.setLine(line);
		this.setColumn(column);
	}

	public LexerGroup getType() {
		return type;
	}

	public void setType(LexerGroup type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public boolean is(String rep) {
		return this.getValue().equals(rep);
	}
	
	@Override
	public String toString() {
		
		return (column < 0) ? 
				String.format("%d [%s, %s ]", this.line, this.type.name(), this.value) :
					String.format("%d:%d [%s, %s ]", this.line, this.column, this.type.name(), this.value);
	}
	
	@Override
	public boolean equals(Object obj){
		
		if(obj instanceof Token){
			
			Token token = (Token) obj;
			return (this.getType().equals(token.getType()) && this.getValue().equals(token.getValue()));
		}
		
		return false;
	}
	
	@Override
	public int hashCode(){
		
		int hash = 7;
		
		return 17 * hash + 
				(this.getValue() != null ? Integer.parseInt(this.getValue()) : 0) + 
				(this.getType() != null ? Integer.parseInt(this.getType().name()) : 0);
	}
}

package br.uefs.model;

import br.uefs.util.TokenType;

public class Token {

	private TokenType type;
	private String value;
	
	private int line;
	private int column;

	public Token(TokenType type, String value) {

		this.setType(type);
		this.setValue(value);
		
		this.setLine(-1);
		this.setColumn(-1);
	}
	
	public Token(TokenType type, String value, int line, int column) {

		this.setType(type);
		this.setValue(value);
		
		this.setLine(line);
		this.setColumn(column);
	}

	public TokenType getType() {
		return type;
	}

	public void setType(TokenType type) {
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

	@Override
	public String toString() {
		
		return (column < 0 && line < 0) ? 
				String.format("<%s, %s>", this.type.name(), this.value) :
					String.format("<%s, %s, line %d, column %d>", this.type.name(), this.value, this.line, this.column);
	}
}

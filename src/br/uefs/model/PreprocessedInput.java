package br.uefs.model;

import java.util.List;

public class PreprocessedInput {
	
	private String content;
	private List<String> originalContent;
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getOriginalContent() {
		return originalContent;
	}

	public void setOriginalContent(List<String> originalContent) {
		this.originalContent = originalContent;
	}
	
	public int getOriginalLineNumber(String pattern){
		
		for(int i = 0; i < originalContent.size(); i++){
			
			String content = originalContent.get(i);
			
			if(content.contains(pattern))
				return i+1;
		}
		
		return 0;
	}
	
	
}

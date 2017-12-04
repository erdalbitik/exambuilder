package com.ebitik.exambuilder;

public class Essay implements Question {
	
	private String text;
	
	public Essay() {
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}

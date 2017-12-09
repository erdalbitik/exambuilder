package com.ebitik.exambuilder.httl;

import java.util.ArrayList;
import java.util.List;

public class OneColumnPage implements Page {
	
	private int blankHeight = 1150;
	
	private List<String> questions;
	
	@Override
	public boolean addQuestion(String question, int height) {
		boolean added = add(question, height);
		return added;
	}
	
	private boolean add(String question, int height) {
		if(height > blankHeight) return false;
		getQuestions().add(question);
		blankHeight -= height;
		return true;
	}

	public List<String> getQuestions() {
		if(questions == null) questions = new ArrayList<>();
		return questions;
	}

	public void setQuestions(List<String> questions) {
		this.questions = questions;
	}

}

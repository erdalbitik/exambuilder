package com.ebitik.exambuilder.httl;

import java.util.ArrayList;
import java.util.List;

import com.ebitik.exambuilder.Question;

public class OneColumnPage implements Page {
	
	private int blankHeight = 1100;
	
	private List<String> questions;
	
	@Override
	public boolean addQuestion(Question question) {
		boolean added = add(question);
		return added;
	}
	
	private boolean add(Question question) {
		int height = question.getHeight();
		if(height > blankHeight) return false;
		getQuestions().add(question.getAsXHTML(true));
		blankHeight -= height;
		Question emptySpace = question.getEmptySpaceAfterQuestion();
		if(emptySpace.getHeight() > 0 && emptySpace.getHeight() < blankHeight) {
			getQuestions().add(emptySpace.getAsXHTML(true));
			blankHeight -= emptySpace.getHeight();
		}
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

package com.ebitik.exambuilder.httl;

import java.util.ArrayList;
import java.util.List;

import com.ebitik.exambuilder.Question;
import com.ebitik.exambuilder.service.Service;

public class OneColumnPage implements Page {
	
	private int blankHeight = 1100;
	
	private List<String> questions;
	
	private Service service;
	
	public OneColumnPage(Service service) {
		this.service = service;
	}
	
	@Override
	public boolean addQuestion(Question question) {
		boolean added = add(question);
		return added;
	}
	
	private boolean add(Question question) {
		int height = question.getHeight(service);
		if(height > blankHeight) return false;
		getQuestions().add(question.getAsXHTML(true));
		blankHeight -= height;
		Question emptySpace = question.getEmptySpaceAfterQuestion();
		int emptySpaceHeight = emptySpace.getHeight(service);
		if(emptySpaceHeight > 0 && emptySpaceHeight < blankHeight) {
			getQuestions().add(emptySpace.getAsXHTML(true));
			blankHeight -= emptySpaceHeight;
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

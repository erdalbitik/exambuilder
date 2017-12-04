package com.ebitik.exambuilder;

import java.util.List;

public class Group implements Question {
	
	private String text;
	
	private List<MultipleChoiceBuilder> questionList;
	
	public Group() {
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<MultipleChoiceBuilder> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<MultipleChoiceBuilder> questionList) {
		this.questionList = questionList;
	}
	
}

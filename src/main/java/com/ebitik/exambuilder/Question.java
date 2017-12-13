package com.ebitik.exambuilder;

public interface Question {

	Integer defaultEmptySpaceAfterQuestion = 50;//px
	
	String getAsXHTML(boolean asTable);
	int getHeight();
	Question getEmptySpaceAfterQuestion();
}

package com.ebitik.exambuilder;

import com.ebitik.exambuilder.service.Service;

public interface Question {

	Integer defaultEmptySpaceAfterQuestion = 50;//px
	
	String getAsXHTML(boolean asTable);
	int getHeight(Service service);
	Question getEmptySpaceAfterQuestion();
}

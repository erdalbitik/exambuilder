package com.ebitik.exambuilder.httl;

import java.util.ArrayList;
import java.util.List;

public class TwoColumnPage implements Page {
	
	private int leftBlankHeight = 982;
	
	private int rightBlankHeight = 982;
	
	private List<String> leftQuestions;
	
	private List<String> rightQuestions;
	
	@Override
	public boolean addQuestion(String question, int height) {
		//oncelikle sagda soru varmi diye bakilir. varsa oradan devam edilir.
		if(getRightQuestions().size() > 0) {
			return addRight(question, height);
		}
		
		//sola deneyelim
		boolean addedLeft = addLeft(question, height);
		//olmazsa saga deneyelim
		if(!addedLeft) {
			return addRight(question, height);
		}
		return addedLeft;
	}
	
	private boolean addRight(String question, int height) {
		if(height > rightBlankHeight) return false;
		getRightQuestions().add(question);
		rightBlankHeight -= height;
		return true;
	}
	
	private boolean addLeft(String question, int height) {
		if(height > leftBlankHeight) return false;
		getLeftQuestions().add(question);
		leftBlankHeight -= height;
		return true;
	}

	public List<String> getLeftQuestions() {
		if(leftQuestions == null) leftQuestions = new ArrayList<>();
		return leftQuestions;
	}

	public void setLeftQuestions(List<String> leftQuestions) {
		this.leftQuestions = leftQuestions;
	}

	public List<String> getRightQuestions() {
		if(rightQuestions == null) rightQuestions = new ArrayList<>();
		return rightQuestions;
	}

	public void setRightQuestions(List<String> rightQuestions) {
		this.rightQuestions = rightQuestions;
	}

	public int getLeftBlankHeight() {
		return leftBlankHeight;
	}

	public void setLeftBlankHeight(int leftBlankHeight) {
		this.leftBlankHeight = leftBlankHeight;
	}

	public int getRightBlankHeight() {
		return rightBlankHeight;
	}

	public void setRightBlankHeight(int rightBlankHeight) {
		this.rightBlankHeight = rightBlankHeight;
	}
}

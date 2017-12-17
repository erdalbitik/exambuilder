package com.ebitik.exambuilder.httl;

import java.util.ArrayList;
import java.util.List;

import com.ebitik.exambuilder.EmptySpace;
import com.ebitik.exambuilder.Question;
import com.ebitik.exambuilder.service.Service;
import com.ebitik.exambuilder.type.ColumnType;
import com.ebitik.exambuilder.type.PaperType;

public class TwoColumnPage implements Page {
	
	private int leftBlankHeight = 1100;
	
	private int rightBlankHeight = 1100;
	
	private List<String> leftQuestions;
	
	private List<String> rightQuestions;
	
	private Service service;
	
	public TwoColumnPage(Service service) {
		this.service = service;
	}
	
	@Override
	public boolean addQuestion(Question question) {
		//oncelikle sagda soru varmi diye bakilir. varsa oradan devam edilir.
		if(getRightQuestions().size() > 0) {
			return addRight(question);
		}
		
		//sola deneyelim
		boolean addedLeft = addLeft(question);
		//olmazsa saga deneyelim
		if(!addedLeft) {
			return addRight(question);
		}
		return addedLeft;
	}
	
	private boolean addRight(Question question) {
		int height = question.getHeight(service);
		if(height > rightBlankHeight) {
			//kalan bosluga bos alan ekleyelim
			try {
				getRightQuestions().add(new EmptySpace(rightBlankHeight, PaperType.A4, ColumnType.TWO_COLUMN).getAsXHTML(true));
			} catch (Exception e) {
			}
			
			rightBlankHeight = 0;
			return false;
		}
		getRightQuestions().add(question.getAsXHTML(true));
		rightBlankHeight -= height;
		Question emptySpace = question.getEmptySpaceAfterQuestion();
		int emptySpaceHeight = emptySpace.getHeight(service);
		if(emptySpaceHeight > 0 && emptySpaceHeight < rightBlankHeight) {
			getRightQuestions().add(emptySpace.getAsXHTML(true));
			rightBlankHeight -= emptySpaceHeight;
		}
		return true;
	}
	
	private boolean addLeft(Question question) {
		int height = question.getHeight(service);
		if(height > leftBlankHeight) {
			try {
				getLeftQuestions().add(new EmptySpace(leftBlankHeight, PaperType.A4, ColumnType.TWO_COLUMN).getAsXHTML(true));
			} catch (Exception e) {
			}
			leftBlankHeight = 0;
			return false;
		}
		getLeftQuestions().add(question.getAsXHTML(true));
		leftBlankHeight -= height;
		Question emptySpace = question.getEmptySpaceAfterQuestion();
		int emptySpaceHeight = emptySpace.getHeight(service);
		if(emptySpaceHeight > 0 && emptySpaceHeight < leftBlankHeight) {
			getLeftQuestions().add(emptySpace.getAsXHTML(true));
			leftBlankHeight -= emptySpaceHeight;
		}
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

package com.ebitik.exambuilder.httl;

import java.util.ArrayList;
import java.util.List;

import com.ebitik.exambuilder.ColumnType;
import com.ebitik.exambuilder.Question;

public class ExamPages {
	
	private List<Page> pages = new ArrayList<>();
	
	private ColumnType columnType;
	
	public ExamPages(ColumnType columnType) {
		this.columnType = columnType;
	}
	
	public void addQuestion(Question question) {
		boolean addedQuestion = getLastPage().addQuestion(question);
		//eklenemezse yeni sayfa olustur.
		if(!addedQuestion) {
			addNewPage();
			addedQuestion = getLastPage().addQuestion(question);
		}
	}
	
	private Page getLastPage() {
		if(pages.size() == 0) addNewPage();
		return pages.get(pages.size()-1);
	}
	
	private void addNewPage() {
		if(ColumnType.ONE_COLUMN.equals(columnType)) {
			pages.add(new OneColumnPage());
		} else {
			pages.add(new TwoColumnPage());
		}
	}

	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

}

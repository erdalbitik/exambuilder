package com.ebitik.exambuilder.httl;

import java.util.ArrayList;
import java.util.List;

import com.ebitik.exambuilder.ColumnType;

public class ExamPages {
	
	private List<Page> pages = new ArrayList<>();
	
	private ColumnType columnType;
	
	public ExamPages(ColumnType columnType) {
		this.columnType = columnType;
	}
	
	public void addQuestion(String question, int height) {
		boolean addedQuestion = getLastPage().addQuestion(question, height);
		//eklenemezse yeni sayfa olustur.
		if(!addedQuestion) {
			addNewPage();
			addedQuestion = getLastPage().addQuestion(question, height);
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

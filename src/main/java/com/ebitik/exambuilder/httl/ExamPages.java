package com.ebitik.exambuilder.httl;

import java.util.ArrayList;
import java.util.List;

import com.ebitik.exambuilder.Question;
import com.ebitik.exambuilder.service.Service;
import com.ebitik.exambuilder.type.ColumnType;

public class ExamPages {
	
	private List<Page> pages = new ArrayList<>();
	
	private ColumnType columnType;
	
	private Service service;
	
	public ExamPages(Service service, ColumnType columnType) {
		this.service = service;
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
			pages.add(new OneColumnPage(service));
		} else {
			pages.add(new TwoColumnPage(service));
		}
	}

	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

}

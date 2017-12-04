package com.ebitik.exambuilder.httl;

import java.util.ArrayList;
import java.util.List;

public class ExamPages {
	
	private List<TwoColumnPage> pages = new ArrayList<>();
	
	public void addQuestion(String question, int height) {
		boolean addedQuestion = getLastPage().addQuestion(question, height);
		//eklenemezse yeni sayfa olustur.
		if(!addedQuestion) {
			pages.add(new TwoColumnPage());
			addedQuestion = getLastPage().addQuestion(question, height);
		}
	}
	
	private TwoColumnPage getLastPage() {
		if(pages.size() == 0) pages.add(new TwoColumnPage());
		return pages.get(pages.size()-1);
	}

	public List<TwoColumnPage> getPages() {
		return pages;
	}

	public void setPages(List<TwoColumnPage> pages) {
		this.pages = pages;
	}

}

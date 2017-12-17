package com.ebitik.exambuilder.service;

public interface Service {

	public int getQuestionTableHeight(String html) throws Exception;
	
	public void htmlToPdf(String htmlFilePath, String pdfPath) throws Exception;
	
}

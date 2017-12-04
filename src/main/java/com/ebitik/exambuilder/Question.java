package com.ebitik.exambuilder;

public interface Question {
	//PdfPTable getAsPDFPTable() throws Exception;
	String getAsXHTML(boolean asTable) throws Exception;
	int getHeight() throws Exception;
}

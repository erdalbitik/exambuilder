package com.ebitik.exambuilder;

import org.apache.commons.lang3.StringUtils;

import com.ebitik.exambuilder.service.PuppeteerService;
import com.ebitik.exambuilder.service.QuestionSizeMap;
import com.ebitik.exambuilder.util.Util;

public class Essay implements Question {
	
	private String questionText;
	
	private String answerText;
	
	private PaperType paperType;
	
	private ColumnType columnType;
	
	private String questionNumber;
	
	private String xhtml;
	
	private String table;
	
	private Integer height;
	
	private String htmlTemplate = "<table id='questionTable' style='table-layout: fixed;'> <tbody> ${{optionalHeaderLine}}<tr> <td valign='top'> <b>${{questionNumber}}.</b></td><td> <div class='cont'> ${{questionText}}</div></td></tr> <tr> <td></td> <td> <div class=\"cevapCont\"> ${{answerText}} </div> </td></tr></tbody> </table>";
	
	public Essay(String questionNumber, PaperType paperType, ColumnType columnType, String questionText, String answerText) {
		this.questionText = questionText;
		this.paperType = paperType;
		this.questionNumber = questionNumber;
		this.answerText = answerText;
		this.columnType = columnType;
	}
	
	private void createHtml(String questionNumber, PaperType paperType, ColumnType columnType) throws Exception {
		if("1".equals(questionNumber)) {
			htmlTemplate = htmlTemplate.replace("${{optionalHeaderLine}}", "<tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td></tr>");
		} else {
			htmlTemplate = htmlTemplate.replace("${{optionalHeaderLine}}", "");
		}
		
		htmlTemplate = htmlTemplate.replace("${{questionNumber}}", questionNumber);
		htmlTemplate = htmlTemplate.replace("${{questionText}}", questionText);
		htmlTemplate = htmlTemplate.replace("${{answerText}}", answerText);
		
		xhtml = Util.htmlToXhtml(htmlTemplate, paperType, columnType);
		table = StringUtils.substringBetween(xhtml, "<body>", "</body>");
	}
	
	@Override
	public String getAsXHTML(boolean isTable) throws Exception {
		if(xhtml == null || "".equals(xhtml)) {
			createHtml(questionNumber, paperType, columnType);
		}
		if(isTable) return table;
		return xhtml;
	}

	@Override
	public int getHeight() throws Exception {
		if(height != null && height > 0) return height;
		String mapHtml = xhtml.replaceAll("(<td valign='top'><b>)[^&]*(.</b></td>)", "$1$2");
		height = QuestionSizeMap.getSize(mapHtml);
		if(height == null) {
			height = PuppeteerService.getQuestionTableHeight(getAsXHTML(false));
			QuestionSizeMap.putSize(mapHtml, height);
		}
		return height;
	}
	
}

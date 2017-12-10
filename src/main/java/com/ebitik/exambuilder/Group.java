package com.ebitik.exambuilder;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.ebitik.exambuilder.service.PuppeteerService;
import com.ebitik.exambuilder.util.Util;

public class Group implements Question {

	private String text;

	private List<Question> questionList;

	private PaperType paperType;

	private ColumnType columnType;

	private String xhtml;

	private String table;
	
	private String questionNumber;

	private int height = 0;

	private String htmlTemplate = "<table id='questionTable' style='table-layout: fixed;'> <tbody> ${{optionalHeaderLine}}<tr> <td valign='top'> <b></b></td><td> <div class='cont'> ${{questionText}}</div></td></tr> <tr> <td>&nbsp;</td> </tr> <tr> <td>&nbsp;</td> </tr></tbody> </table>";

	public Group(String questionNumber, PaperType paperType, ColumnType columnType, String text, List<Question> questionList) {
		this.questionNumber = questionNumber;
		this.text = text;
		this.paperType = paperType;
		this.columnType = columnType;
		this.questionList = questionList;
	}
	
	private void createHtml(String questionNumber, PaperType paperType, ColumnType columnType) throws Exception {
		if("1".equals(questionNumber)) {
			htmlTemplate = htmlTemplate.replace("${{optionalHeaderLine}}", "<tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td></tr>");
		} else {
			htmlTemplate = htmlTemplate.replace("${{optionalHeaderLine}}", "");
		}
		
		htmlTemplate = htmlTemplate.replace("${{questionText}}", text);
		
		xhtml = Util.htmlToXhtml(htmlTemplate, paperType, columnType);
		table = StringUtils.substringBetween(xhtml, "<body>", "</body>");
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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
		if(height == 0) {
			height = PuppeteerService.getQuestionTableHeight(getAsXHTML(false));
		}
		return height;
	}

	public List<Question> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<Question> questionList) {
		this.questionList = questionList;
	}

}

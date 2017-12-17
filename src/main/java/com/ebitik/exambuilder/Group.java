package com.ebitik.exambuilder;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.ebitik.exambuilder.service.QuestionSizeMap;
import com.ebitik.exambuilder.service.Service;
import com.ebitik.exambuilder.type.ColumnType;
import com.ebitik.exambuilder.type.PaperType;
import com.ebitik.exambuilder.util.Util;

public class Group implements Question {

	private String preText;

	private String text;

	private List<Question> questionList;

	private PaperType paperType;

	private ColumnType columnType;

	private String xhtml;

	private String table;

	private Integer height;

	private String htmlTemplate = "<table id='questionTable' style='table-layout: fixed;'> <tbody> <tr> <td valign='top'></td><td> <div class='cont'> ${{preText}}</div></td></tr><tr> <td>&nbsp;</td> </tr><tr> <td valign='top'> <b></b></td><td> <div class='cont'> ${{questionText}}</div></td></tr> <tr> <td>&nbsp;</td> </tr> <tr> <td>&nbsp;</td> </tr></tbody> </table>";

	public Group(String preText, PaperType paperType, ColumnType columnType, String text, List<Question> questionList) {
		this.preText = preText;
		this.text = text;
		this.paperType = paperType;
		this.columnType = columnType;
		this.questionList = questionList;
	}

	private void createHtml(PaperType paperType, ColumnType columnType) throws Exception {
		htmlTemplate = htmlTemplate.replace("${{questionText}}", text);
		htmlTemplate = htmlTemplate.replace("${{preText}}", preText);
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
	public String getAsXHTML(boolean isTable) {
		if(xhtml == null || "".equals(xhtml)) {
			try {
				createHtml(paperType, columnType);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		if(isTable) return table;
		return xhtml;
	}

	@Override
	public int getHeight(Service service) {
		if(height != null && height > 0) return height;
		if(StringUtils.isEmpty(xhtml)) xhtml = getAsXHTML(false);
		height = QuestionSizeMap.getSize(xhtml);
		if(height == null) {
			try {
				height = service.getQuestionTableHeight(getAsXHTML(false));
			} catch (Exception e) {
				height = 100;//TODO: magic number duzelt
				System.out.println(e);
			}
			QuestionSizeMap.putSize(xhtml, height);
		}
		return height;
	}

	public List<Question> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<Question> questionList) {
		this.questionList = questionList;
	}

	@Override
	public Question getEmptySpaceAfterQuestion() {
		return new EmptySpace(0, paperType, columnType);
	}

}

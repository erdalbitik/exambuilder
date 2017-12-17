package com.ebitik.exambuilder;

import org.apache.commons.lang3.StringUtils;

import com.ebitik.exambuilder.service.QuestionSizeMap;
import com.ebitik.exambuilder.service.Service;
import com.ebitik.exambuilder.type.ColumnType;
import com.ebitik.exambuilder.type.PaperType;
import com.ebitik.exambuilder.util.Util;

public class Essay implements Question {

	private String questionText;

	private PaperType paperType;

	private ColumnType columnType;

	private String questionNumber;

	private String xhtml;

	private String table;

	private Integer height;

	private Integer emptySpaceHeight;

	private String htmlTemplate = "<table id='questionTable' style='table-layout: fixed;'> <tbody> <tr> <td valign='top'> <b>${{questionNumber}}.</b></td><td> <div class='cont'> ${{questionText}}</div></td></tr> </tbody> </table>";

	public Essay(String questionNumber, PaperType paperType, ColumnType columnType, Integer emptySpaceHeight, String questionText) {
		this.questionText = questionText;
		this.paperType = paperType;
		this.questionNumber = questionNumber;
		this.columnType = columnType;
		this.emptySpaceHeight = emptySpaceHeight;
	}

	private void createHtml(String questionNumber, PaperType paperType, ColumnType columnType) throws Exception {
		htmlTemplate = htmlTemplate.replace("${{questionNumber}}", questionNumber);
		htmlTemplate = htmlTemplate.replace("${{questionText}}", questionText);

		xhtml = Util.htmlToXhtml(htmlTemplate, paperType, columnType);
		table = StringUtils.substringBetween(xhtml, "<body>", "</body>");
	}

	@Override
	public String getAsXHTML(boolean isTable) {
		if(xhtml == null || "".equals(xhtml)) {
			try {
				createHtml(questionNumber, paperType, columnType);
			} catch (Exception e) {
				height = 100;//TODO: magic number duzelt
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
		String mapHtml = xhtml.replaceAll("(<td valign='top'><b>)[^&]*(.</b></td>)", "$1$2");
		height = QuestionSizeMap.getSize(mapHtml);
		if(height == null) {
			try {
				height = service.getQuestionTableHeight(getAsXHTML(false));
			} catch (Exception e) {
				height = 100;//TODO: magic number duzelt
				System.out.println(e);
			}
			QuestionSizeMap.putSize(mapHtml, height);
		}
		System.out.println(questionNumber+": "+height);
		return height;
	}

	@Override
	public Question getEmptySpaceAfterQuestion() {
		if(emptySpaceHeight == null) emptySpaceHeight = defaultEmptySpaceAfterQuestion;
		return new EmptySpace(emptySpaceHeight, paperType, columnType);
	}

}

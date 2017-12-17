package com.ebitik.exambuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.ebitik.exambuilder.service.PuppeteerService;
import com.ebitik.exambuilder.service.QuestionSizeMap;
import com.ebitik.exambuilder.service.Service;
import com.ebitik.exambuilder.type.ColumnType;
import com.ebitik.exambuilder.type.PaperType;
import com.ebitik.exambuilder.util.Util;

public class MultipleChoice implements Question {

	private String text;

	private List<Choice> choiceList;

	private PaperType paperType;

	private ColumnType columnType;

	private String questionNumber;

	private String xhtml;

	private String table;

	private Integer height;

	Integer emptySpaceHeight;
	
	Service service;

	private String questionHtmlTemplate = "<table id='questionTable' width='330px'><tbody><tr> <td valign='top'><b>${{questionNumber}}.</b></td><td><div class='cont'>${{questionText}}</div></td></tr><tr><td>&nbsp;</td></tr>${{choiceTexts}}</tbody></table>";
	private String choiceHtmlTemplate = "<tr><td></td><td style='padding-top: 0px;' class='cevapCont'><table class=\"fixedWidthTableCell\"><tr><td valign=\"top\" width=\"20\" style='white-space: nowrap; padding-left:6px;'><b>${{choiceLabel}})</b></td><td style=\"padding-left:5px;\">${{choiceText}}</td></tr></table></td></tr>";

	public MultipleChoice(String questionNumber, PaperType paperType, ColumnType columnType, Integer emptySpaceHeight, String questionText, List<Choice> choiceList) {
		this.text = questionText;
		this.choiceList = choiceList;
		this.paperType = paperType;
		this.columnType = columnType;
		this.questionNumber = questionNumber;
		this.emptySpaceHeight = emptySpaceHeight;
	}

	protected String getText() {
		return text;
	}

	protected void setText(String text) {
		this.text = text;
	}

	protected List<Choice> getChoiceList() {
		return choiceList;
	}

	protected void setChoiceList(List<Choice> choiceList) {
		this.choiceList = choiceList;
	}

	protected void addChoice(Choice choice) {
		if(Objects.isNull(choiceList)) choiceList = new ArrayList<>();
		choiceList.add(choice);
	}

	private void createHtml(String questionNumber, PaperType paperType, ColumnType columnType) throws Exception {
		String qtext = text;
		if(Objects.isNull(qtext)) {
			qtext = "";
		}

		questionHtmlTemplate = questionHtmlTemplate.replace("${{questionNumber}}", questionNumber);
		questionHtmlTemplate = questionHtmlTemplate.replace("${{questionText}}", qtext);

		String choiceTexts = "";
		if(Objects.nonNull(choiceList) && !choiceList.isEmpty()) {
			String[] labels = {"A", "B", "C", "D", "E", "F", "G"};
			for (int i=0; i<choiceList.size();i++) {
				Choice choice = choiceList.get(i);
				String textTemplate = choiceHtmlTemplate;
				choiceTexts += textTemplate.replace("${{choiceText}}", choice.getText()).replace("${{choiceLabel}}", labels[i]);
			}
		}
		questionHtmlTemplate = questionHtmlTemplate.replace("${{choiceTexts}}", choiceTexts);

		xhtml = Util.htmlToXhtml(questionHtmlTemplate, paperType, columnType);

		table = StringUtils.substringBetween(xhtml, "<body>", "</body>");
	}

	@Override
	public String getAsXHTML(boolean isTable) {
		if(xhtml == null || "".equals(xhtml)) {
			try {
				createHtml(questionNumber, paperType, columnType);
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
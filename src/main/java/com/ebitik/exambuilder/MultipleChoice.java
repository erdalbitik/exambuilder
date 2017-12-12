package com.ebitik.exambuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.ebitik.exambuilder.service.PuppeteerService;
import com.ebitik.exambuilder.service.QuestionSizeMap;
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
	
	private String questionHtmlTemplate = "<table id='questionTable' width='330px'><tbody>${{optionalHeaderLine}}<tr> <td valign='top'><b>${{questionNumber}}.</b></td><td><div class='cont'>${{questionText}}</div></td></tr><tr><td>&nbsp;</td></tr>${{choiceTexts}}<tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td></tr></tbody></table>";
	private String choiceHtmlTemplate = "<tr><td></td><td style='padding-top: 0px;' class='cevapCont'><table class=\"fixedWidthTableCell\"><tr><td valign=\"top\" width=\"20\" style=\"padding-left:6px;\"><b>${{choiceLabel}})</b></td><td style=\"padding-left:5px;\">${{choiceText}}</td></tr></table></td></tr>";
	
	public MultipleChoice(String questionNumber, PaperType paperType, ColumnType columnType, String questionText, List<Choice> choiceList) {
		this.text = questionText;
		this.choiceList = choiceList;
		this.paperType = paperType;
		this.columnType = columnType;
		this.questionNumber = questionNumber;
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
		
		if("1".equals(questionNumber)) {
			questionHtmlTemplate = questionHtmlTemplate.replace("${{optionalHeaderLine}}", "<tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td></tr>");
		} else {
			questionHtmlTemplate = questionHtmlTemplate.replace("${{optionalHeaderLine}}", "");
		}
		
		questionHtmlTemplate = questionHtmlTemplate.replace("${{questionNumber}}", questionNumber);
		questionHtmlTemplate = questionHtmlTemplate.replace("${{questionText}}", qtext);
		
		String choiceTexts = "";
		if(Objects.nonNull(choiceList) && !choiceList.isEmpty()) {
			String[] labels = {"A", "B", "C", "D", "E"};
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
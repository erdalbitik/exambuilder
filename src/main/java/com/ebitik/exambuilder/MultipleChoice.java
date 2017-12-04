package com.ebitik.exambuilder;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.ebitik.exambuilder.service.PhantomService;
import com.ebitik.exambuilder.util.Util;
import com.lowagie.text.Element;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class MultipleChoice implements Question {
	
	private String text;
	
	private List<Choice> choiceList;
	
	private PdfPTable pdfPTable;
	
	private PaperType paperType;
	
	private ColumnType columnType;
	
	private String questionNumber;
	
	private String xhtml;
	
	private String table;
	
	int height = 0;
	
	private String questionHtmlTemplate = "<table id='questionTable' style='table-layout: fixed;'><tbody><tr> <td valign='top'><b>${{questionNumber}}.</b></td><td><div class='cont'>${{questionText}}</div></td></tr>${{choiceTexts}}</tbody></table>";
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

	/*@Override
	public PdfPTable getAsPDFPTable() throws Exception {
		if(pdfPTable == null) {
			createPdfPTable(questionNumber, paperType, columnType);
		}
		return pdfPTable;
	}*/
	
	/*private void createPdfPTable(String questionNumber, PaperType paperType, ColumnType columnType) throws Exception {
		PdfPTable table = new PdfPTable(1);
		table.setTotalWidth(300);
		PdfPCell cell = new PdfPCell();
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setPaddingLeft(15);
		String qtext = text;
		if(Objects.isNull(qtext)) {
			qtext = "";
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
		
		String qhtml = Util.htmlToXhtml(questionHtmlTemplate, paperType, columnType);
		
		String css = columnType.css;
		Reader reader = new StringReader(qhtml);
		StyleSheet style = new StyleSheet();
		style.loadStyle(css, null);
		ArrayList<Element> parseToList = HTMLWorker.parseToList(reader, style);
		//ElementList parseToElementList = XMLWorkerHelper.parseToElementList(soruMetin, css);

		for (Element el : parseToList) {
			cell.addElement(el);
		}
		// cell.setExtraParagraphSpace(5);
		table.addCell(cell);

		this.pdfPTable = table;
	}*/
	
	private void createHtml(String questionNumber, PaperType paperType, ColumnType columnType) throws Exception {
		
		String qtext = text;
		if(Objects.isNull(qtext)) {
			qtext = "";
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
		if(height == 0) {
			height = PhantomService.getHtmlElementHeight(getAsXHTML(false));
			//System.out.println(getAsXHTML(false));
		}
		return height;
	}

}
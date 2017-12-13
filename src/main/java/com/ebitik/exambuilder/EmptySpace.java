package com.ebitik.exambuilder;

import org.apache.commons.lang3.StringUtils;

public class EmptySpace implements Question {
	
	private PaperType paperType;
	
	private ColumnType columnType;
	
	private Integer height = 0;
	
	private String html;
	
	private String htmlTemplate = "<table id='questionTable' style='table-layout: fixed;'> <tbody><tr><td style='height: ${{height}}px'></td></tr></tbody> </table>";
	
	public EmptySpace(Integer height, PaperType paperType, ColumnType columnType) {
		this.paperType = paperType;
		this.columnType = columnType;
		this.height = height;
	}
	
	private void createHtml(Integer height, PaperType paperType, ColumnType columnType) {
		html = htmlTemplate.replace("${{height}}", (height-6)+"");//baski sirasinda 6 pixel fark oluyor her zaman.
	}
	
	@Override
	public String getAsXHTML(boolean isTable) {
		if(StringUtils.isEmpty(html)) {
			createHtml(height, paperType, columnType);
			//olmasi gerekenden 6 pixel fazla cikiyor gercekte.
			//int sheight = PuppeteerService.getQuestionTableHeight(Util.htmlToXhtml(html, paperType, columnType));
			//System.out.println("Height: "+height+" pupheight: "+sheight);
		}
		System.out.println("Bosluk eklendi: "+(height));
		return html;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public Question getEmptySpaceAfterQuestion() {
		return this;
	}
	
}

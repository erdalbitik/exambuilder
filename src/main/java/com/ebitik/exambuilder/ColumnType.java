package com.ebitik.exambuilder;

public enum ColumnType {

	ONE_COLUMN("/templates/one_column.vm", ".cont { font-weight: bold; padding-left:5px; width: 600px; text-align: justify; text-justify: inter-word; } .cevapCont { width: 540px; text-align: justify; text-justify: inter-word; } * { font-family: 'Myriad Pro'; font-size: 12px; line-height: 14px;} img { display:block; max-width: 540px;} .cevapTextTD { padding-left:15px; text-align: left; } .fixedWidthTableCell { table-layout:fixed; width: 560px;} .fixedWidthTableCell td { overflow: visible; } sup { vertical-align: super; font-size: smaller;} sub {vertical-align: sub; font-size: smaller;}"),
	TWO_COLUMN("/templates/two_column.vm", ".cont { font-weight: bold; padding-left:5px; width: 300px; text-align: justify; text-justify: inter-word; } .cevapCont { width: 300px; text-align: justify; text-justify: inter-word; } * { font-family: 'Myriad Pro'; font-size: 12px; line-height: 12px;} img { display:block; max-width: 300px;} .cevapTextTD { padding-left:8px; text-align: left; } .fixedWidthTableCell { table-layout:fixed; width: 300px;} .fixedWidthTableCell td { overflow: visible; } sup { vertical-align: super; font-size: smaller;} sub {vertical-align: sub; font-size: smaller;}");
	
	public String vm;
	public String css;
	
	private ColumnType(String vm, String css) {
		this.vm = vm;
		this.css = css;
	}

}

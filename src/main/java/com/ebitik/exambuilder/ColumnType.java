package com.ebitik.exambuilder;

public enum ColumnType {

	ONE_COLUMN(".cont { font-weight: bold; padding-left:10px; width: 520px; text-align: justify; text-justify: inter-word; } .cevapCont { width: 540px; text-align: justify; text-justify: inter-word; } * { font-family: 'Myriad Pro'; font-size: 12px; line-height: 16px;} img { display:block; max-width: 520px;} .cevapTextTD { padding-left:10px; text-align: left; } .fixedWidthTableCell { table-layout:fixed; width: 480px;} .fixedWidthTableCell td { overflow: visible; } sup { vertical-align: super; font-size: smaller;} sub {vertical-align: sub; font-size: smaller;}"),
	TWO_COLUMN(".cont { font-weight: bold; padding-left:5px; width: 300px; text-align: justify; text-justify: inter-word; } .cevapCont { width: 310px; text-align: justify; text-justify: inter-word; } * { font-family: 'Myriad Pro'; font-size: 12px; line-height: 12px;} img { display:block; max-width: 301px;} .cevapTextTD { padding-left:8px; text-align: left; } .fixedWidthTableCell { table-layout:fixed; width: 298px;} .fixedWidthTableCell td { overflow: visible; } sup { vertical-align: super; font-size: smaller;} sub {vertical-align: sub; font-size: smaller;}");
	
	public String css;
	
	private ColumnType(String css) {
		this.css = css;
	}

}

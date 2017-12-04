package com.ebitik.exambuilder;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

public enum PaperType {
	A4(PageSize.A4);
	
	public Rectangle pageSize;
	
	PaperType(Rectangle pageSize) {
		this.pageSize = pageSize;
	}
	
}

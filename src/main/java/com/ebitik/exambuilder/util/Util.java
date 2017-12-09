package com.ebitik.exambuilder.util;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import com.ebitik.exambuilder.ColumnType;
import com.ebitik.exambuilder.PaperType;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;

public class Util {

	public static String htmlToXhtml(String html, PaperType paperType, ColumnType columnType) throws Exception {
		try (ByteArrayOutputStream fos = new ByteArrayOutputStream();) {
			Tidy tidy = new Tidy();
			tidy.setShowWarnings(false);
			tidy.setShowErrors(0);
			tidy.setQuiet(true);
			tidy.setInputEncoding("UTF-8");
			tidy.setOutputEncoding("UTF-8");
			tidy.setXHTML(true);
			tidy.setMakeClean(true);

			org.w3c.dom.Document dom = tidy.parseDOM(new ByteArrayInputStream(html.getBytes("UTF-8")), null);

			int maxImgWidth = (int) paperType.pageSize.getWidth()/2;
			if(ColumnType.ONE_COLUMN.equals(columnType)) {
				maxImgWidth = (int) paperType.pageSize.getWidth();
			}
			NodeList imgList = dom.getElementsByTagName("img");
			for (int i = 0; i < imgList.getLength(); i++) {
				org.w3c.dom.Element element = (org.w3c.dom.Element) imgList.item(i);
				String style = element.getAttribute("style");
				if(Objects.nonNull(style) && style.contains("width")) {
					//get width px
					String snumber = StringUtils.substringBetween(style, "width:", "px");
					int number = convertInteger(snumber, maxImgWidth);
					if(number > maxImgWidth) {
						String height = StringUtils.substringBetween(style, "height:", "px");
						style = StringUtils.replace(style, "height:"+height+"px;", "", 1);
						style = StringUtils.replace(style, "height:"+height+"px", "", 1);
						style = StringUtils.replace(style, "width:"+snumber+"px", "width:"+maxImgWidth+"px", 1);
						element.setAttribute("style", style);
					}
				}
			}

			tidy.pprint(dom, fos);
			String xhtml = fos.toString("UTF-8");
			xhtml = xhtml.replace("<head>", "<head><style>"+columnType.css+"</style>");
			return xhtml;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void addText(PdfContentByte cb, int x, int y, int punto, int style, int align, String fontFamily,
			float opacity, Color color, String text) {
		addText(cb, x, y, punto, style, align, fontFamily, opacity, color, text, 0);
	}

	public static void addText(PdfContentByte cb, int x, int y, int punto, int style, int align, String fontFamily,
			float opacity, Color color, String text, float rotation) {
		Font font = FontFactory.getFont(fontFamily, BaseFont.CP1257, true);
		font.setStyle(style);
		font.setSize(punto);
		font.setColor(color);
		Phrase p = new Phrase(text, font);
		cb.saveState();
		PdfGState gs1 = new PdfGState();
		gs1.setFillOpacity(opacity);
		cb.setGState(gs1);
		ColumnText.showTextAligned(cb, align, p, x, y, rotation);
		cb.restoreState();
	}

	public static int convertInteger(Object obj, int defaultPr) {
		try {
			if (null == obj) {
				return defaultPr;
			}
			if(obj instanceof String) {
				obj = StringUtils.trimToEmpty(obj.toString());
			}

			if (obj instanceof Number) {
				return ((Number) obj).intValue();
			} else {
				return Integer.parseInt(obj.toString());
			}

		} catch (Exception e) {
		}
		return defaultPr;
	}
	
	public static InputStream getFileFromResourceFolder(String path) {
		ClassLoader classLoader = Util.class.getClassLoader();
		return classLoader.getResourceAsStream(path);
	}
	
	public static String getFilePathFromResourceFolder(String path) {
		ClassLoader classLoader = Util.class.getClassLoader();
		return classLoader.getResource(path).getPath();
	}
	
	public static String getFileFullPathResourceFolder(String fileName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL resource = classLoader.getResource(fileName);
		try {
			return Paths.get(resource.toURI()).toFile().getAbsolutePath();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getParentFullPathResourceFolder(String fileName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL resource = classLoader.getResource(fileName);
		try {
			return Paths.get(resource.toURI()).toFile().getParent();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}

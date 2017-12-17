package com.ebitik.exambuilder;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;

import com.ebitik.exambuilder.httl.ExamPages;
import com.ebitik.exambuilder.service.PuppeteerService;
import com.ebitik.exambuilder.service.Service;
import com.ebitik.exambuilder.type.ColumnType;
import com.ebitik.exambuilder.type.PaperType;
import com.ebitik.exambuilder.util.VelocityUtil;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

public class ExamBuilder {

	private VelocityEngine velocityEngine;

	PaperType paperType = PaperType.A4;

	String templatePath;

	Boolean copyProtection = Boolean.FALSE;

	ColumnType columnType = ColumnType.TWO_COLUMN;

	Boolean shuffle = Boolean.TRUE;

	Boolean addPageNumbers = Boolean.TRUE;

	String bigStamper = null;

	String smallStamper = null;

	String defaultHeader = null;
	
	String firstPageHeader = null;
	
	Service pdfService = new PuppeteerService();

	int fontSize = 12;

	List<Question> questionList;

	private String savePath;
	
	public ExamBuilder() {
		Properties prop = new Properties();
		prop.setProperty("resource.loader", "class");
		prop.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngine = new VelocityEngine(prop);
	}
	
	public void build() throws Exception {
		ExamPages examPages = new ExamPages(pdfService, columnType);
		
		//oncelikle headerin hemen altina bir bosulk ekleyelim.
		//questionList.add(0, new EmptySpace(50, paperType, columnType));
		
		for (Question question : questionList) {
			examPages.addQuestion(question);
			if(question instanceof Group) {
				List<Question> groupQuestions = ((Group) question).getQuestionList();
				for (Question gq : groupQuestions) {
					examPages.addQuestion(gq);
				}
			}
		}

		Map<String, Object> model = new HashMap<String, Object>();
		model.put ("pages", examPages.getPages());
		model.put ("css", columnType.css);
		model.put ("firstPageHeader", firstPageHeader);
		model.put ("defaultHeader", defaultHeader);
		model.put("showPageNumber", addPageNumbers);

		String tempFolder = System.getProperty("java.io.tmpdir");//Util.getParentFullPathResourceFolder("temp/not_delete.txt");
		String fileName = UUID.randomUUID().toString();
		String htmlFilePath = tempFolder+File.separator+fileName+".html";
		try (Writer writer = new FileWriter(htmlFilePath)) {
			VelocityUtil.mergeTemplate(velocityEngine, columnType.vm, "utf-8", model, writer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//String pdfPath = savePath;
		String pdfPath = tempFolder+File.separator+fileName+".pdf";
		if(StringUtils.isEmpty(pdfPath) || !pdfPath.endsWith(".pdf")) {
			pdfPath = fileName+".pdf";
		}
		pdfService.htmlToPdf(htmlFilePath, pdfPath);
		//PhantomService.htmlToPdf(tempFolder, fileName, pdfPath, addPageNumbers);

		//phantom dosyayi henuz olusturmamis olabilir. o nedenle 1 sn bekleyelim.
		if (!Files.isRegularFile(Paths.get(pdfPath))) {
			Thread.sleep(1000l);
		}
		manipulatePdf(savePath, pdfPath);
	}

	private void manipulatePdf(String savePath, String baseFilePath) throws Exception {
		//incase any parent folder is not exist
		FileUtils.forceMkdirParent(new File(savePath));
		//pdf reader
		PdfReader reader = new PdfReader(baseFilePath);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(savePath));
		//stamper.insertPage(reader.getNumberOfPages() + 1, reader.getPageSizeWithRotation(1));
		PdfWriter pdfWriter = stamper.getWriter();

		if (copyProtection) {
			pdfWriter.setEncryption(null, null, ~(PdfWriter.ALLOW_COPY), PdfWriter.STANDARD_ENCRYPTION_128);
			pdfWriter.createXmpMetadata();
		}

		InputStream template = null;
		PdfImportedPage templatePage = null;
		if(Objects.nonNull(templatePath)) {
			File templateFile = new File(templatePath);
			template = new FileInputStream(templateFile);
			PdfReader templateReader = new PdfReader(template);
			templatePage = pdfWriter.getImportedPage(templateReader, 1);
		}

		int pageCount = reader.getNumberOfPages();
		int startNumber = 0;

		for (int i = 1; i <= pageCount; i++) {
			PdfContentByte cb = stamper.getOverContent(i);
			if(templatePage != null) {
				cb.addTemplate(templatePage, 0, 0);
			}

			//add company info
			if(ColumnType.TWO_COLUMN.equals(columnType)) {
				addText(cb, 295, 450, 5, Font.NORMAL, Element.ALIGN_CENTER, 0.70f, Color.BLACK, "Erdal && Ahmet Company ® 2017", 90);
				addText(cb, 294, 410, 8, Font.NORMAL, Element.ALIGN_CENTER, 0.60f, Color.BLACK, String.join("", Collections.nCopies(85, "  .  ")), 90);
			} else {
				addText(cb, 570, 450, 6, Font.NORMAL, Element.ALIGN_CENTER, 0.70f, Color.BLACK, "Erdal && Ahmet Company ® 2017", 90);
			}
			
			//add name vs fields
			/*if(i == 1) {
				addText(cb, 30, 780, 10, Font.NORMAL, Element.ALIGN_LEFT, 1.0f, Color.BLACK, "Ad      :   . . . . . . . . . . . . . . . .");
				addText(cb, 30, 755, 10, Font.NORMAL, Element.ALIGN_LEFT, 1.0f, Color.BLACK, "Soyad   :   . . . . . . . . . . . . . .");
				addText(cb, 180, 780, 10, Font.NORMAL, Element.ALIGN_LEFT, 1.0f, Color.BLACK, "Numara :   . . . . . . . . . . . . . .");
				addText(cb, 180, 755, 10, Font.NORMAL, Element.ALIGN_LEFT, 1.0f, Color.BLACK, "Sınıf  :   . . . . . . . . . . . . . . . . .");
			}*/

			/*if(!StringUtils.isEmpty(headerText)) {
				addText(cb, 295, 810, 10, Font.NORMAL, Element.ALIGN_CENTER, 1.0f, Color.BLACK, headerText);
			}*/

			if(i == pageCount) {
				addText(cb, 510, 24, 9, Font.ITALIC, Element.ALIGN_CENTER, 0.5f, Color.BLACK, "Test Bitti");
			} else {
				addText(cb, 490, 24, 9, Font.ITALIC, Element.ALIGN_CENTER, 0.5f, Color.BLACK, "Diğer sayfaya geçiniz »");
			}

			//sayfa numaralari zaten var
			addText(cb, 297, 24, 11, Font.NORMAL, Element.ALIGN_CENTER, 1.0f, Color.BLACK, Integer.toString(startNumber + i));
			if(!StringUtils.isEmpty(bigStamper)) {
				addText(cb, 320, 400, 110, Font.NORMAL, Element.ALIGN_CENTER, 0.17f, Color.GRAY, bigStamper, 55);
			}

			if(!StringUtils.isEmpty(smallStamper)) {
				int y = 0;
				while(y < 900) {
					int x = 0;
					while(x < 600) {
						addText(cb, x, y, 10, Font.NORMAL, Element.ALIGN_CENTER, 0.18f, Color.GRAY, smallStamper, 47);
						x += 30; 
					}
					y += 60;
				}
			}
		}

		stamper.close();
		reader.close();
	}

	private static void addText(PdfContentByte cb, int x, int y, int punto, int style, int align,
			float opacity, Color color, String text) {
		addText(cb, x, y, punto, style, align, opacity, color, text, 0);
	}

	private static void addText(PdfContentByte cb, int x, int y, int punto, int style, int align,
			float opacity, Color color, String text, float rotation) {
		Font font = FontFactory.getFont("font/MyriadPro-Regular.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, punto);//FontFactory.getFont(fontFamily, "UTF-8", true);
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

	public ExamBuilder paperType(PaperType paperType) {
		this.paperType = paperType;
		return this;
	}

	public ExamBuilder templatePath(String templatePath) {
		this.templatePath = templatePath;
		return this;
	}

	public ExamBuilder copyProtection(Boolean copyProtection) {
		this.copyProtection = copyProtection;
		return this;
	}

	public ExamBuilder columnType(ColumnType columnType) {
		this.columnType = columnType;
		return this;
	}

	public ExamBuilder shuffle(Boolean shuffle) {
		this.shuffle = shuffle;
		return this;
	}

	public ExamBuilder addPageNumbers(Boolean addPageNumbers) {
		this.addPageNumbers = addPageNumbers;
		return this;
	}

	public ExamBuilder bigStamper(String bigStamper) {
		this.bigStamper = bigStamper;
		return this;
	}

	public ExamBuilder smallStamper(String smallStamper) {
		this.smallStamper = smallStamper;
		return this;
	}

	public ExamBuilder fontSize(int fontSize) {
		this.fontSize = fontSize;
		return this;
	}
	
	public ExamBuilder defaultHeader(String defaultHeader) {
		this.defaultHeader = defaultHeader;
		return this;
	}
	
	public ExamBuilder firstPageHeader(String firstPageHeader) {
		this.firstPageHeader = firstPageHeader;
		return this;
	}

	public ExamBuilder savePath(String savePath) {
		this.savePath = savePath;
		return this;
	}

	public ExamBuilder questionList(List<Question> questionList) {
		this.questionList = questionList;
		return this;
	}
	
	public ExamBuilder questionList(Service service) {
		this.pdfService = service;
		return this;
	}

}

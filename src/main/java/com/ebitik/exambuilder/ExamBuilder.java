package com.ebitik.exambuilder;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import com.ebitik.exambuilder.httl.ExamPages;
import com.ebitik.exambuilder.service.PuppeteerService;
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

	String headerText = null;

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
		ExamPages examPages = new ExamPages(columnType);
		
		//oncelikle headerin hemen altina bir bosulk ekleyelim.
		questionList.add(0, new EmptySpace(50, paperType, columnType));
		
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
		PuppeteerService.htmlToPdf(htmlFilePath, pdfPath);
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
		//int startNumber = 0;

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
			if(i == 1) {
				addText(cb, 30, 780, 10, Font.NORMAL, Element.ALIGN_LEFT, 1.0f, Color.BLACK, "Ad      :   . . . . . . . . . . . . . . . .");
				addText(cb, 30, 755, 10, Font.NORMAL, Element.ALIGN_LEFT, 1.0f, Color.BLACK, "Soyad   :   . . . . . . . . . . . . . .");
				addText(cb, 180, 780, 10, Font.NORMAL, Element.ALIGN_LEFT, 1.0f, Color.BLACK, "Numara :   . . . . . . . . . . . . . .");
				addText(cb, 180, 755, 10, Font.NORMAL, Element.ALIGN_LEFT, 1.0f, Color.BLACK, "Sınıf  :   . . . . . . . . . . . . . . . . .");
			}

			if(!StringUtils.isEmpty(headerText)) {
				addText(cb, 295, 810, 10, Font.NORMAL, Element.ALIGN_CENTER, 1.0f, Color.BLACK, headerText);
			}

			if(i == pageCount) {
				addText(cb, 510, 24, 9, Font.ITALIC, Element.ALIGN_CENTER, 0.5f, Color.BLACK, "Test Bitti");
			} else {
				addText(cb, 490, 24, 9, Font.ITALIC, Element.ALIGN_CENTER, 0.5f, Color.BLACK, "Diğer sayfaya geçiniz »");
			}

			//sayfa numaralari zaten var
			//addText(cb, 297, 24, 11, Font.NORMAL, Element.ALIGN_CENTER, "Arial", 1.0f, Color.BLACK, Integer.toString(startNumber + i));
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

	/*public void createPdf(OutputStream os) throws Exception {
		Document document = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(document, os);

		if (copyProtection) {
			writer.setEncryption(null, null, ~(PdfWriter.ALLOW_COPY), PdfWriter.STANDARD_ENCRYPTION_128);
			writer.createXmpMetadata();
		}

		document.open();

		// pdf template
		InputStream template = null;
		if(Objects.nonNull(templatePath)) {
			File templateFile = new File(templatePath);
			template = new FileInputStream(templateFile);
		} else {
			ClassLoader classLoader = ExamBuilder.class.getClassLoader();
			if(ColumnType.ONE_COLUMN.equals(columnType)) {
				template = classLoader.getResourceAsStream("/templates/one_column_template.pdf");
			} else {
				template = classLoader.getResourceAsStream("/templates/two_column_template.pdf");
			}
		}

		if(Objects.isNull(questionList)) {
			questionList = new ArrayList<>();
		}

		if(shuffle) {
			Collections.shuffle(questionList);
		}

		PdfReader reader = new PdfReader(template);
		PdfImportedPage page = writer.getImportedPage(reader, 1);

		List<PdfTable> questionTables = prepareQuestionTables();

		while (true) {
			PdfContentByte cb = writer.getDirectContent();
			PdfPTable mainTable = new PdfPTable(2);
			mainTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			mainTable.setKeepTogether(true);
			mainTable.setWidthPercentage(115.0f);

			// sol bolum
			PdfPTable soltable = getPdfTable(soruList, soruPdfTip, true);
			if (soltable != null) {
				mainTable.addCell(soltable);
			}

			// sag bolum
			PdfPTable sagtable = getPdfTable(soruList, soruPdfTip, false);
			if (sagtable != null) {
				mainTable.addCell(sagtable);
			}

			if (soltable != null || sagtable != null) {
				document.add(mainTable);
			}

			if(soruPdfTip.isPreview()) {
				//kisinin idsini bascaz
				int y = 0;
				while(y < 900) {
					int x = 0;
					while(x < 600) {
						addText(cb, x, y, 10, Font.NORMAL, Element.ALIGN_CENTER, "Arial", 0.18f, BaseColor.GRAY, ObjectUtils.convertString(SessionService.getBirey().getId()), 47);
						x += 30; 
					}
					y += 60;
				}
			} else {
				//kitapcik kodu
				addText(cb, 298, 772, 55, Font.BOLD, Element.ALIGN_CENTER, "Arial", 0.25f, BaseColor.BLACK, group);
				//sinav adi
				//addText(cb, 300, 750, 12, Font.NORMAL, Element.ALIGN_CENTER, "Myriad Pro", 0.6f, BaseColor.BLACK, sinavAdi);
			}
			//sinav adi
			addText(cb, 300, 758, 8, Font.NORMAL, Element.ALIGN_CENTER, "Myriad Pro", 0.6f, BaseColor.BLACK, StringUtil.getString(sinavAdi, 100));
			if(sinavAdi.length() > 100)
				addText(cb, 300, 748, 8, Font.NORMAL, Element.ALIGN_CENTER, "Myriad Pro", 0.6f, BaseColor.BLACK, sinavAdi.substring(100));

			cb.addTemplate(page, 0, 0);

			boolean soruVar = isExistProperSoruForPdf(soruList, soruPdfTip);
			if (!soruVar) {
				// addPageMessageToBottom(cb, 510, 23, 9, "Test Bitti");
				addText(cb, 510, 24, 9, Font.ITALIC, Element.ALIGN_CENTER, "Myriad Pro", 0.5f, BaseColor.BLACK,
						"Test Bitti");
				break;
			} else {
				// addPageMessageToBottom(cb, 490, 23, 9, "Diğer sayfaya geçiniz
				// »");
				addText(cb, 490, 24, 9, Font.ITALIC, Element.ALIGN_CENTER, "Myriad Pro", 0.5f, BaseColor.BLACK,
						"Diğer sayfaya geçiniz »");
			}

		}

		// step 5
		document.close();
		writer.close();


	}*/

	/*private List<PdfTable> prepareQuestionTables() {
		PdfPTable table = new PdfPTable(1);
		table.setTotalWidth(110);
		PdfPCell cell = new PdfPCell();
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setPaddingLeft(15);
		return null;
	}

	public static PdfPTable getSoruAsTable(Soru soru, Integer soruSira, SoruPdfTip soruPdfTip, boolean sol)
			throws Exception {
		try {
			PdfPTable table = new PdfPTable(1);
			table.setTotalWidth(110);
			PdfPCell cell = new PdfPCell();
			cell.setBorder(PdfPCell.NO_BORDER);
			if (sol) {
				cell.setPaddingLeft(25);
			} else {
				cell.setPaddingLeft(0);
			}

			// soru metni ve cevaplar merge edilir
			String soruMetin = soru.getXhtml();
			if (StringUtil.isEmpty(soruMetin))
				return null;
			String soruSiraStr = "";
			if (SoruTip.COKTAN_SECMELI.equals(soru.getSoruTip())) {
				soruSiraStr = soruSira.toString();
				soru.setSoruPdfSira(soruSira);
				soruMetin = soruMetin.replace("${{soruSira}}", soruSiraStr);
			} else if (SoruTip.GRUPLU.equals(soru.getSoruTip())) {
				List<Soru> altSoruList = soru.getAltSoruList();
				int size = CollectionUtils.getSize(altSoruList);
				if (size == 0) {
					soruSiraStr = "";
				} else {
					soruSiraStr = getGrupluSoruSiraString(soruSira, size);
				}
				soruMetin = soruMetin.replace("${{SORULARINI}}", soruSiraStr);
			} else if (SoruTip.KLASIK.equals(soru.getSoruTip())) {
				soruSiraStr = soruSira.toString();
				soru.setSoruPdfSira(soruSira);
				soruMetin = soruMetin.replace("${{soruSira}}", soruSiraStr);
			}

			// soru eklenir. //css burdan verilebilir.
			String css = soruPdfTip.getCss();
			ElementList parseToElementList = XMLWorkerHelper.parseToElementList(soruMetin, css);

			for (Element el : parseToElementList) {
				cell.addElement(el);
			}
			// cell.setExtraParagraphSpace(5);
			table.addCell(cell);

			return table;
		} catch (Exception e) {
			// hata alirsa bir daha sorulmasin
			soru.setPdfHeight(null);
			System.out.println("Hata: "+e);
		}
		return null;
	}*/

	private String htmlToXhtml(String html) throws Exception {
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

			return fos.toString("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
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

	public ExamBuilder headerText(String headerText) {
		this.headerText = headerText;
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

}

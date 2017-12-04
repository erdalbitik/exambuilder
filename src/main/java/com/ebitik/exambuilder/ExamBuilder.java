package com.ebitik.exambuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import com.ebitik.exambuilder.httl.ExamPages;
import com.ebitik.exambuilder.util.Util;
import com.ebitik.exambuilder.util.VelocityUtil;

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

	String header = null;

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
		ExamPages examPages = new ExamPages();
		for (Question question : questionList) {
			examPages.addQuestion(question.getAsXHTML(true), question.getHeight());
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put ("pages", examPages.getPages());
		model.put ("css", columnType.css);
		
		String tempFolder = System.getProperty("java.io.tmpdir");//Util.getParentFullPathResourceFolder("temp/not_delete.txt");
		String fileName = UUID.randomUUID().toString();
		String htmlFilePath = tempFolder+File.separator+fileName+".html";
		try (Writer writer = new FileWriter(htmlFilePath)) {
			VelocityUtil.mergeTemplate(velocityEngine, "/templates/two_column.vm", "utf-8", model, writer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String pdfPath = savePath;
		if(StringUtils.isEmpty(pdfPath) || !pdfPath.endsWith(".pdf")) {
			pdfPath = fileName+".pdf";
		}
		//phantomjs cagir
		String line = "phantomjs " +Util.getFileFullPathResourceFolder("phantomjs/html2pdf.js")+ " "+fileName +".html "+pdfPath;
		DefaultExecutor exec = new DefaultExecutor();
		exec.setWorkingDirectory(new File(tempFolder));
		exec.execute(CommandLine.parse(line));
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

	public ExamBuilder header(String header) {
		this.header = header;
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

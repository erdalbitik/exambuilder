package com.test.exambuilder.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;

import org.junit.Test;

import com.ebitik.exambuilder.Choice;
import com.ebitik.exambuilder.ColumnType;
import com.ebitik.exambuilder.Essay;
import com.ebitik.exambuilder.ExamBuilder;
import com.ebitik.exambuilder.Group;
import com.ebitik.exambuilder.MultipleChoice;
import com.ebitik.exambuilder.PaperType;
import com.ebitik.exambuilder.Question;
import com.ebitik.exambuilder.service.PuppeteerService;

public class MultipleChoiceTest {
	
	Random random = new Random();

	/*@Test
	public void getHtmlTable() throws Exception {
		List<Choice> choiceList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Choice choice = new Choice();
			choice.setText("Ben var gitmek "+i);
			choiceList.add(choice);
		}
		MultipleChoice question = new MultipleChoice("12", PaperType.A4, ColumnType.TWO_COLUMN, "Ben kaç yaşımdayım?", choiceList);
		System.out.println(question.getAsPDFPTable());
	}
	
	@Test
	public void getHtmlTableHeight() throws Exception {
		List<Choice> choiceList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Choice choice = new Choice();
			choice.setText("Ben var gitmek "+i);
			choiceList.add(choice);
		}
		MultipleChoice question = new MultipleChoice("12", PaperType.A4, ColumnType.TWO_COLUMN, "Ben kaç yaşımdayım?", choiceList);
		System.out.println(question.getAsPDFPTable().getTotalHeight());
	}
	
	@Test
	public void printHtmlTableAsPdf() throws Exception {
		Document document = new Document(PageSize.A4);
		try (OutputStream os = new FileOutputStream("test.pdf")) {
			PdfWriter writer = PdfWriter.getInstance(document, os);
			document.open();
			document.newPage();
			PdfReader reader = new PdfReader("/templates/two_column_blank_page.pdf");
			PdfImportedPage page = writer.getImportedPage(reader, 1);
			PdfContentByte cb = writer.getDirectContent();
			PdfPTable mainTable = new PdfPTable(2);
			mainTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			mainTable.setKeepTogether(true);
			mainTable.setWidthPercentage(115.0f);
			mainTable.addCell(getQuestion().getAsPDFPTable());
			document.add(mainTable);
			cb.addTemplate(page, 0, 0);
			document.close();
			writer.close();
		}
	}*/
	
	///@Test
	public void getSoruHeight() throws Exception {
		String xhtml = getQuestion().getAsXHTML(false);
		int elementHeight = PuppeteerService.getQuestionTableHeight(xhtml);
		System.out.println(elementHeight);
	}
	
	///@Test
	public void createCiftKolonHtml() throws Exception {
		List<Question> qList = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			Question question = getRandomQuestion(i+1, ColumnType.TWO_COLUMN);
			qList.add(question);
		}
		ExamBuilder eb = new ExamBuilder();
		eb.questionList(qList);
		eb.savePath("C:\\dev\\sil\\bih.pdf");
		//eb.templatePath(Util.getFilePathFromResourceFolder("templates/two_column_blank_page.pdf"));
		eb.defaultHeader("MEHMETÇİK SELEN İLKOKULU FEN BİLGİSİ SINAVI 2017");
		eb.build();
	}
	
	@Test
	public void createTekKolonHtml() throws Exception {
		List<Question> qList = new ArrayList<>();
		
		Question que = new Essay("1", PaperType.A4, ColumnType.ONE_COLUMN, 300, "Osmanlının yıkılışını anlatınız?");
		qList.add(que);
		
		for (int i = 1; i <= 6; i++) {
			Question question = getRandomQuestion(i+1, ColumnType.ONE_COLUMN);
			qList.add(question);
		}
		
		List<Question> groupQList = new ArrayList<>();
		for (int i = qList.size(); i < qList.size()+4; i++) {
			Question question = getRandomQuestion(i+1, ColumnType.ONE_COLUMN);
			groupQList.add(question);
		}
		
		Group group = new Group("5", PaperType.A4, ColumnType.ONE_COLUMN, "Aşağıdaki soruları cevaplayınız?", groupQList);
		qList.add(group);
		
		ExamBuilder eb = new ExamBuilder();
		eb.columnType(ColumnType.ONE_COLUMN);
		eb.questionList(qList);
		eb.savePath("C:\\dev\\sile\\bih1.pdf");
		eb.copyProtection(Boolean.TRUE);
		eb.smallStamper("erdal");
		eb.bigStamper("CUMALI");
		eb.defaultHeader("MEHMETÇİK SELEN İLKOKULU FEN BİLGİSİ SINAVI 2017");
		//eb.templatePath(Util.getFilePathFromResourceFolder("templates/two_column_blank_page.pdf"));
		eb.build();
	}
	
	//@Test
	public void createCiftKolonFullHtml() throws Exception {
		List<Question> qList = new ArrayList<>();
		
		Question que = new Essay("1", PaperType.A4, ColumnType.TWO_COLUMN, 300, "19. yy Osmanlının mimarisini anlatınız?");
		qList.add(que);
		
		for (int i = 1; i <= 19; i++) {
			Question question = getRandomQuestion(i+1, ColumnType.TWO_COLUMN);
			qList.add(question);
		}
		
		Question q20 = getRandomQuestion(21, ColumnType.TWO_COLUMN, 100);
		qList.add(q20);
		
		List<Question> groupQList = new ArrayList<>();
		StringJoiner groupSira = new StringJoiner(", ");
		for (int i = qList.size(); i < qList.size()+4; i++) {
			Question question = getRandomQuestion(i+1, ColumnType.TWO_COLUMN);
			groupSira.add((i+1)+"");
			groupQList.add(question);
		}
		
		Group group = new Group(groupSira.toString()+" sorularını aşağıda verilen metine göre cevaplayınız.", PaperType.A4, ColumnType.TWO_COLUMN, "Aşağıdaki gruplanmış soruları cevaplayınız?", groupQList);
		qList.add(group);
		
		ExamBuilder eb = new ExamBuilder();
		eb.columnType(ColumnType.TWO_COLUMN);
		eb.questionList(qList);
		eb.savePath("C:\\dev\\sile\\bih.pdf");
		eb.copyProtection(Boolean.TRUE);
		eb.smallStamper("erdal.bitik");
		eb.bigStamper("WATERMARK");
		eb.defaultHeader("MEHMETÇİK SELEN İLKOKULU 7/A FEN BİLGİSİ SINAVI 2017");
		//eb.templatePath(Util.getFilePathFromResourceFolder("templates/two_column_blank_page.pdf"));
		eb.build();
	}
	
	@Test
	public void createCiftKolonLowHtml() throws Exception {
		List<Question> qList = new ArrayList<>();
		
		Question que = new Essay("1", PaperType.A4, ColumnType.TWO_COLUMN, 300, "Osmanlının yıkılışını anlatınız?");
		qList.add(que);
		
		for (int i = 1; i <= 20; i++) {
			Question question = getSemiRandomQuestion(i+1, ColumnType.TWO_COLUMN);
			qList.add(question);
		}
		
		List<Question> groupQList = new ArrayList<>();
		StringJoiner groupSira = new StringJoiner(", ");
		for (int i = qList.size(); i < qList.size()+4; i++) {
			Question question = getSemiRandomQuestion(i+1, ColumnType.TWO_COLUMN);
			groupSira.add((i+1)+"");
			groupQList.add(question);
		}
		
		Group group = new Group(groupSira.toString()+" sorularını aşağıda verilen metine göre cevaplayınız.", PaperType.A4, ColumnType.TWO_COLUMN, "Aşağıdaki gruplanmış soruları cevaplayınız?", groupQList);
		qList.add(group);
		
		
		ExamBuilder eb = new ExamBuilder();
		eb.columnType(ColumnType.TWO_COLUMN);
		eb.questionList(qList);
		eb.savePath("C:\\dev\\sile\\bih.pdf");
		eb.copyProtection(Boolean.TRUE);
		eb.smallStamper("erdal.bitik");
		eb.bigStamper("WATERMARK");
		eb.defaultHeader("MEHMETÇİK SELEN İLKOKULU 7/A FEN BİLGİSİ SINAVI 2017");
		eb.firstPageHeader("CUMALI BAYER İLKOKULU 7/A FEN BİLGİSİ SINAVI 2017");
		//eb.templatePath(Util.getFilePathFromResourceFolder("templates/two_column_blank_page.pdf"));
		eb.build();
	}
	
	///@Test
	public void createTekKolonEssayHtml() throws Exception {
		List<Question> qList = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			Question question = getRandomQuestion(i+1, ColumnType.ONE_COLUMN);
			qList.add(question);
		}
		ExamBuilder eb = new ExamBuilder();
		eb.columnType(ColumnType.ONE_COLUMN);
		eb.questionList(qList);
		eb.savePath("C:\\dev\\sil\\bih.pdf");
		eb.copyProtection(Boolean.TRUE);
		eb.smallStamper("erdal");
		eb.bigStamper("CUMALI");
		eb.defaultHeader("MEHMETÇİK SELEN İLKOKULU FEN BİLGİSİ SINAVI 2017");
		//eb.templatePath(Util.getFilePathFromResourceFolder("templates/two_column_blank_page.pdf"));
		eb.build();
	}
	
	private Question getQuestion() {
		List<Choice> choiceList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Choice choice = new Choice();
			choice.setText("Ben var gitmek "+i);
			choiceList.add(choice);
		}
		return new MultipleChoice("12", PaperType.A4, ColumnType.TWO_COLUMN, 60, "Ben kaç yaşımdayım?", choiceList);
	}
	
	String[] soruList = {
			". Any good company needs Direct competitors!Copycats validate your startup’s business. Their existence",
			"indicates that there’s money in the market you’re trying to capture and is worth fighting for. That is why",
			"VCs always ask ‘Who are your competitors?’. If you have no competitors, smart people will always question if",
			"you have attained the right product market fit and whether your business is sustainable. You should ask these",
			"questions yourself as well.Another player copying your startup idea means another set of smart folks building a",
			"product, and marketing it to generate a buzz around their product in the market. Thereby expanding the market. They",
			"will reach out to customers you might not have thought of yet and thus help you identify new potential customers. They",
			"will end up energizing the market and boosting awareness about the problem that your product is trying to solve. By doing",
			"so, your copycat becomes an extension of your marketing efforts for free!1Any founder worth their salt  will tell you that",
			"when you have others replicating your efforts, you know that your product will resonate with a wider audience.Direct competition",
			"is war, but it provides validity to your argument, creates awareness and removes the blind spots.Learn from themYour copycats might",
			"resemble you but they will never be you. They know nothing about the customer psyche and the struggles needed to get where you are. Welcome",
			"them and let them make their own mistakes.The copycat will often explore and make the same mistakes that you did. While you have",
			"iteratively developed your Startup over time learning what works and what doesn’t, the copycat will either get stuck making the",
			"same mistakes and die, or will make new ones that you ought to learn from. Follow every move they make. These moves will give",
			"you new ideas, challenge your assumptions and point to the shortcomings of your own product. Copycats will bring you insights",
			"into the market, existing customers, partners and other potential customers.Embrace them, learn from them, because they don’t",
			"hurt your business, instead they’ll help you grow.1Counter them. Some Strategies:Embracing copycats and learning from them is",
			"one thing, but never let them hurt your business in unscrupulous ways. Don’t let them steal your customers, don’t let them",
			"steal your team. Investors also look for companies with a strong competitive moat. “What is a competitor starts doing what",
			"you do?”, “Why are you best positioned to execute this idea?”. Copying a product is the biggest compliment another founder",
			"can give you. Enjoy the flattery then continue revolutionizing.Create High barriers to entry. Get copyrights, & patents",
			"in key markets. Patent key components of your software.Sign ‘Exclusives’ with key partners.Buy domains and protect your",
			"trademark in key markets as early as possible.Have a large but clear vision on what you want the company to become.",
			"Keep executing on that path. If your vision is bigger than a feature, copycats won’t be able to catch up.Keep innovating.",
			"Invest in R&D and build proprietary stuff that can not easily be copied. Follow industry news to stay alert and updated",
			"about the happenings in the industry.If it goes too far, Consult a lawyer, send a ‘Cease and desist’ letter, and elevate",
			"to a lawsuit if necessary.Stick to your prices but offer discount/deals/annual plans to acquire new customers faster.",
			"Talk to your existing customers more & understand their pain points. Make sure they understand that you’re working",
			"hard to solve them.Research about the competitor key strength and weaknesses and hit them where it hurts1.Take",
			"action as early as possible. Use StartupFlux to get notified if a new company enters your Competitors list.What",
			"do you think?Have you ever had your Startup Idea stolen? Have you ever had any copycats? What did you do about",
			"it?1 Did you panic or embraced it with confidence that you can stand your ground and perhaps even learn from",
			"Bu soruda imaj yok. <img src='https://www.w3schools.com/html/img_chania.jpg' alt='Cinque Terre'>",
			"<h2>Necessary ye contented newspaper zealously breakfast he prevailed</h2><p>He my polite be object oh change. Consider no mr am overcame yourself throwing sociable children. Hastily her totally conduct may. My solid by stuff first smile fanny. Humoured how advanced mrs elegance sir who. Home sons when them dine do want to. Estimating themselves unsatiable imprudence an he at an. Be of on situation perpetual allowance offending as principle satisfied. Improved carriage securing are desirous too.</p>"};
	
	String[] cevapList = {
			"الأَبْجَدِيَّة العَرَبِيَّة‎ Bu bir arapça metin. الحُرُوف العَرَبِيَّة",
			"الأَبْجَدِيَّة العَرَبِيَّة‎ Bu bir arapça metin1. الحُرُوف العَرَبِيَّة",
			"الأَبْجَدِيَّة العَرَبِيَّة‎ Bu bir arapça metin2. الحُرُوف العَرَبِيَّة",
			"الأَبْجَدِيَّة العَرَبِيَّة‎ Bu bir arapça metin3. الحُرُوف العَرَبِيَّة",
			"الأَبْجَدِيَّة العَرَبِيَّة‎ Bu bir arapça metin4. الحُرُوف العَرَبِيَّة",
			"الأَبْجَدِيَّة العَرَبِيَّة‎ Bu bir arapça metin5. الحُرُوف العَرَبِيَّة",
			"الأَبْجَدِيَّة العَرَبِيَّة‎ Bu bir arapça metin6. الحُرُوف العَرَبِيَّة",
			"الأَبْجَدِيَّة العَرَبِيَّة‎ Bu bir arapça metin7. الحُرُوف العَرَبِيَّة",
			"Log4JLogChute initialized using file",
			"Initializing Velocity, Calling init",
			"Starting Apache Velocity v1.7",
			"Trying to use logger class org.apache.velocity.runtime.log.AvalonLogChute",
			"Loaded System Directive",
			"org.apache.velocity.runtime.directive",
			"Log4JLogChute - ResourceLoader instantiated",
			"Could not load resource 'VM_global_library.vm'",
			"n the course of building StartupFlux, I have interacted with many smart",
			"entrepreneurs who are scared of other people just catching wind of their",
			"idea. They don’t even want to disclose their business idea and are always",
			"thinking – “What if someone starts copying your Business idea?”. Ones that",
			"reveal their ideas are deathly scared – “What if copycats go faster than me?”.",
			"This is also true for me – The thought that what if a competitor starts copying",
			"our idea comes to my mind all the time. Indian startups are also infamous as",
			"copycats of the west.Having copycats is inevitable. When they can’t innovate,",
			"they copy. Sometimes we assume that established competitors are focusing on X",
			"and we are focusing on Y, but as growth becomes harder to come by and they",
			"struggle to increase revenues, copying your idea is always a possibility. When",
			"a new business idea is executed successfully, other businesses naturally imitate",
			"and emerge.  Take it from the traditional business market, there are thousands of",
			"businesses doing exactly the same thing and making a good living off it. So there’s nothing",
			"Starting Apache Velocity v1.7",
			"Trying to use logger class org.apache.velocity.runtime.log.AvalonLogChute",
			"Loaded System Directive",
			"org.apache.velocity.runtime.directive",
			"Log4JLogChute - ResourceLoader instantiated",
			"Could not load resource 'VM_global_library.vm'",
			"n the course of building StartupFlux, I have interacted with many smart",
			"entrepreneurs who are scared of other people just catching wind of their",
			"idea. They don’t even want to disclose their business idea and are always",
			"thinking – “What if someone starts copying your Business idea?”. Ones that",
			"reveal their ideas are deathly scared – “What if copycats go faster than me?”.",
			"This is also true for me – The thought that what if a competitor starts copying",
			"our idea comes to my mind all the time. Indian startups are also infamous as",
			"copycats of the west.Having copycats is inevitable. When they can’t innovate,",
			"they copy. Sometimes we assume that established competitors are focusing on X",
			"and we are focusing on Y, but as growth becomes harder to come by and they",
			"struggle to increase revenues, copying your idea is always a possibility. When",
			"a new business idea is executed successfully, other businesses naturally imitate",
			"and emerge.  Take it from the traditional business market, there are thousands of",
			"businesses doing exactly the same thing and making a good living off it. So there’s nothing",
			"Starting Apache Velocity v1.7",
			"Trying to use logger class org.apache.velocity.runtime.log.AvalonLogChute",
			"Loaded System Directive",
			"org.apache.velocity.runtime.directive",
			"Log4JLogChute - ResourceLoader instantiated",
			"Could not load resource 'VM_global_library.vm'",
			"n the course of building StartupFlux, I have interacted with many smart",
			"entrepreneurs who are scared of other people just catching wind of their",
			"idea. They don’t even want to disclose their business idea and are always",
			"thinking – “What if someone starts copying your Business idea?”. Ones that",
			"reveal their ideas are deathly scared – “What if copycats go faster than me?”.",
			"This is also true for me – The thought that what if a competitor starts copying",
			"our idea comes to my mind all the time. Indian startups are also infamous as",
			"copycats of the west.Having copycats is inevitable. When they can’t innovate,",
			"they copy. Sometimes we assume that established competitors are focusing on X",
			"and we are focusing on Y, but as growth becomes harder to come by and they",
			"struggle to increase revenues, copying your idea is always a possibility. When",
			"a new business idea is executed successfully, other businesses naturally imitate",
			"and emerge.  Take it from the traditional business market, there are thousands of",
			"businesses doing exactly the same thing and making a good living off it. So there’s nothing",
			"to be scared of.Embrace themAs the saying goes, Imitation is the sincerest form of flattery3",
			"Necessary ye contented newspaper zealously breakfast he prevailed<p>He my polite bto. Estimating themselves unsatiable imprudence an he at an. Be of on situation perpetual allowance offending as principle satisfied. Improved carriage securing are desirous too.</p>"};
	
	
	
	private Question getRandomQuestion(int number, ColumnType columnType) {
		return new MultipleChoice(number+"", PaperType.A4, columnType, 60, soruList[random.nextInt(soruList.length)], getRandomChooseList());
	}
	
	private Question getRandomQuestion(int number, ColumnType columnType, int height) {
		return new MultipleChoice(number+"", PaperType.A4, columnType, height, soruList[random.nextInt(soruList.length)], getRandomChooseList());
	}
	
	private Question getSemiRandomQuestion(int number, ColumnType columnType) {
		return new MultipleChoice(number+"", PaperType.A4, columnType, 60, soruList[random.nextInt(1)], getSemiRandomChooseList());
	}
	
	private List<Choice> getRandomChooseList() {
		List<Choice> choiceList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Choice choice = new Choice();
			choice.setText(cevapList[random.nextInt(cevapList.length)]);
			choiceList.add(choice);
		}
		return choiceList;
	}
	
	private List<Choice> getSemiRandomChooseList() {
		List<Choice> choiceList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Choice choice = new Choice();
			choice.setText(cevapList[random.nextInt(1)]);
			choiceList.add(choice);
		}
		return choiceList;
	}
	
}

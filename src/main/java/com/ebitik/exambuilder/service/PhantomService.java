package com.ebitik.exambuilder.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.ebitik.exambuilder.util.Util;


public class PhantomService implements Service {
	
	public int getQuestionTableHeight(String html) throws Exception {
		
		//oncelikle htmli dosya olarak kaydedelim.
		String tempFolder = System.getProperty("java.io.tmpdir");//Util.getParentFullPathResourceFolder("temp/not_delete.txt");
		String fileName = UUID.randomUUID().toString();
		String htmlFilePath = tempFolder+File.separator+fileName+".html";
		FileUtils.writeStringToFile(new File(htmlFilePath), html,  StandardCharsets.UTF_8);
		
		//simdi dosyayi phantomlayalim
		String line = "phantomjs " +Util.getFileFullPathResourceFolder("phantomjs/height.js")+ " "+fileName +".html";
		//String line = "node " +Util.getFileFullPathResourceFolder("puppeteer/height.js")+ " "+htmlFilePath;
		//DefaultExecutor exec = new DefaultExecutor();
		//exec.setWorkingDirectory(new File(tempFolder));
		//int exitValue = exec.execute(CommandLine.parse(line));
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    CommandLine commandline = CommandLine.parse(line);
	    DefaultExecutor exec = new DefaultExecutor();
	    exec.setWorkingDirectory(new File(tempFolder));
	    PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
	    exec.setStreamHandler(streamHandler);
	    exec.execute(commandline);
	    String height = outputStream.toString();
		
		//String height = exec.getStdOut();
		height = height.replaceAll("(\\r|\\n)", "");
		if(StringUtils.isNumeric(height)) {
			//System.out.println(height);
			return Integer.parseInt(height);
		}
		return 0;
	}

	@Override
	public void htmlToPdf(String htmlFilePath, String pdfPath) throws Exception {
		String line = "phantomjs " +Util.getFileFullPathResourceFolder("phantomjs/html2pdf.js")+ " "+new File(htmlFilePath).getName() +".html "+pdfPath+ " "+true;
		DefaultExecutor exec = new DefaultExecutor();
		exec.setWorkingDirectory(new File(Util.getParentFullPathResourceFolder(htmlFilePath)));
		exec.execute(CommandLine.parse(line));
	}

}

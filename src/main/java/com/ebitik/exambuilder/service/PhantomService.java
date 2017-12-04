package com.ebitik.exambuilder.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.ebitik.exambuilder.util.Util;

public class PhantomService {
	
	public static int getHtmlElementHeight(String html) throws Exception {
		
		//oncelikle htmli dosya olarak kaydedelim.
		String tempFolder = System.getProperty("java.io.tmpdir");//Util.getParentFullPathResourceFolder("temp/not_delete.txt");
		String fileName = UUID.randomUUID().toString();
		String htmlFilePath = tempFolder+File.separator+fileName+".html";
		FileUtils.writeStringToFile(new File(htmlFilePath), html,  StandardCharsets.UTF_8);
		
		//simdi dosyayi phantomlayalim
		String line = "phantomjs " +Util.getFileFullPathResourceFolder("phantomjs/height.js")+ " "+fileName +".html";
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
			System.out.println(height);
			return Integer.parseInt(height);
		}
		return 0;
	}
	
	private static InputStream getScript(String html) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("var page = require('webpage').create();									");	
		sb.append("page.content = \"%s\";													");				
		sb.append("page.onConsoleMessage = function(msg) {  								");				
		sb.append(" console.log(msg);   													");				
		sb.append("};                                       								");				
		sb.append("page.evaluate(function() {             									");	
		//sb.append("   var tableElement = document.getElementById('questionTable');			");
		//sb.append("   if(tableElement != undefined)											");
		//sb.append("   	console.log(tableElement.offsetHeight);								");
		//sb.append("   else																	");
		//sb.append("   	console.log(0);														");
		sb.append("   	console.log(document.body.offsetHeight);														");
		sb.append("});                                    									");			
		sb.append("phantom.exit();                      									");
		String str = sb.toString();
		html = html.replaceAll("\"", "'");
		html = html.replaceAll("\r*\n", " ");
		str = String.format(str, html);
		InputStream stream = new ByteArrayInputStream(str.getBytes("UTF-8"));
		return stream;
	}
	
	private static InputStream getImageRenderScript(String html) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("var page = require('webpage').create();									");	
		sb.append("page.content = \"%s\";													");
		sb.append("page.evaluate(function() {             									");	
		sb.append("   var tableElement = document.getElementById('questionTable');			");
		sb.append("   if(tableElement != undefined)											");
		sb.append("   	console.log(tableElement.offsetWidth);								");
		sb.append("   else																	");
		sb.append("   	console.log(0);														");
		sb.append("});                                    									");			
		sb.append("phantom.exit();                      									");
		String str = sb.toString();
		html = html.replaceAll("\"", "'");
		html = html.replaceAll("\r*\n", " ");
		str = String.format(str, html);
		InputStream stream = new ByteArrayInputStream(str.getBytes("UTF-8"));
		return stream;
	}


}

package com.test.exambuilder.test;

import org.junit.Test;

import com.ebitik.exambuilder.service.PhantomService;

public class PhantomTest {
	
	@Test
	public void test() throws Exception {
		String htmlElementHeight = PhantomService.getHtmlElementHeight();
		System.out.println(htmlElementHeight);
	}

}

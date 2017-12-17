package com.test.exambuilder.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ebitik.exambuilder.service.Service;

public class CommandLineTest {

	@Mock
	private Service serviceMock;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testIfServiceWorking() throws Exception {
		when(serviceMock.getQuestionTableHeight("")).thenReturn(10);
		
		int questionTableHeight = serviceMock.getQuestionTableHeight("");
		assertEquals(10, questionTableHeight);
	}


}

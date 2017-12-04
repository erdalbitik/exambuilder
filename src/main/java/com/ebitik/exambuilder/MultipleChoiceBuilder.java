package com.ebitik.exambuilder;

import java.util.LinkedHashSet;
import java.util.Set;

public class MultipleChoiceBuilder {
	String questionText;
	
	Set<String> choiceTextList = new LinkedHashSet<>();
	
	public MultipleChoiceBuilder questionText(String questionText) {
		this.questionText = questionText;
		return this;
	}
	
	public MultipleChoiceBuilder addChoice(String choice) {
		choiceTextList.add(choice);
		return this;
	}
	
	public MultipleChoice build() {
		MultipleChoice mc = new MultipleChoice();
		for (String cs : choiceTextList) {
			Choice choice = new Choice();
			choice.setText(cs);
			mc.addChoice(choice);
		}
		return mc;
	}
	
}

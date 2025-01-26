package model.view.controller;

import java.util.Scanner;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class Controller extends AMapReduceTracer {
	private final Model model;
	private static final String EXIT_MESSAGE = "quit"; 
	
	public Controller(final Model m) {
		this.model = m;
	}
	
	public void gatherInput(final Scanner scanner, final boolean isSum) {
		while (true) {
			super.traceNumbersPrompt();
			
			final String firstLine = scanner.nextLine();
			
			if (EXIT_MESSAGE.equals(firstLine)) {
				break;
			}
			
			model.setInputString(firstLine);
			
			if (isSum) {
				model.findNewResultSum(firstLine);
			} else {			
				model.findNewResult(firstLine);
			}
		}
	}
	
	@Override
	public String toString() {
		return this.CONTROLLER;
	}
}

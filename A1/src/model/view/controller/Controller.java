package model.view.controller;

import java.util.Scanner;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class Controller extends AMapReduceTracer {
	private Model model;
	private static final String EXIT_MESSAGE = "quit"; 
	
	public Controller(Model model) {
		this.model = model;
	}
	
	public void gatherInput(Scanner scanner) {
		while (true) {
			super.traceNumbersPrompt();
			
			String firstLine = scanner.nextLine();
			
			if (firstLine.equals(EXIT_MESSAGE)) {
				break;
			}
			
			model.setInputString(firstLine);
		}
	}
	
	public String toString() {
		return super.CONTROLLER;
	}
}

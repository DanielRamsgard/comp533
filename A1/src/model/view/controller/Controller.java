package model.view.controller;

import java.util.Scanner;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class Controller extends AMapReduceTracer implements ControllerInterface {
	private final Model model;
	private static final String EXIT_MESSAGE = "quit"; 
	
	public Controller(final Model m) {
		this.model = m;
	}
	
	public void gatherInputFromScanner(final Scanner scanner, final boolean isSum) {
		super.traceThreadPrompt();
		
		final int numThreads = scanner.nextInt();
		
		model.setNumThreads(numThreads);
		
		while (true) {
			super.traceNumbersPrompt();
			
			final String firstLine = scanner.nextLine();
			
			if (EXIT_MESSAGE.equals(firstLine)) {
				super.traceQuit();
				break;
			}
			
			model.setInputString(firstLine);
			
			if (isSum) {
				model.findNewResultSum(firstLine);
			} else {			
				model.findNewResult(firstLine);
			}
		}
		
		scanner.close();
	}
	
	@Override
	public String toString() {
		return super.CONTROLLER;
	}
}

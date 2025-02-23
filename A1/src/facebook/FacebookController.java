package facebook;

import java.util.Scanner;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class FacebookController extends AMapReduceTracer {
	private final FacebookModel facebookModel;
	private static final String EXIT_MESSAGE = "quit"; 
	
	public FacebookController(final FacebookModel m) {
		this.facebookModel = m;
	}
	
	public void gatherInputFromScanner(final Scanner scanner) {
		
		super.traceThreadPrompt();
		
		final int numThreads = scanner.nextInt();
		scanner.nextLine();
		
		facebookModel.setNumThreads(numThreads);
		
		while (true) {
			super.traceNumbersPrompt();
			
			final String firstLine = scanner.nextLine();
			
			if (EXIT_MESSAGE.equals(firstLine)) {
				super.traceQuit();
				break;
			}			
			
			facebookModel.setInputString(firstLine);
		}
		
		facebookModel.terminate();
		super.traceQuit();
		
		scanner.close();
	}
	
	@Override
	public String toString() {
		return super.CONTROLLER;
	}
}

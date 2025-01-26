package standalone.token.counter;

import java.util.HashMap;
import java.util.Scanner;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class StandaloneTokenCounter extends AMapReduceTracer {
	private static final String EXIT_MESSAGE = "quit"; 
	
	public void runLogic() {
		final Scanner scanner = new Scanner(System.in);
		
		while (true) {
			super.traceNumbersPrompt();
			
			final String firstLine = scanner.nextLine();
			
			if (EXIT_MESSAGE.equals(firstLine)) {
				break;
			}
			
			final String[] myList = firstLine.split(" ");
			final HashMap<String, Integer> myMap = new HashMap<>();
			String finalOuput = "";
			
			for (int i = 0; i < myList.length; i++) {
				final String currentString = myList[i];
				if (myMap.containsKey(currentString)) {
					myMap.put(currentString, myMap.get(currentString) + 1);
				} else {
					myMap.put(currentString, 1);
				}
			}
			
			for (int i = 0; i < myList.length; i++) {
				final String currentStringToOutput = myList[i];
				
				if (myMap.containsKey(currentStringToOutput)) {
					finalOuput += (currentStringToOutput + "=" + myMap.get(currentStringToOutput));					
					
					if (myMap.size() != 1) {
						finalOuput += ", ";
					}
					
					myMap.remove(currentStringToOutput);
				}				
			}
					
			super.trace(finalOuput);
			
		}				

		scanner.close();
	}
	
	public static void main(final String[] args) {
		final StandaloneTokenCounter counter = new StandaloneTokenCounter();
		counter.runLogic();
	}
	
}

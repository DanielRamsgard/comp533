package standalone.token.counter;

import java.util.*;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class StandaloneTokenCounter extends AMapReduceTracer {
	private static final String EXIT_MESSAGE = "quit"; 
	
	public void runLogic() {
		Scanner scanner = new Scanner(System.in);
		
		while (true) {
			super.traceNumbersPrompt();
			
			String firstLine = scanner.nextLine();
			
			if (firstLine.equals(EXIT_MESSAGE)) {
				break;
			}
			
			String[] myList = firstLine.split(" ");
			HashMap<String, Integer> myMap = new HashMap<>();
			String finalOuput = "";
			
			for (int i = 0; i < myList.length; i++) {
				String currentString = myList[i];
				if (myMap.containsKey(currentString)) {
					myMap.put(currentString, myMap.get(currentString) + 1);
				} else {
					myMap.put(currentString, 1);
				}
			}
			
			for (int i = 0; i < myList.length; i++) {
				String currentStringToOutput = myList[i];
				
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
	
	public static void main(String[] args) {
		StandaloneTokenCounter counter = new StandaloneTokenCounter();
		counter.runLogic();
	}
	
}

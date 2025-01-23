package standalone.token.counter;

import java.util.*;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class StandaloneTokenCounter {
	private static final String EXIT_MESSAGE = "quit"; 
	private static final String DIRECTION = "Please enter quit or a line of tokens to be processed separated by spaces";
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		while (true) {
			System.out.println(DIRECTION);
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
			
			System.out.println(finalOuput);
			
		}		
		
		scanner.close();
	}
}

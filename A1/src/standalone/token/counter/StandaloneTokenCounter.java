package standalone.token.counter;

import java.util.*;

public class StandaloneTokenCounter {
	private static final String EXIT_MESSAGE = "quit"; 
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		while (true) {
			String firstLine = scanner.nextLine();
			
			if (firstLine == EXIT_MESSAGE) {
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
					myMap.remove(currentStringToOutput);
				}
				
				if (i != (myList.length - 1)) {
					finalOuput += ", ";
				}
			}
			
			System.out.println(finalOuput);
			
		}		
		
		scanner.close();
	}
}

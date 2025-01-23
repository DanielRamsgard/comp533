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
			
			String[] myList = firstLine.split(firstLine);
			
			HashMap<String, Integer> myMap = new HashMap<>();
			
			for (int i = 0; i < myList.length; i++) {
				String currentString = myList[i];
				if (myMap.containsKey(currentString)) {
					myMap.put(currentString, myMap.get(currentString) + 1);
				}
			}
			
			
			
		}		
		
		scanner.close();
	}
}

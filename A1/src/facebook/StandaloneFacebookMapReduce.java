package facebook;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import key.value.KeyValue;

public class StandaloneFacebookMapReduce extends AMapReduceTracer {
	private static final String EXIT_MESSAGE = "quit"; 
	
	public void runLogic() {
		final Scanner scanner = new Scanner(System.in);
		
		while (true) {
			
			final String firstLine = scanner.nextLine();
			
			if (EXIT_MESSAGE.equals(firstLine)) {
				break;
			}
			
			List<KeyValue<String, List<String>>> finalMappingResult = FacebookMapper.map(firstLine);
			
			System.out.println(finalMappingResult);
		
			final Map<String, List<String>> finalReductionResult = FacebookReducer.reduce(finalMappingResult);
			
			
			super.trace(finalReductionResult.toString());
			
		}				

		scanner.close();
	}
	
	public static void main(final String[] args) {
		final StandaloneFacebookMapReduce counter = new StandaloneFacebookMapReduce();
		counter.runLogic();
	}
	
}
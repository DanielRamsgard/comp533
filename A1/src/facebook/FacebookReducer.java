package facebook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacebookReducer {
	public static Map<String, List<String>> reduce(List<Map<String, List<String>>> finalMappingResult) {
		// reduction
		final Map<String, List<String>> finalReductionResult = new HashMap<>();
		
		// iterate over list of profiles (maps)
		for (int j = 0; j < finalMappingResult.size(); j++) {
			// current map
			Map<String, List<String>> currentMap = finalMappingResult.get(j);
			
			// for each key in the current map -> check all other maps and see if exists also
			for (String key : currentMap.keySet()) {					
				// initialize a to intersect object
				List<String> toIntersect = null;
				
				// if the key exists -> take the intersection of the two lists and store that list as a key value in the final output
				for (int k = 0; k < finalMappingResult.size(); k++) {
					Map<String, List<String>> currentNestedMap = finalMappingResult.get(k);									
	                
	                if (currentNestedMap.containsKey(key) ) {
	                	// initialize the intersection with the list from the first map
		                if (toIntersect != null) {
		                	toIntersect.retainAll(currentNestedMap.get(key));		                	
		                } else {
		                	toIntersect = new ArrayList<>(currentNestedMap.get(key));
		                }
	                }
				}
				
				// take intersection if there is anything to intersect with
				if (toIntersect != null) {
					finalReductionResult.put(key, toIntersect);
				} else {
					finalReductionResult.put(key, currentMap.get(key));
				}
				
			}
		}
		
		return finalReductionResult;
	}
}

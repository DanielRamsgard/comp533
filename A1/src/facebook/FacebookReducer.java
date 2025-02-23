package facebook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import key.value.KeyValue;

public class FacebookReducer {
	public static Map<String, List<String>> reduce(List<KeyValue<String, List<String>>> finalMappingResult) {
		// reduction
		final Map<String, List<String>> finalReductionResult = new HashMap<>();
		
		// iterate over list of profiles (maps)
		for (int j = 0; j < finalMappingResult.size(); j++) {
			// current key value
			KeyValue<String, List<String>> currentKeyValue = finalMappingResult.get(j);
			String currentKey = currentKeyValue.getKey();
			
			// initialize a to intersect object
			List<String> toIntersect = null;
			
				
			// if the key exists -> take the intersection of the two lists and store that list as a key value in the final output
			for (int k = 0; k < finalMappingResult.size(); k++) {
				KeyValue<String, List<String>> currentNestedKeyValue = finalMappingResult.get(k);									
                
                if (currentNestedKeyValue.getKey().equals(currentKey)) {
                	// initialize the intersection with the list from the first map
	                if (toIntersect != null) {
	                	toIntersect.retainAll(currentNestedKeyValue.getValue());		                	
	                } else {
	                	toIntersect = new ArrayList<>(currentNestedKeyValue.getValue());
	                }
                }
			}
				
			// take intersection if there is anything to intersect with
			if (toIntersect != null) {
				finalReductionResult.put(currentKey, toIntersect);
			} else {
				finalReductionResult.put(currentKey, currentKeyValue.getValue());
			}
				
		}
		
		return finalReductionResult;
	}
}

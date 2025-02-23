package facebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacebookMapper {
	public static List<Map<String, List<String>>> map(String input) { 
		final String[] profiles = input.split(" ");
		final List<Map<String, List<String>>> finalMappingResult = new ArrayList<>();
		
		// full mapping logic
		int i = 0;
		for (String profile : profiles) {
			// final result initialization
			finalMappingResult.add(new HashMap<>());
			
			// get the user and his/her friends
			List<String> friends = new ArrayList<>(List.of(profile.split(",")));
			String baseUser = friends.remove(0);
			
			// sorting
			Collections.sort(friends);
			
			// logic for friends mapping
			for (String friend : friends) {
				// base user + a friend combination
				List<String> currentOrdering = new ArrayList<>();
				currentOrdering.add(baseUser);
				currentOrdering.add(friend);
				Collections.sort(currentOrdering);
				
				// put the result
				finalMappingResult.get(i).put(currentOrdering.get(0) + "_" + currentOrdering.get(1), friends);
			}			
			
			// increment i
			i++;
			
		}
		
		return finalMappingResult;
	}
}

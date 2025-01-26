package comp533.reducer.implementation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import key.value.KeyValue;

public class ReducerImpl extends AMapReduceTracer implements Reducer<String, Integer> {

	@Override
	public Map<String, Integer> reduce(final List<KeyValue<String, Integer>> myList) {
		// TODO Auto-generated method stub
		final Map<String, Integer> myMap = new HashMap<>();		
		
		for (int i = 0; i < myList.size(); i++) {
			final String currentString = myList.get(i).getkey();
			
			if (myMap.containsKey(currentString)) {
				myMap.put(currentString, myMap.get(currentString) + myList.get(i).getValue());
			} else {
				myMap.put(currentString, myList.get(i).getValue());
			}
		}		
		
		super.traceReduce(myList, myMap);
		
		return myMap;
	}
	
	@Override
	public String toString() {
		return this.REDUCER;
	}
	
}

package comp533.assignment.mapper;

import java.util.*;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import key.value.*;

public class AssignmentMapperImpl extends AMapReduceTracer implements AssignmentMapper<String, Integer> {

	@Override
	public List<KeyValue<String, Integer>> map(List<String> aStrings) {
		// TODO Auto-generated method stub
		List<KeyValue<String, Integer>> myList = new ArrayList<>();
		
		for (int i = 0; i < aStrings.size(); i++) {								
			myList.add(new KeyValueImpl<String, Integer>(aStrings.get(i), 1));
		}
		
		return myList;
	}
	
	public String toString() {
		return super.TOKEN_COUNTING_MAPPER;
	}
	
}
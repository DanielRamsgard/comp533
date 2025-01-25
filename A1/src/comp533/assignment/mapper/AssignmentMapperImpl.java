package comp533.assignment.mapper;

import java.util.List;
import java.util.ArrayList;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import key.value.KeyValueImpl;
import key.value.KeyValue;

public class AssignmentMapperImpl extends AMapReduceTracer implements AssignmentMapper<String, Integer> {

	@Override
	public List<KeyValue<String, Integer>> map(final List<String> aStrings) {
		super.trace(aStrings.toString());
		
		// TODO Auto-generated method stub
		final List<KeyValue<String, Integer>> myList = new ArrayList<>();
		
		for (int i = 0; i < aStrings.size(); i++) {								
			myList.add(new KeyValueImpl<String, Integer>(aStrings.get(i), 1));
		}
		
		super.trace("Map:" + aStrings.toString() + myList.toString());
		
		return myList;
	}
	
	public String toString() {
		return super.TOKEN_COUNTING_MAPPER;
	}
	
}
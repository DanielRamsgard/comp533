package comp533.assignment.mapper;

import java.util.ArrayList;
import java.util.List;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import key.value.KeyValue;
import key.value.KeyValueImpl;

public class SumMapperImpl extends AMapReduceTracer implements AssignmentMapper<String, Integer> {
	private static final String RESULT_KEY = "ResultKey";
	@Override
	public List<KeyValue<String, Integer>> map(List<String> aStrings) {
		// TODO Auto-generated method stub
		List<KeyValue<String, Integer>> myList = new ArrayList<>();
		
		for (int i = 0; i < aStrings.size(); i++) {								
			myList.add(new KeyValueImpl<String, Integer>(RESULT_KEY, Integer.parseInt(aStrings.get(i))));
		}
		
		return myList;
	}
	
	public String toString() {
		return super.INT_SUMMING_MAPPER;
	}
}

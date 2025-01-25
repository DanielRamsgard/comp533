package model.view.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import key.value.KeyValue;
import mapper.factory.MapperFactory;
import mapper.factory.MapperSumFactory;
import reduce.factory.ReducerFactoryImpl;

public class Model extends AMapReduceTracer {
	
	private PropertyChangeSupport propertyChangeSupport;
	
	public Model() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener newListener) {
		propertyChangeSupport.addPropertyChangeListener(newListener);
	
	}
	
	public String toString() {
		return super.MODEL;
	}
	
	public void setInputString(String inputString) {
		PropertyChangeEvent inputEvent = new PropertyChangeEvent(this, "InputString", null, inputString);
		propertyChangeSupport.firePropertyChange(inputEvent);		
	}
	
	public void findNewResult(String inputString) {
		String[] myList = inputString.split(" ");
		
		List<KeyValue<String, Integer>> intermediate = MapperFactory.getMapper().map(Arrays.asList(myList));
				
		Map<String, Integer> myMap = ReducerFactoryImpl.getReducer().reduce(intermediate);
		
		setResult(myMap);
	}
	
	public void findNewResultSum(String inputString) {
		String[] myList = inputString.split(" ");
		
		List<KeyValue<String, Integer>> intermediate = MapperSumFactory.getMapper().map(Arrays.asList(myList));
				
		Map<String, Integer> myMap = ReducerFactoryImpl.getReducer().reduce(intermediate);
		
		setResult(myMap);
	}
	
	private void setResult(Map<String, Integer> myMap) {
		PropertyChangeEvent inputEvent = new PropertyChangeEvent(this, "Result", null, myMap);
		
		
		propertyChangeSupport.firePropertyChange(inputEvent);
	}
}

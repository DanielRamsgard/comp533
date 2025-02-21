package model.view.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import key.value.KeyValue;
import mapper.factory.MapperFactory;
import reduce.factory.ReducerFactoryImpl;
import sum.mapper.MapperSumFactory;

public class Model extends AMapReduceTracer implements ModelInterface{
	private static final String BAR = " ";
	
	private PropertyChangeSupport propertyChangeSupport;
	
	public Model() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener(final PropertyChangeListener newListener) {
		propertyChangeSupport.addPropertyChangeListener(newListener);
	
	}
	
	@Override
	public String toString() {
		return super.MODEL;
	}
	
	public void setInputString(final String inputString) {
		final PropertyChangeEvent inputEvent = new PropertyChangeEvent(this, "InputString", null, inputString);
		propertyChangeSupport.firePropertyChange(inputEvent);		
	}
	
	public void findNewResult(final String inputString) {
		final String[] myList = inputString.split(BAR);
		
		final List<KeyValue<String, Integer>> intermediate = MapperFactory.getMapper().map(Arrays.asList(myList));
				
		final Map<String, Integer> myMap = ReducerFactoryImpl.getReducer().reduce(intermediate);
		
		setResult(myMap);
	}
	
	public void findNewResultSum(final String inputString) {
		final String[] myList = inputString.split(BAR);
		
		final List<KeyValue<String, Integer>> intermediate = MapperSumFactory.getMapper().map(Arrays.asList(myList));
				
		final Map<String, Integer> myMap = ReducerFactoryImpl.getReducer().reduce(intermediate);
		
		setResult(myMap);
	}
	
	private void setResult(final Map<String, Integer> myMap) {
		final PropertyChangeEvent inputEvent = new PropertyChangeEvent(this, "Result", null, myMap);
		
		
		propertyChangeSupport.firePropertyChange(inputEvent);
	}
}

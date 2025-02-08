package model.view.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import key.value.KeyValue;
import mapper.factory.MapperFactory;
import reduce.factory.ReducerFactoryImpl;
import slave.Slave;
import sum.mapper.MapperSumFactory;

public class Model extends AMapReduceTracer implements ModelInterface{
	private static final String BAR = " ";
	private static final String SLAVE = "Slave";
	private int numThreads;
	private final List<Thread> threads;
	
	public int getNumThreads() {
		return numThreads;
	}
	
	public void setNumThreads(int newValue) {
		this.numThreads = newValue;
		
		for (int i = 0; i < newValue; i++) {
			Slave slave = new Slave(i, this);
			Thread thread = new Thread(slave);
			
			thread.setName(SLAVE + i);
			
		}
	}
	
	public List<Thread> getThreads() {
		return threads;
	}
	
	private PropertyChangeSupport propertyChangeSupport;
	
	public Model() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		this.threads = new ArrayList<Thread>();
		this.numThreads = 0;
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

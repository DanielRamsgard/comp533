package model.view.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

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
		
		findNewResult(inputString);
	}
	
	private void findNewResult(String inputString) {
		String[] myList = inputString.split(" ");
		HashMap<String, Integer> myMap = new HashMap<>();		
		
		for (int i = 0; i < myList.length; i++) {
			String currentString = myList[i];
			if (myMap.containsKey(currentString)) {
				myMap.put(currentString, myMap.get(currentString) + 1);
			} else {
				myMap.put(currentString, 1);
			}
		}
		
		setResult(myMap);
	}
	
	private void setResult(HashMap<String, Integer> myMap) {
		PropertyChangeEvent inputEvent = new PropertyChangeEvent(this, "Result", null, myMap);
		
		
		propertyChangeSupport.firePropertyChange(inputEvent);
	}
}

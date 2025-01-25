package model.view.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class View extends AMapReduceTracer implements PropertyChangeListener {
	
	private String inputString;
	
	
	public String toString() {
		return super.VIEW;
	}
	
	private String[] getList() {
		return inputString.split(" ");
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		String eventName = evt.getPropertyName();		
		
		if (eventName == "Result") {
			String[] myList = getList();
			HashMap<String, Integer> myMap = (HashMap<String, Integer>) evt.getNewValue();
			String finalOuput = "";
			
			for (int i = 0; i < myList.length; i++) {
				String currentStringToOutput = myList[i];
				
				if (myMap.containsKey(currentStringToOutput)) {
					finalOuput += (currentStringToOutput + "=" + myMap.get(currentStringToOutput));					
					
					if (myMap.size() != 1) {
						finalOuput += ", ";
					}
					
					myMap.remove(currentStringToOutput);
				}				
			}
					
			super.trace(finalOuput);
		} else if (eventName == "InputString") {
			inputString = (String) evt.getNewValue();
		}
	}
}

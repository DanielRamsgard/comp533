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

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub	
		super.trace(evt.toString());
	}
}

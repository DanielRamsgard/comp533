package model.view.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class View extends AMapReduceTracer implements PropertyChangeListener {
	@Override
	public String toString() {
		return super.VIEW;
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		// TODO Auto-generated method stub	
		super.trace(evt.toString());
	}
}

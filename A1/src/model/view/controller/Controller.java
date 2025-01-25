package model.view.controller;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class Controller extends AMapReduceTracer {
	private Model model;
	
	public Controller(Model model) {
		this.model = model;
	}
	
	public String toString() {
		return super.CONTROLLER;
	}
}

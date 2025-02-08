package slave;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import model.view.controller.Model;

public class Slave extends AMapReduceTracer implements Runnable {
	public static String SLAVE = "Slave";
	private final int identifier;
	private final Model model;
	
	public Slave(int identifier, Model model) {
		this.identifier = identifier;
		this.model = model;
	}
	
	public void run() {
		
	}
	
	public String toString() {
		return SLAVE;
	}

}

package slave;

import java.util.ArrayList;
import java.util.List;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import key.value.KeyValue;
import model.view.controller.Model;

public class Slave extends AMapReduceTracer implements Runnable {
	public static String SLAVE = "Slave";
	private final int identifier;
	private final Model model;
	private final List<KeyValue<String, Integer>> inputList;
	
	public Slave(int identifier, Model model) {
		this.identifier = identifier;
		this.model = model;
		this.inputList = new ArrayList<>();
	}
	
	private void produceMap() {
		
	}
	
	public void run() {
		boolean itemsLeft = true;
		
		// get items
		while (itemsLeft) {
			KeyValue<String, Integer> currentKeyValue = null;
			
			try {
				currentKeyValue = model.getBlockingQueue().take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (currentKeyValue != null && currentKeyValue.getKey() != null && currentKeyValue.getValue() != null) {
				inputList.add(currentKeyValue);
			} else if (currentKeyValue != null) {
				itemsLeft = false;
			}
		}
		
		// produce partially reduced map
		produceMap();
	}
	
	public String toString() {
		return SLAVE;
	}

}

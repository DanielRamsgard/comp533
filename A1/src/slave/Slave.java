package slave;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import key.value.KeyValue;
import key.value.KeyValueImpl;
import model.view.controller.Model;
import reduce.factory.ReducerFactoryImpl;
import comp533.partitioner.PartitionerFactory;

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
		final Map<String, Integer> currentMap = ReducerFactoryImpl.getReducer().reduce(inputList);
		
		currentMap.forEach((key, value) -> {
			int partition = PartitionerFactory.getPartitioner().getPartition("", 1, model.getNumThreads());
			
			model.getReductionQueueList().get(partition).add(new KeyValueImpl(key, value));
		});
		
		model.getBarrier().barrier();
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

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
			int partition = PartitionerFactory.getPartitioner().getPartition(key, value, model.getNumThreads());
			
			synchronized (model.getReductionQueueList().get(partition)) {
				model.getReductionQueueList().get(partition).add(new KeyValueImpl(key, value));
			}			
		});
		
		model.getBarrier().barrier();
		
		// process via .reduce
		final Map<String, Integer> subMap = ReducerFactoryImpl.getReducer().reduce(model.getReductionQueueList().get(identifier));
		
		// update reduction queue with final values
		subMap.forEach((key, value) -> {
			synchronized (model.getReductionQueueList().get(identifier)) {
				model.getReductionQueueList().get(identifier).clear();
				model.getReductionQueueList().get(identifier).add(new KeyValueImpl(key, value));
			}			
		});
		
		// wait
		model.getJoiner().finished();
	}
	
	public void run() {
		// get items
		while (true) {
			KeyValue<String, Integer> currentKeyValue = null;
			
			try {
				currentKeyValue = model.getBlockingQueue().take();
			} catch (InterruptedException e) {
				super.traceQuit();
				e.printStackTrace();
				break;
			}
			
			if (currentKeyValue != null && currentKeyValue.getKey() != null && currentKeyValue.getValue() != null) {
				inputList.add(currentKeyValue);
			} else if (currentKeyValue != null) {
				break;
			}
		}
		
		// produce partially reduced map
		produceMap();
	}
	
	public String toString() {
		return SLAVE;
	}

}

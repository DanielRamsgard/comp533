package slave;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import client.Client;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import key.value.KeyValue;
import key.value.KeyValueImpl;
import model.view.controller.Model;
import reduce.factory.ReducerFactoryImpl;
import comp533.partitioner.PartitionerFactory;

public class SlaveImpl extends AMapReduceTracer implements Runnable {
	private final int identifier;
	private final Model model;
	private final List<KeyValue<String, Integer>> inputList;
	private boolean exitEarly = false;
	private Client client;
	
	public SlaveImpl(int identifier, Model model) {
		this.identifier = identifier;
		this.model = model;
		this.inputList = new ArrayList<>();
		this.client = null;
	}
	
	private void produceMap() {
		final Map<String, Integer> currentMap = ReducerFactoryImpl.getReducer().reduce(inputList);
		
		currentMap.forEach((key, value) -> {
			int partition = PartitionerFactory.getPartitioner().getPartition(key, value, model.getNumThreads());
			
			super.tracePartitionAssigned(key, value, partition, partition);
			
			synchronized (model.getReductionQueueList().get(partition)) {
				model.getReductionQueueList().get(partition).add(new KeyValueImpl(key, value));
			}			
		});		
		
		super.traceWait();
		
		model.getBarrier().barrier();
		
		super.traceSplitAfterBarrier(identifier, inputList);
		
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
	
	private void remoteCallProduceMap() {
		try {
			client.remoteProduceMap(inputList);
		} catch (RemoteException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public synchronized void notifySlave() {
		super.traceNotify();
		this.notify();
	}
	
	private synchronized void waitForBuffer() {
		try {
			super.traceWait();
			this.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			this.exitEarly = true;
			super.traceQuit();
		}
		
	}
	
	public void run() {
		// get items
		while (true) {
			if (exitEarly) {
				break;
			}
			
			while (true) {
				KeyValue<String, Integer> currentKeyValue = null;
				
				try {
					currentKeyValue = model.getBlockingQueue().take();
					super.traceDequeue(currentKeyValue);
				} catch (InterruptedException e) {
					super.traceQuit();
					exitEarly = true;
					
					break;
				}
				
				if (currentKeyValue != null && currentKeyValue.getKey() != null && currentKeyValue.getValue() != null) {
					inputList.add(currentKeyValue);
				} else if (currentKeyValue != null) {
					break;
				}
			}
			
			// if we have a client -> produce the reduction on the client
			if (this.client != null) {
				remoteCallProduceMap();
			} else {
				produceMap();
			}			
			waitForBuffer();
		}		
	}
	
	public String toString() {
		return super.SLAVE;
	}
	
	public void clearInput() {
		this.inputList.clear();
	}
	
	public void resetClient() {
		this.client = null;
	}
	
	public void addRemoteClient(Client client) {
		this.client = client;
	}

}

package facebook;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import client.Client;
import comp533.partitioner.PartitionerFactory;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import key.value.KeyValue;
import key.value.KeyValueImpl;
import model.view.controller.Model;
import reduce.factory.ReducerFactoryImpl;

public class FacebookSlaveImpl extends AMapReduceTracer implements Runnable, FacebookRemoteSlave {
	private final int identifier;
	private final FacebookModel facebookModel;
	private final List<KeyValue<String, List<String>>> inputList;
	private boolean exitEarly = false;
	private FacebookClient facebookClient;
	
	public FacebookSlaveImpl(int identifier, FacebookModel facebookModel) {
		this.identifier = identifier;
		this.facebookModel = facebookModel;
		this.inputList = new ArrayList<>();
		this.facebookClient = null;
	}
	
	private void produceMap() {
		final Map<String, List<String>> currentMap = FacebookReducer.reduce(inputList);
		
		currentMap.forEach((key, value) -> {
			int partition = PartitionerFactory.getPartitioner().getPartition(key, value, facebookModel.getNumThreads());
			
			super.tracePartitionAssigned(key, value, partition, partition);
			
			synchronized (facebookModel.getReductionQueueList().get(partition)) {
				facebookModel.getReductionQueueList().get(partition).add(new KeyValueImpl(key, value));
			}			
		});		
		
		super.traceWait();
		
		facebookModel.getBarrier().barrier();
		
		super.traceSplitAfterBarrier(identifier, inputList);
		
		Map<String, List<String>> subMap = null;
		
		// trace the input
		super.traceRemoteList(facebookModel.getReductionQueueList().get(identifier));
		
		// if we have a client -> produce the reduction on the client
		if (this.facebookClient != null) {
			subMap = remoteCallReduce();
		} else {
			subMap = localCallReduce();
		}
		
		// trace the result
		super.traceRemoteResult(subMap);
		
		finish(subMap);
		
		// wait
		facebookModel.getJoiner().finished();	
	}
	
	public Map<String, List<String>> remoteCallReduce() {
		try {
			return facebookClient.reduce(facebookModel.getReductionQueueList().get(identifier));
		} catch (RemoteException e) {
			System.out.println(e.getMessage());
			return localCallReduce(); 
		}
	}
	
	public Map<String, List<String>> localCallReduce() {
		// process via .reduce
		return FacebookReducer.reduce(facebookModel.getReductionQueueList().get(identifier));
	}
	
	private void finish(Map<String, List<String>> subMap) {
		// update reduction queue with final values
		facebookModel.getReductionQueueList().get(identifier).clear();
		
		subMap.forEach((key, value) -> {
			synchronized (facebookModel.getReductionQueueList().get(identifier)) {				
				facebookModel.getReductionQueueList().get(identifier).add(new KeyValueImpl(key, value));
			}			
		});
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
				KeyValue<String, List<String>> currentKeyValue = null;
				
				try {
					currentKeyValue = facebookModel.getBlockingQueue().take();
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
			
			// produce the reduction on the client
			produceMap();			
			waitForBuffer();
		}		
	}
	
	public String toString() {
		return super.SLAVE;
	}
	
	public void clearInput() {
		this.inputList.clear();
	}
	
	public void addRemoteClient(FacebookClient facebookClient) {
		super.traceClientAssignment(facebookClient);
		this.facebookClient = facebookClient;
	}
}

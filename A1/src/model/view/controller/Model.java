package model.view.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import client.Client;
import comp533.barrier.Barrier;
import comp533.barrier.BarrierImpl;
import comp533.joiner.Joiner;
import comp533.joiner.JoinerImpl;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import key.value.KeyValue;
import key.value.KeyValueImpl;
import mapper.factory.MapperFactory;
import reduce.factory.ReducerFactoryImpl;
import slave.SlaveImpl;
import sum.mapper.MapperSumFactory;

public class Model extends AMapReduceTracer implements ModelInterface, RemoteModel {
	private static final String BAR = " ";
	private static final String SLAVE = "Slave";
	private PropertyChangeSupport propertyChangeSupport;
	
	private int numThreads;
	private List<Thread> threads;
	private List<SlaveImpl> slaves;
	private BlockingQueue<KeyValue<String, Integer>> keyValueQueue;
	private List<LinkedList<KeyValue<String, Integer>>> reductionQueueList;
	private Joiner joiner;
	private Barrier barrier;
	private boolean isSum;
	private Map<Client, SlaveImpl> clients = new HashMap<>();
	
	public Model() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	
	}
	
	public int getNumThreads() {
		return numThreads;
	}
	
	public void setNumThreads(int newValue) {
		int oldValue = numThreads;
		this.numThreads = newValue;
		
		final PropertyChangeEvent inputEvent = new PropertyChangeEvent(this, "NumThreads", oldValue, newValue);
		propertyChangeSupport.firePropertyChange(inputEvent);
		
		threads = new ArrayList<>();
		slaves = new ArrayList<>();
		reductionQueueList = new ArrayList<>();
		
		// initialize next round of processing
		this.keyValueQueue = new ArrayBlockingQueue<>(super.BUFFER_SIZE, true);		
		this.joiner = new JoinerImpl(numThreads);
		this.barrier = new BarrierImpl(numThreads);
		
		for (int i = 0; i < newValue; i++) {
			SlaveImpl slave = new SlaveImpl(i, this);
			
			slaves.add(slave);
			
			Thread thread = new Thread(slave);
			
			thread.setName(SLAVE + i);
			thread.start();
			threads.add(thread);			
			reductionQueueList.add(new LinkedList<KeyValue<String, Integer>>());
			
		}		
		
		final PropertyChangeEvent threadInputEvent = new PropertyChangeEvent(this, "Threads", null, threads);
		propertyChangeSupport.firePropertyChange(threadInputEvent);
	}
	
	public List<Thread> getThreads() {
		return threads;
	}	
	
	public void addPropertyChangeListener(final PropertyChangeListener newListener) {
		propertyChangeSupport.addPropertyChangeListener(newListener);
	
	}
	
	@Override
	public String toString() {
		return super.MODEL;
	}
	
	public void setSum(boolean isSum) {
		this.isSum = isSum;
	}
	
	private Map<String, Integer> gatherResults() {
		// TODO Auto-generated method stub
		final Map<String, Integer> myMap = new HashMap<>();		
		
		for (int i = 0; i < reductionQueueList.size(); i++) {
			List<KeyValue<String, Integer>> currentList = reductionQueueList.get(i);
			
			Map<String, Integer> currentMap = ReducerFactoryImpl.getReducer().reduce(currentList);
			
			myMap.putAll(currentMap);
			
			super.traceAddedToMap(myMap, currentMap);
		}			
		
		return myMap;
	}
	
	public void resetInput() {
		joiner.resetThreadCount();
		barrier.resetThreadCount();
		reductionQueueList.clear();
		keyValueQueue.clear();
		
		for (int i = 0; i < threads.size(); i++) {			
			slaves.get(i).clearInput();
			slaves.get(i).resetClient();
			reductionQueueList.add(new LinkedList<KeyValue<String, Integer>>());
			
		}	
		
		int i = 0;
		
		for (Client client : clients.keySet()) {
		    if (clients.get(client) == null && i < threads.size()) {
		    	SlaveImpl currentSlave = slaves.get(i);
		    	currentSlave.addRemoteClient(client);
		    	clients.put(client, currentSlave);
		    }
		    
		    i++;
		}
	}
	
	public void setInputString(final String inputString) {
		resetInput();
		
		for (SlaveImpl slave : slaves) {
			slave.notifySlave();
		}
		
		// send event
		final PropertyChangeEvent inputEvent = new PropertyChangeEvent(this, "InputString", null, inputString);
		propertyChangeSupport.firePropertyChange(inputEvent);			
		
		final String[] inputList = inputString.split(BAR);
		
		final List<KeyValue<String, Integer>> intermediate; 
		
		// two implements for if sum or not
		if (isSum) {
			intermediate = MapperSumFactory.getMapper().map(Arrays.asList(inputList));
		} else {
			intermediate = MapperFactory.getMapper().map(Arrays.asList(inputList));
		}		
		
		int currentSize = intermediate.size();
		
		super.traceBarrierCreated(this.barrier, numThreads);
		super.traceJoinerCreated(this.joiner, numThreads);
		
		// building the buffer
		for (int i = 0; i < currentSize; i++) {
			try {
				KeyValue<String, Integer> current = intermediate.get(i);
				
				super.traceEnqueueRequest(current);
				keyValueQueue.put(current);
				super.traceEnqueue(keyValueQueue);			
								
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		for (int i = 0; i < threads.size(); i++) {
			try {
				KeyValue<String, Integer> current = new KeyValueImpl(null, null);
				
				super.traceEnqueueRequest(current);
				keyValueQueue.put(current);
				super.traceEnqueue(keyValueQueue);
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		// wait for threads to finish execution
		joiner.join();	
		
		
		// gather the result into one output
		Map<String, Integer> resultMap = gatherResults();
		
		final PropertyChangeEvent outputEvent = new PropertyChangeEvent(this, "Result", null, resultMap);
		propertyChangeSupport.firePropertyChange(outputEvent);		
		
	}
	
	public void terminate() {
		for (Thread thread : threads) {
			thread.interrupt();
		}
	}
	
	public Joiner getJoiner() {
		return joiner;
	}
	
	public Barrier getBarrier() {
		return barrier;
	}
	
	public BlockingQueue<KeyValue<String, Integer>> getBlockingQueue() {
		return keyValueQueue;
	}
	
	public List<LinkedList<KeyValue<String, Integer>>> getReductionQueueList() {
		return reductionQueueList;
	}

	@Override
	public void registerRemoteClient(Client client) throws RemoteException {
		clients.put(client, null);
	}
}

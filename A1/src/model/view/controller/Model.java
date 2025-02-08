package model.view.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import comp533.barrier.Barrier;
import comp533.barrier.BarrierImpl;
import comp533.joiner.Joiner;
import comp533.joiner.JoinerImpl;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import key.value.KeyValue;
import key.value.KeyValueImpl;
import mapper.factory.MapperFactory;
import reduce.factory.ReducerFactoryImpl;
import slave.Slave;
import sum.mapper.MapperSumFactory;

public class Model extends AMapReduceTracer implements ModelInterface{
	private static final String BAR = " ";
	private static final String SLAVE = "Slave";
	private PropertyChangeSupport propertyChangeSupport;
	
	private int numThreads;
	private List<Thread> threads;
	private BlockingQueue<KeyValue<String, Integer>> keyValueQueue;
	private List<LinkedList> reductionQueueList;
	private Joiner joiner;
	private Barrier barrier;
	
	public Model() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		this.threads = new ArrayList<Thread>();
		this.keyValueQueue = new ArrayBlockingQueue<>(super.BUFFER_SIZE, true);
		this.reductionQueueList = new ArrayList<>();
		this.joiner = new JoinerImpl(0);
		this.barrier = new BarrierImpl(0);
	}
	
	public int getNumThreads() {
		return numThreads;
	}
	
	public void setNumThreads(int newValue) {
		int oldValue = numThreads;
		this.numThreads = newValue;
		
		final PropertyChangeEvent inputEvent = new PropertyChangeEvent(this, "NumThreads", oldValue, newValue);
		propertyChangeSupport.firePropertyChange(inputEvent);
		
		final List<Thread> oldThreads = threads;
		threads.clear();
		
		for (int i = 0; i < newValue; i++) {
			Slave slave = new Slave(i, this);
			Thread thread = new Thread(slave);
			
			thread.setName(SLAVE + i);
			
			threads.add(thread);
			reductionQueueList.add(new LinkedList(null));
			
		}		
		
		final PropertyChangeEvent threadInputEvent = new PropertyChangeEvent(this, "Threads", oldThreads, threads);
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
	
	public void setInputString(final String inputString) {
		// send event
		final PropertyChangeEvent inputEvent = new PropertyChangeEvent(this, "InputString", null, inputString);
		propertyChangeSupport.firePropertyChange(inputEvent);	
		
		// initialize next round of processing
		this.threads = new ArrayList<Thread>();
		this.keyValueQueue = new ArrayBlockingQueue<>(super.BUFFER_SIZE, true);
		this.reductionQueueList = new ArrayList<>();
		this.joiner = new JoinerImpl(0);
		this.barrier = new BarrierImpl(0);
		
		final String[] inputList = inputString.split(BAR);
		
		final List<KeyValue<String, Integer>> intermediate = MapperSumFactory.getMapper().map(Arrays.asList(inputList));
		
		for (int i = 0; i < intermediate.size(); i++) {
			try {
				KeyValue<String, Integer> current = intermediate.get(i);
				
				super.traceEnqueueRequest(current);
				keyValueQueue.put(current);
				super.traceEnqueue(keyValueQueue);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}	
		
		setNumThreads(intermediate.size());
		
		joiner.join();
		
		final PropertyChangeEvent outputEvent = new PropertyChangeEvent(this, "Result", null, myMap);
		propertyChangeSupport.firePropertyChange(inputEvent);
		
	}
}

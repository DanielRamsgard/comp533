package facebook;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
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
import model.view.controller.ModelInterface;
import reduce.factory.ReducerFactoryImpl;
import slave.SlaveImpl;
import sum.mapper.MapperSumFactory;

public class FacebookModel extends AMapReduceTracer implements ModelInterface, Serializable, FacebookRemoteModelInterface {
	private static final String BAR = " ";
	private static final String SLAVE = "Slave";
	private PropertyChangeSupport propertyChangeSupport;
	
	private int numThreads;
	private List<Thread> threads;
	private List<FacebookSlaveImpl> slaves;
	private BlockingQueue<KeyValue<String, List<String>>> keyValueQueue;
	private List<LinkedList<KeyValue<String, List<String>>>> reductionQueueList;
	private Joiner joiner;
	private Barrier barrier;
	private Stack<FacebookClient> clientStack = new Stack<>();
	private Stack<FacebookSlaveImpl> slaveStack = new Stack<>();
	private List<FacebookClient> clientList = new ArrayList<>();
	
	public FacebookModel() {
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
			FacebookSlaveImpl slave = new FacebookSlaveImpl(i, this);
			
			slaves.add(slave);
			slaveStack.push(slave);
			
			Thread thread = new Thread(slave);
			
			thread.setName(SLAVE + i);
			thread.start();
			threads.add(thread);			
			reductionQueueList.add(new LinkedList<KeyValue<String, List<String>>>());
			
		}		
		
		for (int i = 0; i < newValue; i++) {
			match();
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
	
	private Map<String, List<String>> gatherResults() {
		// TODO Auto-generated method stub
		final Map<String, List<String>> myMap = new HashMap<>();		
		
		for (int i = 0; i < reductionQueueList.size(); i++) {
			List<KeyValue<String, List<String>>> currentList = reductionQueueList.get(i);
			
			Map<String, List<String>> currentMap = FacebookReducer.reduce(currentList);
			
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
			reductionQueueList.add(new LinkedList<KeyValue<String, List<String>>>());
			
		}
	}
	
	public void setInputString(final String inputString) {
		resetInput();
		
		for (FacebookSlaveImpl slave : slaves) {
			slave.notifySlave();
		}
		
		// send event
		final PropertyChangeEvent inputEvent = new PropertyChangeEvent(this, "InputString", null, inputString);
		propertyChangeSupport.firePropertyChange(inputEvent);
		
		final List<KeyValue<String, List<String>>> intermediate = FacebookMapper.map(inputString);
		
		int currentSize = intermediate.size();
		
		super.traceBarrierCreated(this.barrier, numThreads);
		super.traceJoinerCreated(this.joiner, numThreads);
		
		// building the buffer
		for (int i = 0; i < currentSize; i++) {
			try {
				KeyValue<String, List<String>> current = intermediate.get(i);
				
				super.traceEnqueueRequest(current);
				keyValueQueue.put(current);
				super.traceEnqueue(keyValueQueue);			
								
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		for (int i = 0; i < threads.size(); i++) {
			try {
				KeyValue<String, List<String>> current = new KeyValueImpl(null, null);
				
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
		Map<String, List<String>> resultMap = gatherResults();
		
		final PropertyChangeEvent outputEvent = new PropertyChangeEvent(this, "Result", null, resultMap);
		propertyChangeSupport.firePropertyChange(outputEvent);		
		
	}
	
	public void terminate() {
		for (Thread thread : threads) {
			thread.interrupt();
		}
		
		quit();
	}
	
	public Joiner getJoiner() {
		return joiner;
	}
	
	public Barrier getBarrier() {
		return barrier;
	}
	
	public BlockingQueue<KeyValue<String, List<String>>> getBlockingQueue() {
		return keyValueQueue;
	}
	
	public List<LinkedList<KeyValue<String, List<String>>>> getReductionQueueList() {
		return reductionQueueList;
	}
	
	public List<LinkedList<KeyValue<String, List<String>>>> getReductionQueueListRemote() {
		return reductionQueueList;
	}

	@Override
	public void registerRemoteClient(FacebookClient facebookClient) throws RemoteException {
		super.traceRegister(facebookClient);
		clientList.add(facebookClient);
		clientStack.push(facebookClient);
		match();
	}
	
	@Override
	public void quit() {
		for (FacebookClient facebookClient : clientList) {
			try {
				facebookClient.quit();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		super.traceExit(getClass());
		System.exit(0);
	}
	
	public void match() {
		if (!clientStack.empty() && !slaveStack.empty()) {
			// bind a slave and a client
			FacebookSlaveImpl currentSlave = slaveStack.pop();
			currentSlave.addRemoteClient(clientStack.pop());
		}
	}
}

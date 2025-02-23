package client;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import key.value.KeyValue;
import key.value.KeyValueImpl;
import model.view.controller.RemoteConnect;
import model.view.controller.RemoteModel;
import reduce.factory.ReducerFactoryImpl;

public class ClientImpl extends AMapReduceTracer implements Client, Serializable {
	private RemoteModel remoteModel;

	
	public ClientImpl(RemoteModel remoteModel) {
		this.remoteModel = remoteModel;
	}
	
	public Map<String, Integer> reduce(List<KeyValue<String, Integer>> myList) {
		super.traceRemoteList(myList);
		
		final Map<String, Integer> subMap = ReducerFactoryImpl.getReducer().reduce(myList);
		
		super.traceRemoteResult(subMap);
		
		return subMap;
	}
	
	public synchronized void quit() {
		super.traceQuit();
		super.traceNotify();
		this.notify();		
	}
	
	public synchronized void block() {
		try {
			super.traceWait();
			this.wait();
			super.traceExit(getClass());
			System.exit(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}		
	}
}

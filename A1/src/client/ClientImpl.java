package client;

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

public class ClientImpl extends AMapReduceTracer implements Client {
	static String SERVER_HOST_NAME = "localhost";
	private RemoteModel remoteModel;

	
	public ClientImpl() throws NotBoundException {
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry(SERVER_HOST_NAME, RemoteConnect.SERVER_PORT);
		    RemoteModel remoteModel = (RemoteModel) rmiRegistry.lookup(RemoteConnect.MODEL_NAME);
		    remoteModel.registerRemoteClient(this);
		    this.remoteModel = remoteModel;
		} catch (RemoteException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public Map<String, Integer> reduce(List<KeyValue<String, Integer>> myList) {
		super.traceRemoteList(myList);
		
		final Map<String, Integer> subMap = ReducerFactoryImpl.getReducer().reduce(myList);
		
		super.traceRemoteResult(subMap);
		
		return subMap;
	}
}

package facebook;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import key.value.KeyValue;
import model.view.controller.RemoteModel;

public class FacebookClientImpl extends AMapReduceTracer implements FacebookClient, Serializable {
	
	public Map<String, List<String>> reduce(List<KeyValue<String, List<String>>> finalMappingResult) {
		super.traceRemoteList(finalMappingResult);
		
		final Map<String, List<String>> interumResult = FacebookReducer.reduce(finalMappingResult);

		super.traceRemoteResult(interumResult);
		
		return interumResult;
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


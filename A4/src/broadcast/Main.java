package broadcast;

import java.rmi.RemoteException;

import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import assignments.util.mainArgs.ServerArgsProcessor;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import util.annotations.Tags;
import util.tags.DistributedTags;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.gipc.GIPCRPCTraceUtility;
import util.trace.port.rpc.rmi.RMIObjectRegistered;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({DistributedTags.SERVER, DistributedTags.RMI, DistributedTags.GIPC})
public class Main {
	public static void main (String[] args) {
		FactoryTraceUtility.setTracing();
		BeanTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		GIPCRPCTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		
		System.setProperty("java.awt.headless","true");
		System.setProperty("java.rmi.server.hostname", "localhost");
		
		// initialize variables to work with RMI
		String serverHost = ServerArgsProcessor.getRegistryHost(args);
		int serverPort = ServerArgsProcessor.getRegistryPort(args);
		int serverGIPCPort = ServerArgsProcessor.getGIPCServerPort(args);

		// initialize object to do work
		CoupledServerSimulation coupledServerSimulation = new CoupledServerSimulation();
		
		// initialize RMI registry and export
		try {
			Registry rmiRegistry = coupledServerSimulation.setupConnection(serverHost, serverPort);
			ICoupledServerSimulation coupledServerSimulationProxy = (ICoupledServerSimulation) UnicastRemoteObject.exportObject(coupledServerSimulation, 0);
			coupledServerSimulation.performRebind(rmiRegistry, coupledServerSimulationProxy);
			
			// GIPC
			GIPCRegistry gipcRegistry = coupledServerSimulation.setupConnectionGIPC(serverGIPCPort);
			RMIObjectRegistered.newCase(Main.class, "SERVER", coupledServerSimulationProxy, rmiRegistry);
			coupledServerSimulation.performRebindGIPC(gipcRegistry);
			
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// run the object after exporting it
		coupledServerSimulation.startCustom(args);
	}

}

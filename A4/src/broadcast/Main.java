package broadcast;

import java.rmi.RemoteException;

import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import assignments.util.mainArgs.ServerArgsProcessor;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({DistributedTags.SERVER, DistributedTags.RMI})
public class Main {
	public static void main (String[] args) {
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
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// GIPC
		GIPCRegistry gipcRegistry = coupledServerSimulation.setupConnectionGIPC(serverGIPCPort);
		coupledServerSimulation.performRebindGIPC(gipcRegistry);
		
		// run the object after exporting it
		coupledServerSimulation.startCustom(args);
	}

}

package broadcast;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import assignments.util.mainArgs.ServerArgsProcessor;

public class Main {
	public static String SERVER_NAME = "SERVER";
	
	public static void main (String[] args) {
		// initialize variables to work with RMI
		String serverHost = ServerArgsProcessor.getRegistryHost(args);
		int serverPort = ServerArgsProcessor.getRegistryPort(args);

		// initialize object to do work
		CoupledServerSimulation coupledServerSimulation = new CoupledServerSimulation();
		
		// initialize RMI registry and export
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry(serverHost, serverPort);
			ICoupledServerSimulation iCoupledServerSimulation = (ICoupledServerSimulation) UnicastRemoteObject.exportObject(coupledServerSimulation, 0);
			rmiRegistry.rebind(SERVER_NAME, iCoupledServerSimulation);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// run the object after exporting it
		coupledServerSimulation.start(args);
	}

}

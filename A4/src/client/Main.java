package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import assignments.util.mainArgs.ClientArgsProcessor;
import broadcast.ICoupledServerSimulation;

public class Main {
	public static void main (String[] args) throws NotBoundException {
		// initialize variables to work with RMI
		String clientHost = ClientArgsProcessor.getRegistryHost(args);
		int clientPort = ClientArgsProcessor.getRegistryPort(args);

		// initialize object to do work
		CoupledClientSimulation coupledClientSimulation = new CoupledClientSimulation();
		
		// initialize RMI registry and export
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry(clientHost, clientPort);
			ICoupledClientSimulation coupledClientSimulationRemote = (ICoupledClientSimulation) UnicastRemoteObject.exportObject(coupledClientSimulation, 0);
			ICoupledServerSimulation coupledServerSimulation = (ICoupledServerSimulation) rmiRegistry.lookup(broadcast.Main.SERVER_NAME);
			coupledServerSimulation.registerClient(coupledClientSimulationRemote);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// run the object after exporting it
		coupledClientSimulation.start(args);
	}

}

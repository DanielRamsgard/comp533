package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import assignments.util.mainArgs.ClientArgsProcessor;
import broadcast.ICoupledServerSimulation;
import util.trace.port.rpc.rmi.RMIObjectLookedUp;
import util.trace.port.rpc.rmi.RMIRegistryLocated;

import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({DistributedTags.CLIENT, DistributedTags.RMI})
public class Main {
	public static void main (String[] args) throws NotBoundException {
		// initialize variables to work with RMI
		String clientHost = ClientArgsProcessor.getRegistryHost(args);
		int clientPort = ClientArgsProcessor.getRegistryPort(args);
		String clientName = ClientArgsProcessor.getClientName(args);

		// initialize object to do work
		CoupledClientSimulation coupledClientSimulation = new CoupledClientSimulation();
		coupledClientSimulation.setClientName(clientName);
		
		// initialize RMI registry and export
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry(clientHost, clientPort);
			RMIRegistryLocated.newCase(coupledClientSimulation, clientHost, clientPort, rmiRegistry);
			ICoupledClientSimulation coupledClientSimulationRemote = (ICoupledClientSimulation) UnicastRemoteObject.exportObject(coupledClientSimulation, 0);
			ICoupledServerSimulation coupledServerSimulation = (ICoupledServerSimulation) rmiRegistry.lookup(broadcast.Main.SERVER_NAME);
			coupledClientSimulation.setConfigurer(coupledServerSimulation);
			//
			RMIObjectLookedUp.newCase(coupledClientSimulationRemote, coupledServerSimulation, coupledClientSimulation.getClientName(), rmiRegistry);
			//
			
			coupledServerSimulation.registerClient(coupledClientSimulationRemote);
			coupledClientSimulation.setConfigurer(coupledServerSimulation);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// run the object after exporting it
		coupledClientSimulation.startCustom(args);
	}

}

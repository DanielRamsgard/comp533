package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import assignments.util.mainArgs.ClientArgsProcessor;
import broadcast.ICoupledServerSimulation;
import broadcast.IGeneralizedIPCServer;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.gipc.GIPCRPCTraceUtility;
import util.trace.port.rpc.rmi.RMIObjectLookedUp;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.rpc.rmi.RMITraceUtility;
import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({DistributedTags.CLIENT, DistributedTags.RMI, DistributedTags.GIPC})
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
			Registry rmiRegistry = coupledClientSimulation.setupConnection(clientHost, clientPort);
			ICoupledClientSimulation coupledClientSimulationRemote = (ICoupledClientSimulation) UnicastRemoteObject.exportObject(coupledClientSimulation, 0);
			ICoupledServerSimulation coupledServerSimulation = coupledClientSimulation.performLookup(rmiRegistry);
			
			coupledServerSimulation.registerClient(coupledClientSimulationRemote);
			coupledClientSimulation.setServer(coupledServerSimulation);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// GIPC
		GIPCRegistry gipcRegistry = coupledClientSimulation.setupConnectionGIPC(clientHost, clientName, args);
		IGeneralizedIPCServer coupledServerSimulationGIPC = coupledClientSimulation.performLookupGIPC(gipcRegistry);
		coupledServerSimulationGIPC.registerClientGIPC(coupledClientSimulation);
		coupledClientSimulation.setServerGIPC(coupledServerSimulationGIPC);

		
		// run the object after exporting it
		coupledClientSimulation.startCustom(args);
	}

}

package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import assignments.util.inputParameters.SimulationParametersListener;
import broadcast.ICoupledServerSimulation;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import util.annotations.Tags;
import util.tags.DistributedTags;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.PortTraceUtility;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({DistributedTags.CLIENT_CONFIGURER, DistributedTags.RMI})
public class ClientConfigurer implements SimulationParametersListener {
	
	protected void setTracing() {
		PortTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		FactoryTraceUtility.setTracing();		
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		trace(true);
	}
	
	public ClientConfigurer() { 
		setTracing();
	}
	
	public Registry setupConnection(String clientHost, int clientPort) throws RemoteException {
		Registry rmiRegistry = LocateRegistry.getRegistry(clientHost, clientPort);
		RMIRegistryLocated.newCase(this, clientHost, clientPort, rmiRegistry);
		
		return rmiRegistry;
	}
	
	public GIPCRegistry setupConnectionGIPC(String clientHost, int gipcPort, String clientName) {
		GIPCRegistry gipcRegistry = GIPCLocateRegistry.getRegistry(clientHost, gipcPort, clientName);
		
		return gipcRegistry;
	}
}

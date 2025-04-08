package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import assignments.util.inputParameters.SimulationParametersListener;
import assignments.util.mainArgs.ClientArgsProcessor;
import broadcast.CoupledServerSimulation;
import broadcast.ICoupledServerSimulation;
import broadcast.IGeneralizedIPCServer;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import port.ATracingConnectionListener;
import util.annotations.Tags;
import util.interactiveMethodInvocation.IPCMechanism;
import util.tags.DistributedTags;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.PortTraceUtility;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.consensus.ProposedStateSet;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.gipc.GIPCObjectLookedUp;
import util.trace.port.rpc.gipc.GIPCRegistryLocated;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({DistributedTags.CLIENT_CONFIGURER, DistributedTags.RMI, DistributedTags.GIPC})
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
	
	public GIPCRegistry setupConnectionGIPC(String clientHost, String clientName, String[] args) {
		int port = ClientArgsProcessor.getGIPCPort(args) + 1;
		GIPCRegistry gipcRegistry = GIPCLocateRegistry.getRegistry(clientHost, port, clientName);
		
		GIPCRegistryLocated.newCase(this, clientHost, port, clientName);
		
		return gipcRegistry;
	}
	
	public void setBroadcastMetaStateChild(CoupledClientSimulation coupledClientSimulation, boolean newValue) {
		coupledClientSimulation.setBroadcastMetaState(newValue);
	}
	
	public void setIPCChild(CoupledClientSimulation coupledClientSimulation, ICoupledServerSimulation server, IPCMechanism newValue) {
		if (coupledClientSimulation.isBroadcastMetaState()) {
			try {
				server.alterIpc(newValue, ClientArgsProcessor.getClientName(null));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ProposedStateSet.newCase(this, "ipc_mechanism", -1, newValue);
			coupledClientSimulation.setIPCMechanism(newValue);
		}
	}
	
	public IGeneralizedIPCServer performLookupGIPC(GIPCRegistry gipcRegistry) {
		IGeneralizedIPCServer retServer = (IGeneralizedIPCServer) gipcRegistry.lookup(IGeneralizedIPCServer.class, CoupledServerSimulation.SERVER_NAME);
		
		gipcRegistry.getInputPort().addConnectionListener(new ATracingConnectionListener(gipcRegistry.getInputPort()));
		
		GIPCObjectLookedUp.newCase(this, retServer, getClass(), null, gipcRegistry);
		
		return retServer;
	}
}

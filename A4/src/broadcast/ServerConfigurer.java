package broadcast;

import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import assignments.util.inputParameters.SimulationParametersListener;
import client.ICoupledClientSimulation;
import util.annotations.Tags;
import util.tags.DistributedTags;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.PortTraceUtility;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.rmi.RMIObjectRegistered;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({DistributedTags.SERVER_CONFIGURER, DistributedTags.RMI})
public class ServerConfigurer implements SimulationParametersListener {
	private List<ICoupledClientSimulation> clients;
	
	protected void setTracing() {
		PortTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		FactoryTraceUtility.setTracing();		
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		trace(true);
	}
	
	public ServerConfigurer() {
		setTracing();
		this.clients = new ArrayList<>();
	}
	
	public void addClient(ICoupledClientSimulation o) {
		clients.add(o);
	}
	
	public List<ICoupledClientSimulation> getClients() {
		return clients;
	}
	
	public void performRebind(Registry rmiRegistry, ICoupledServerSimulation iCoupledServerSimulation, String serverName) throws AccessException, RemoteException {
		rmiRegistry.rebind(serverName, iCoupledServerSimulation);
		RMIObjectRegistered.newCase(this, serverName, iCoupledServerSimulation, rmiRegistry);
	}
	
	public Registry setupConnection(String serverHost, int serverPort) throws RemoteException {
		Registry rmiRegistry = LocateRegistry.getRegistry(serverHost, serverPort);
		RMIRegistryLocated.newCase(this, serverHost, serverPort, rmiRegistry);
		
		return rmiRegistry;
	}
}

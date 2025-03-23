package broadcast;

import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import client.ICoupledClientSimulation;
import util.annotations.Tags;
import util.tags.DistributedTags;
import util.trace.port.rpc.rmi.RMIObjectRegistered;
import util.trace.port.rpc.rmi.RMIRegistryLocated;

@Tags({DistributedTags.SERVER_CONFIGURER, DistributedTags.RMI})
public class ServerConfigurer {
	private List<ICoupledClientSimulation> clients;
	
	public ServerConfigurer() {
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

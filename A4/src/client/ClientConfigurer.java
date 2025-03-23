package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import broadcast.ICoupledServerSimulation;
import util.annotations.Tags;
import util.tags.DistributedTags;
import util.trace.port.rpc.rmi.RMIRegistryLocated;

@Tags({DistributedTags.CLIENT_CONFIGURER, DistributedTags.RMI})
public class ClientConfigurer {
	private ICoupledServerSimulation remoteServer;
	
	public ClientConfigurer(ICoupledServerSimulation server) {
		this.remoteServer = server;
	}
	
	public ICoupledServerSimulation getRemoteServer() {
		return remoteServer;
	}
	
	public Registry setupConnection(String clientHost, int clientPort) throws RemoteException {
		Registry rmiRegistry = LocateRegistry.getRegistry(clientHost, clientPort);
		RMIRegistryLocated.newCase(this, clientHost, clientPort, rmiRegistry);
		
		return rmiRegistry;
	}
}

package client;

import broadcast.ICoupledServerSimulation;
import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({DistributedTags.CLIENT_CONFIGURER, DistributedTags.RMI})
public class ClientConfigurer {
	private ICoupledServerSimulation remoteServer;
	
	public ClientConfigurer(ICoupledServerSimulation server) {
		this.remoteServer = server;
	}
	
	public ICoupledServerSimulation getRemoteServer() {
		return remoteServer;
	}
}

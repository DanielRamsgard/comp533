package broadcast;

import java.util.ArrayList;
import java.util.List;

import client.ICoupledClientSimulation;
import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({DistributedTags.SERVER_CONFIGURER, DistributedTags.RMI})
public class ServerConfigurer {
	private List<ICoupledClientSimulation> clients;
	
	public ServerConfigurer() {
		this.clients = new ArrayList<>();
	}
	
	public void addClient(ICoupledClientSimulation o) {
		clients.add(o);
	}
}

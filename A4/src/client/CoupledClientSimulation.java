package client;

import broadcast.ICoupledServerSimulation;
import coupledsims.AStandAloneTwoCoupledHalloweenSimulations;
import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({DistributedTags.CLIENT_REMOTE_OBJECT, DistributedTags.RMI})
public class CoupledClientSimulation extends AStandAloneTwoCoupledHalloweenSimulations implements ICoupledClientSimulation {
	private String clientName;
	private ClientConfigurer configurer;
	
	public void setClientName(String name) {
		clientName = name;
	}
	
	public String getClientName() {
		return clientName;
	}
	
	public void setConfigurer(ICoupledServerSimulation server) {
		this.configurer = new ClientConfigurer(server);
	}
	
	public void notifyNewCommand(String command) {
		// must somehow notify the simulation and process the command
	}
}

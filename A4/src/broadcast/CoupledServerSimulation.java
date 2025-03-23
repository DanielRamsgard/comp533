package broadcast;
import client.ICoupledClientSimulation;
import coupledsims.AStandAloneTwoCoupledHalloweenSimulations;
import util.annotations.Tags;
import util.interactiveMethodInvocation.SimulationParametersControllerFactory;
import util.tags.DistributedTags;

@Tags({DistributedTags.SERVER_REMOTE_OBJECT, DistributedTags.RMI})
public class CoupledServerSimulation extends AStandAloneTwoCoupledHalloweenSimulations implements ICoupledServerSimulation {
	private ServerConfigurer configurer;
	
	public CoupledServerSimulation() {
		this.configurer = new ServerConfigurer();
	}
	
	@Override
	public void registerClient(ICoupledClientSimulation o) {
		this.configurer.addClient(o);
	}
	
	@Override
	public void broadcast(String command, String sendingClientName) {
		this.configurer.broadcast(command, sendingClientName);
	}

}

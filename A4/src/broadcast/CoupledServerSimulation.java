package broadcast;

import java.util.List;
import client.ICoupledClientSimulation;
import coupledsims.AStandAloneTwoCoupledHalloweenSimulations;

public class CoupledServerSimulation extends AStandAloneTwoCoupledHalloweenSimulations implements ICoupledServerSimulation {
	private List<ICoupledClientSimulation> clients;
	
	
	public CoupledServerSimulation() {
		
	}
	
	@Override
	public void registerClient(ICoupledClientSimulation o) {
		clients.add(o);
	}

}

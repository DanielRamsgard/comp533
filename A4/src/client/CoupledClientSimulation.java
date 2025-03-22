package client;

import coupledsims.AStandAloneTwoCoupledHalloweenSimulations;

public class CoupledClientSimulation extends AStandAloneTwoCoupledHalloweenSimulations implements ICoupledClientSimulation {
	private String clientName;
	
	public void setClientName(String name) {
		clientName = name;
	}
	
	public String getClientName() {
		return clientName;
	}
}

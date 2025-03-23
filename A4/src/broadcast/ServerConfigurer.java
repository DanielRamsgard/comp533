package broadcast;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import client.ICoupledClientSimulation;
import util.annotations.Tags;
import util.tags.DistributedTags;
import util.trace.port.consensus.ProposalLearnedNotificationSent;
import util.trace.port.consensus.RemoteProposeRequestReceived;
import util.trace.port.consensus.communication.CommunicationStateNames;

@Tags({DistributedTags.SERVER_CONFIGURER, DistributedTags.RMI})
public class ServerConfigurer {
	private List<ICoupledClientSimulation> clients;
	
	public ServerConfigurer() {
		this.clients = new ArrayList<>();
	}
	
	public void addClient(ICoupledClientSimulation o) {
		clients.add(o);
	}
	
	public void broadcast(String command, String sendingClientName) {
		RemoteProposeRequestReceived.newCase(this, CommunicationStateNames.COMMAND, -1, command);
		ProposalLearnedNotificationSent.newCase(this, CommunicationStateNames.COMMAND, -1, command);
		
		for (ICoupledClientSimulation client : clients) {
			try {
				if (!client.getClientName().equals(sendingClientName)) {
					client.notifyNewCommand(command);
				}
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
}

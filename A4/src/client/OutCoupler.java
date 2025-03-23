package client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;

import broadcast.ICoupledServerSimulation;
import stringProcessors.HalloweenCommandProcessor;
import util.annotations.Tags;
import util.tags.DistributedTags;
import util.trace.port.consensus.ProposalMade;
import util.trace.port.consensus.RemoteProposeRequestSent;
import util.trace.port.consensus.communication.CommunicationStateNames;
import util.trace.trickOrTreat.LocalCommandObserved;

@Tags({DistributedTags.CLIENT_OUT_COUPLER, DistributedTags.RMI})
public class OutCoupler implements PropertyChangeListener {
	private HalloweenCommandProcessor processer;
	private ClientConfigurer configurer;
	private String clientName;

	public OutCoupler(HalloweenCommandProcessor passedProcesser, ClientConfigurer passedConfigurer, String passedClientName) {
		this.processer = passedProcesser;
		this.configurer = passedConfigurer;
		this.clientName = passedClientName;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent anEvent) {		
		if (!anEvent.getPropertyName().equals("InputString")) return;
		String newCommand = (String) anEvent.getNewValue();
		
		LocalCommandObserved.newCase(this, newCommand);
		ProposalMade.newCase(this, CommunicationStateNames.COMMAND, -1, newCommand);
		RemoteProposeRequestSent.newCase(this, CommunicationStateNames.COMMAND, -1, newCommand);
		
		processer.processCommand(newCommand);
		ICoupledServerSimulation server = configurer.getRemoteServer();
		try {
			server.broadcast(newCommand, clientName);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	
}

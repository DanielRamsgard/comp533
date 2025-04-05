package client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;

import assignments.util.inputParameters.SimulationParametersListener;
import broadcast.ICoupledServerSimulation;
import stringProcessors.HalloweenCommandProcessor;
import util.annotations.Tags;
import util.tags.DistributedTags;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.PortTraceUtility;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.consensus.ProposalMade;
import util.trace.port.consensus.RemoteProposeRequestSent;
import util.trace.port.consensus.communication.CommunicationStateNames;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.rmi.RMITraceUtility;
import util.trace.trickOrTreat.LocalCommandObserved;
import util.interactiveMethodInvocation.IPCMechanism;

@Tags({DistributedTags.CLIENT_OUT_COUPLER, DistributedTags.RMI})
public class OutCoupler implements PropertyChangeListener, SimulationParametersListener {
	private HalloweenCommandProcessor processer;
	private String clientName;
	private ICoupledServerSimulation server;
	
	protected void setTracing() {
		PortTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		FactoryTraceUtility.setTracing();		
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		trace(true);
	}


	public OutCoupler(HalloweenCommandProcessor passedProcesser, String passedClientName, ICoupledServerSimulation passedServer) {
		setTracing();
		this.processer = passedProcesser;
		this.clientName = passedClientName;
		this.server = passedServer;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent anEvent) {	
		String name = anEvent.getPropertyName();
		if (name.equals("InputString")) {
			String newCommand = (String) anEvent.getNewValue();
			
			LocalCommandObserved.newCase(this, newCommand);
			ProposalMade.newCase(this, CommunicationStateNames.COMMAND, -1, newCommand);
			RemoteProposeRequestSent.newCase(this, CommunicationStateNames.COMMAND, -1, newCommand);
			
			try {
				server.broadcast(newCommand, clientName);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (name.equals("ipc_mechanism")) {
			IPCMechanism newIPC = (IPCMechanism) anEvent.getNewValue();
			
			try {
				server.alterIpc(newIPC, clientName);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	
		
	}
	
}

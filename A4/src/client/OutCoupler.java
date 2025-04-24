package client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;

import assignments.util.MiscAssignmentUtils;
import assignments.util.inputParameters.SimulationParametersListener;
import broadcast.ICoupledServerSimulation;
import broadcast.IGeneralizedIPCServer;
import inputport.nio.manager.NIOManager;
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

@Tags({DistributedTags.CLIENT_OUT_COUPLER, DistributedTags.RMI, DistributedTags.GIPC, DistributedTags.NIO})
public class OutCoupler implements PropertyChangeListener, SimulationParametersListener {
	private HalloweenCommandProcessor processer;
	private String clientName;
	private ICoupledServerSimulation server;
	private IGeneralizedIPCServer serverGIPC;
	private CoupledClientSimulation client;
	private NIOManager nioManager;
	private SocketChannel socketChannel;
	private ClientConfigurer configurer;
	
	protected void setTracing() {
		PortTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		FactoryTraceUtility.setTracing();		
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		trace(true);
	}


	public OutCoupler(ClientConfigurer configurer, SocketChannel socketChannel, NIOManager nioManager, CoupledClientSimulation client, HalloweenCommandProcessor passedProcesser, String passedClientName, ICoupledServerSimulation passedServer, IGeneralizedIPCServer serverGIPC) {
		setTracing();
		this.processer = passedProcesser;
		this.clientName = passedClientName;
		this.server = passedServer;
		this.serverGIPC = serverGIPC;
		this.client = client;
		this.nioManager = nioManager;
		this.socketChannel = socketChannel;
		this.configurer = configurer;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent anEvent) {
		if (!anEvent.getPropertyName().equals("InputString")) return;
		
		String newCommand = (String) anEvent.getNewValue();
		
		LocalCommandObserved.newCase(this, newCommand);
		ProposalMade.newCase(this, CommunicationStateNames.COMMAND, -1, newCommand);
		RemoteProposeRequestSent.newCase(this, CommunicationStateNames.COMMAND, -1, newCommand);
		
		// do GIPC or do RMI based on value
		IPCMechanism ipcState = client.getIPCMechanism();
		
		if (ipcState == IPCMechanism.RMI) {
			try {
				server.broadcast(newCommand, clientName);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (ipcState == IPCMechanism.GIPC) {
			serverGIPC.broadcastGIPC(newCommand, clientName);
		} else if (ipcState == IPCMechanism.NIO) {
			// use the socket channel to pass the new command to server for relay
			ByteBuffer aWriteMessage = ByteBuffer.wrap(newCommand.getBytes());
			ByteBuffer dup = MiscAssignmentUtils.deepDuplicate(aWriteMessage);
			nioManager.write(socketChannel, dup, configurer);
		}
		
	}
	
}

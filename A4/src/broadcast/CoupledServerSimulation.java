package broadcast;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.List;

import assignments.util.mainArgs.ClientArgsProcessor;
import client.ICoupledClientSimulation;
import client.IGeneralizedIPCClient;
import coupledsims.AStandAloneTwoCoupledHalloweenSimulations;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import util.annotations.Tags;
import util.interactiveMethodInvocation.IPCMechanism;
import util.interactiveMethodInvocation.SimulationParametersControllerFactory;
import util.tags.DistributedTags;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.PortTraceUtility;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.consensus.ProposalLearnedNotificationReceived;
import util.trace.port.consensus.ProposalLearnedNotificationSent;
import util.trace.port.consensus.ProposalMade;
import util.trace.port.consensus.ProposedStateSet;
import util.trace.port.consensus.RemoteProposeRequestReceived;
import util.trace.port.consensus.communication.CommunicationStateNames;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({DistributedTags.SERVER_REMOTE_OBJECT, DistributedTags.RMI})
public class CoupledServerSimulation extends AStandAloneTwoCoupledHalloweenSimulations implements ICoupledServerSimulation, IGeneralizedIPCServer {
	public static String SERVER_NAME = "SERVER";
	private ServerConfigurer configurer;
	private IPCMechanism ipcState;
	
	protected void setTracing() {
		PortTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		FactoryTraceUtility.setTracing();		
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		trace(true);
	}
	
	public CoupledServerSimulation() {
		setTracing();
		this.configurer = new ServerConfigurer();
		super.broadcastMetaState = true;
	}
	
	@Override
	public void registerClient(ICoupledClientSimulation o) {
		this.configurer.addClient(o);
	}
	
	public void registerClientGIPC(IGeneralizedIPCClient o) {
		this.configurer.addClientGIPC(o);
	}
	
	@Override
	public void alterIpc(IPCMechanism ipcState, String sendingClientName) {
		RemoteProposeRequestReceived.newCase(this, "ipc_mechanism", -1, ipcState);
		ProposalLearnedNotificationSent.newCase(this, "ipc_mechanism", -1, ipcState);
		List<ICoupledClientSimulation> clients = this.configurer.getClients();
		
		for (ICoupledClientSimulation client : clients) {
			try {
				if (!client.getClientName().equals(sendingClientName)) {
					client.notifyIPCUpdate(ipcState);
				}
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ProposedStateSet.newCase(this, "ipc_mechanism", -1, ipcState);
		this.ipcState = ipcState;
	}
	
	@Override
	public void broadcast(String command, String sendingClientName) {
		RemoteProposeRequestReceived.newCase(this, CommunicationStateNames.COMMAND, -1, command);
		ProposalLearnedNotificationSent.newCase(this, CommunicationStateNames.COMMAND, -1, command);
		List<ICoupledClientSimulation> clients = this.configurer.getClients();
		
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
	
	@Override
	public void broadcastGIPC(String command, String sendingClientName) {
		RemoteProposeRequestReceived.newCase(this, CommunicationStateNames.COMMAND, -1, command);
		ProposalLearnedNotificationSent.newCase(this, CommunicationStateNames.COMMAND, -1, command);
		List<IGeneralizedIPCClient> clients = this.configurer.getClientsGIPC();
		
		for (IGeneralizedIPCClient client : clients) {
			if (!client.getClientName().equals(sendingClientName)) {
				client.notifyNewCommandGIPC(command);
			}
		}
	}
	
	private void processArgsCustom(String[] args) {	
		System.out.println("Registry host:" + ClientArgsProcessor.getRegistryHost(args));
		System.out.println("Registry port:" + ClientArgsProcessor.getRegistryPort(args));
		System.out.println("Server host:" + ClientArgsProcessor.getServerHost(args));
		System.out.println("Headless:" + ClientArgsProcessor.getHeadless(args));
		System.out.println("Client name:" + ClientArgsProcessor.getClientName(args));

		// Make sure you set this property when processing args
		System.setProperty("java.awt.headless", ClientArgsProcessor.getHeadless(args));
		
	}
	
	public void startCustom(String[] args) {
		setTracing();
		processArgsCustom(args);
		// register a callback to process actions denoted by the user commands
		SimulationParametersControllerFactory.getSingleton().addSimulationParameterListener(this);
		// use the calling back library
		SimulationParametersControllerFactory.getSingleton().processCommands();
	}
	
	public Registry setupConnection(String serverHost, int serverPort) throws RemoteException {
		return this.configurer.setupConnection(serverHost, serverPort);
	}
	
	public GIPCRegistry setupConnectionGIPC(int serverGIPCPort) {
		return this.configurer.setupConnectionGIPC(serverGIPCPort);
	}
	
	public void performRebind(Registry rmiRegistry, ICoupledServerSimulation iCoupledServerSimulation) throws AccessException, RemoteException {
		this.configurer.performRebind(rmiRegistry, iCoupledServerSimulation, SERVER_NAME);
	}
	
	public void performRebindGIPC(GIPCRegistry gipcRegistry) {
		this.configurer.performRebindGIPC(gipcRegistry, this);
	}
	
	// happens locally so not a property change listener
	@Override
	public void ipcMechanism(IPCMechanism newValue) {
		ProposalMade.newCase(this, "ipc_mechanism", -1, newValue);
		List<ICoupledClientSimulation> clients = this.configurer.getClients();
		
		for (ICoupledClientSimulation client : clients) {
			try {
					client.notifyIPCUpdate(ipcState);
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ProposedStateSet.newCase(this, "ipc_mechanism", -1, ipcState);
		this.ipcState = newValue;
	}
 
}

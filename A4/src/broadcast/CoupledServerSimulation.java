package broadcast;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import assignments.util.inputParameters.AnAbstractSimulationParametersBean;
import assignments.util.inputParameters.SimulationParametersListener;
import assignments.util.mainArgs.ClientArgsProcessor;
import assignments.util.mainArgs.ServerArgsProcessor;
import client.ICoupledClientSimulation;
import client.IGeneralizedIPCClient;
import coupledsims.AStandAloneTwoCoupledHalloweenSimulations;
import inputport.nio.manager.NIOManager;
import inputport.nio.manager.NIOManagerFactory;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import util.annotations.Tags;
import util.interactiveMethodInvocation.IPCMechanism;
import util.interactiveMethodInvocation.SimulationParametersControllerFactory;
import util.tags.DistributedTags;
import util.trace.Tracer;
import util.trace.bean.BeanTraceUtility;
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
import util.trace.port.rpc.gipc.GIPCRPCTraceUtility;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({DistributedTags.SERVER_REMOTE_OBJECT, DistributedTags.RMI, DistributedTags.GIPC})
public class CoupledServerSimulation extends AnAbstractSimulationParametersBean implements ICoupledServerSimulation, IGeneralizedIPCServer, SimulationParametersListener {
	public static String SERVER_NAME = "SERVER";
	private ServerConfigurer configurer;
	private IPCMechanism ipcState;
	private NIOManager nioManager;
	private ServerSocketChannel socketChannel;
	private ArrayBlockingQueue<Intermediate> messagesQueue;
	public static final String READ_THREAD_NAME = "Read Thread";
	
	@Override
	public void trace(boolean value) {
		super.trace(value);
		Tracer.showInfo(isTrace());
	}
	
	protected void setTracing() {
		FactoryTraceUtility.setTracing();
		BeanTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		GIPCRPCTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		PortTraceUtility.setTracing();
		System.setProperty("java.awt.headless","true");
		System.setProperty("java.rmi.server.hostname", "localhost");
		trace(true);
	}
	
	@Override
	public void quit(int code) {
		System.exit(code);
	}
	
	public CoupledServerSimulation() {
		setTracing();
		this.configurer = new ServerConfigurer(messagesQueue);
		super.broadcastMetaState = true;
		this.nioManager = NIOManagerFactory.getSingleton();
		this.messagesQueue = new ArrayBlockingQueue<>(100000);
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
		setIPCMechanism(ipcState);
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
	
	// reading thread will call this method
	@Override
	public void broadcastNIO(Intermediate intermediate) throws IOException {
		List<SocketChannel> channels = this.configurer.getClientsNIO();
		
		for (SocketChannel channel : channels) {
			// notify the client of the new data via NIO
			// create a byte buffer and write it to client using the socket channel
			if (channel != intermediate.getSockerChannel()) {
				channel.write(intermediate.getByteBuffer());
			}
		}
	}
	
	private void processArgsCustom(String[] args) {	
		System.out.println("Registry host:" + ServerArgsProcessor.getRegistryHost(args));
		System.out.println("Registry port:" + ServerArgsProcessor.getRegistryPort(args));
		System.out.println("Server host:" + ServerArgsProcessor.getGIPCServerPort(args));
	}
		
	public void start(String[] args) {
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
	
	public GIPCRegistry setupConnectionGIPC(String[] args) {
		return this.configurer.setupConnectionGIPC(args);
	}
	
	public void performRebind(Registry rmiRegistry, ICoupledServerSimulation iCoupledServerSimulation) throws AccessException, RemoteException {
		this.configurer.performRebind(rmiRegistry, iCoupledServerSimulation, SERVER_NAME);
	}
	
	public void performRebindGIPC(GIPCRegistry gipcRegistry) {
		this.configurer.performRebindGIPC(gipcRegistry, this);
	}
	
	public void setupNIO(String[] args) throws IOException {
		this.socketChannel = this.configurer.setupNIO(nioManager, args);
	}
	
	// happens locally so not a property change listener
	@Override
	public void ipcMechanism(IPCMechanism newValue) {
		ProposalMade.newCase(this, "ipc_mechanism", -1, newValue);
		List<ICoupledClientSimulation> clients = this.configurer.getClients();
		
		if (isBroadcastMetaState()) {
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
	
	@Override
	public void broadcastMetaState(boolean newValue) {
		setBroadcastMetaState(newValue);
	}
	
	public void startReadingThread() {
		// start the reading thread and give it the references to messagesQueue and this
		Thread thread = new Thread(new ReadingThread(this, messagesQueue));
		thread.setName(READ_THREAD_NAME);
		
		thread.start();
	}
 
}

package client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ArrayBlockingQueue;

import assignments.util.inputParameters.AnAbstractSimulationParametersBean;
import assignments.util.inputParameters.SimulationParametersListener;
import assignments.util.mainArgs.ClientArgsProcessor;
import broadcast.CoupledServerSimulation;
import broadcast.ICoupledServerSimulation;
import broadcast.IGeneralizedIPCServer;
import broadcast.Intermediate;
import broadcast.ReadingThread;
import coupledsims.AStandAloneTwoCoupledHalloweenSimulations;
import coupledsims.Simulation;
import coupledsims.Simulation1;
import coupledsims.Simulation2;
import inputport.nio.manager.NIOManager;
import inputport.nio.manager.NIOManagerFactory;
import inputport.rpc.GIPCRegistry;
import main.BeauAndersonFinalProject;
import port.ATracingConnectionListener;
import stringProcessors.HalloweenCommandProcessor;
import util.annotations.Tags;
import util.interactiveMethodInvocation.IPCMechanism;
import util.interactiveMethodInvocation.SimulationParametersControllerFactory;
import util.misc.ThreadSupport;
import util.tags.DistributedTags;
import util.trace.Tracer;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.PortTraceUtility;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.consensus.ProposalLearnedNotificationReceived;
import util.trace.port.consensus.ProposalMade;
import util.trace.port.consensus.ProposedStateSet;
import util.trace.port.consensus.communication.CommunicationStateNames;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.gipc.GIPCRPCTraceUtility;
import util.trace.port.rpc.rmi.RMIObjectLookedUp;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({DistributedTags.CLIENT_REMOTE_OBJECT, DistributedTags.RMI, DistributedTags.GIPC})
public class CoupledClientSimulation extends AnAbstractSimulationParametersBean implements ICoupledClientSimulation, IGeneralizedIPCClient, SimulationParametersListener {
	private String clientName;
	private ClientConfigurer configurer;
	private ICoupledServerSimulation server;
	HalloweenCommandProcessor commandProcessor1;
	private IGeneralizedIPCServer serverGIPC;
	private OutCoupler simulation1Coupler;
	private NIOManager nioManager;
	private SocketChannel socketChannel;
	private ArrayBlockingQueue<Intermediate> messagesQueue;
	public static final String READ_THREAD_NAME = "Read Thread";
	
	
	@Override
	public void simulationCommand(String aCommand) {
		long aDelay = getDelay(); 
		if (aDelay > 0) {
			ThreadSupport.sleep(aDelay);
		}
		commandProcessor1.setInputString(aCommand);
	}
	
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
		trace(true);
	}
	
	@Override
	public void quit(int code) {
		System.exit(code);
	}

	
	public CoupledClientSimulation() {
		setTracing();
		this.messagesQueue = new ArrayBlockingQueue<>(100000);
		setIPCMechanism(IPCMechanism.NIO);		
		this.configurer = new ClientConfigurer(messagesQueue);
		setBroadcastMetaState(true);
		this.nioManager = NIOManagerFactory.getSingleton();
	}
	
	@Override
	public void setClientName(String name) {
		clientName = name;
	}
	
	@Override
	public String getClientName() {
		return clientName;
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
	
	public void setServer(ICoupledServerSimulation passedServer) {
		this.server = passedServer;
	}
	
	public void setServerGIPC(IGeneralizedIPCServer passedServer) {
		this.serverGIPC = passedServer;
	}
	
	public void setupNIO(String[] args) throws IOException {
		this.socketChannel = this.configurer.setupNIO(nioManager, args);
	}
	
	protected HalloweenCommandProcessor createSimulation1(String aPrefix) {
		return 	BeauAndersonFinalProject.createSimulation(
					aPrefix,
					Simulation1.SIMULATION1_X_OFFSET, 
					Simulation.SIMULATION_Y_OFFSET, 
					Simulation.SIMULATION_WIDTH, 
					Simulation.SIMULATION_HEIGHT, 
					Simulation1.SIMULATION1_X_OFFSET, 
					Simulation.SIMULATION_Y_OFFSET);
	}
	
	private void initCustom (String[] args) {
		setTracing();
		processArgsCustom(args);
		//Ideally the prefixes should be main arguments
		commandProcessor1 = createSimulation1(Simulation1.SIMULATION1_PREFIX);
		simulation1Coupler = new OutCoupler(configurer, socketChannel, nioManager, this, commandProcessor1, clientName, server, serverGIPC);
		commandProcessor1.addPropertyChangeListener(simulation1Coupler);
	}
	
	public void startCustom(String[] args) {
		initCustom(args);
		// register a callback to process actions denoted by the user commands
		SimulationParametersControllerFactory.getSingleton().addSimulationParameterListener(this);
		// use the calling back library
		SimulationParametersControllerFactory.getSingleton().processCommands();
	}

	@Override
	public void notifyNewCommand(String command) throws RemoteException {
		ProposalLearnedNotificationReceived.newCase(this, CommunicationStateNames.COMMAND, -1, command);
		ProposedStateSet.newCase(this, CommunicationStateNames.COMMAND, -1, command);
		
		commandProcessor1.processCommand(command);	
	}
	
	@Override
	public void notifyNewCommandGIPC(String command) {
		ProposalLearnedNotificationReceived.newCase(this, CommunicationStateNames.COMMAND, -1, command);
		ProposedStateSet.newCase(this, CommunicationStateNames.COMMAND, -1, command);
		
		commandProcessor1.processCommand(command);
	}
	
	public void notifyNewCommandNIO(Intermediate intermediate) {
		ByteBuffer aMessage = intermediate.getByteBuffer();
		String command = new String(aMessage.array(), aMessage.position(), aMessage.limit());
		
		System.out.println("COMMAND HERE: " + command);
		
		commandProcessor1.processCommand(command);
	}
	
	public Registry setupConnection(String clientHost, int clientPort) throws RemoteException {
		
		return this.configurer.setupConnection(clientHost, clientPort);
	}
	
	public GIPCRegistry setupConnectionGIPC(String clientHost, String clientName, String[] args){
		
		return this.configurer.setupConnectionGIPC(clientHost,  clientName, args);
	}
	
	public ICoupledServerSimulation performLookup(Registry rmiRegistry) throws AccessException, RemoteException, NotBoundException {
		ICoupledServerSimulation coupledServerSimulation = (ICoupledServerSimulation) rmiRegistry.lookup(broadcast.CoupledServerSimulation.SERVER_NAME);
		//
		RMIObjectLookedUp.newCase(this, coupledServerSimulation, this.getClientName(), rmiRegistry);
		//
		
		return coupledServerSimulation;
	}
	
	public IGeneralizedIPCServer performLookupGIPC(GIPCRegistry gipcRegistry) {
		return this.configurer.performLookupGIPC(gipcRegistry);
	}
	
	public IPCMechanism getIpcState() {
		return getIPCMechanism();
	}
	
	// happens locally so not a property change listener
	@Override
	public void ipcMechanism(IPCMechanism newValue) {
		ProposalMade.newCase(this, "ipc_mechanism", -1, newValue);
		
		this.configurer.setIPCChild(this, server, newValue);
				
	}
	
	// the server invokes this method
	@Override
	public void notifyIPCUpdate(IPCMechanism newIpcState) {
		ProposalLearnedNotificationReceived.newCase(this, "ipc_mechanism", -1, newIpcState);
		ProposedStateSet.newCase(this, "ipc_mechanism", -1, newIpcState);
		setIPCMechanism(newIpcState);
	}
	
	@Override
	public void broadcastMetaState(boolean newValue) {
		this.configurer.setBroadcastMetaStateChild(this, newValue);
	}
	
	public void startReadingThread() {
		// start the reading thread and give it the references to messagesQueue and this
		Thread thread = new Thread(new ClientReadingThread(messagesQueue, this));
		thread.setName(READ_THREAD_NAME);
		
		thread.start();
	} 
}

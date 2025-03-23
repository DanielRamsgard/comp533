package client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import assignments.util.mainArgs.ClientArgsProcessor;
import broadcast.ICoupledServerSimulation;
import coupledsims.AStandAloneTwoCoupledHalloweenSimulations;
import coupledsims.Simulation1;
import coupledsims.Simulation2;
import stringProcessors.HalloweenCommandProcessor;
import util.annotations.Tags;
import util.interactiveMethodInvocation.SimulationParametersControllerFactory;
import util.tags.DistributedTags;
import util.trace.port.consensus.ProposalLearnedNotificationReceived;
import util.trace.port.consensus.ProposedStateSet;
import util.trace.port.consensus.communication.CommunicationStateNames;
import util.trace.port.rpc.rmi.RMIObjectLookedUp;
import util.trace.port.rpc.rmi.RMIRegistryLocated;

@Tags({DistributedTags.CLIENT_REMOTE_OBJECT, DistributedTags.RMI})
public class CoupledClientSimulation extends AStandAloneTwoCoupledHalloweenSimulations implements ICoupledClientSimulation {
	private String clientName;
	private ClientConfigurer configurer;
	HalloweenCommandProcessor commandProcessor1; 
	HalloweenCommandProcessor commandProcessor2;
	
	@Override
	public void setClientName(String name) {
		clientName = name;
	}
	
	@Override
	public String getClientName() {
		return clientName;
	}
	
	public void setConfigurer(ICoupledServerSimulation server) {
		this.configurer = new ClientConfigurer(server);
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
	
	
	private void initCustom (String[] args) {
		setTracing();
		processArgsCustom(args);
		//Ideally the prefixes should be main args
		commandProcessor1 = createSimulation1(Simulation1.SIMULATION1_PREFIX);	
		commandProcessor2 = createSimulation2(Simulation2.SIMULATION2_PREFIX);
		simulation1Coupler = new OutCoupler(commandProcessor1, configurer, clientName);
		simulation2Coupler = new OutCoupler(commandProcessor2, configurer, clientName);
		commandProcessor1.addPropertyChangeListener(simulation2Coupler);
		commandProcessor2.addPropertyChangeListener(simulation1Coupler);
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
		commandProcessor2.processCommand(command);		
	}
	
	public Registry setupConnection(String clientHost, int clientPort) throws RemoteException {
		
		return this.configurer.setupConnection(clientHost, clientPort);
	}
	
	public ICoupledServerSimulation performLookup(Registry rmiRegistry) throws AccessException, RemoteException, NotBoundException {
		ICoupledServerSimulation coupledServerSimulation = (ICoupledServerSimulation) rmiRegistry.lookup(broadcast.CoupledServerSimulation.SERVER_NAME);
		//
		RMIObjectLookedUp.newCase(this, coupledServerSimulation, this.getClientName(), rmiRegistry);
		//
		
		return coupledServerSimulation;
	}
}

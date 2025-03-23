package broadcast;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import assignments.util.mainArgs.ClientArgsProcessor;
import client.ICoupledClientSimulation;
import client.OutCoupler;
import coupledsims.AStandAloneTwoCoupledHalloweenSimulations;
import coupledsims.Simulation1;
import coupledsims.Simulation2;
import util.annotations.Tags;
import util.interactiveMethodInvocation.SimulationParametersControllerFactory;
import util.tags.DistributedTags;
import util.trace.port.rpc.rmi.RMIObjectRegistered;
import util.trace.port.rpc.rmi.RMIRegistryLocated;

@Tags({DistributedTags.SERVER_REMOTE_OBJECT, DistributedTags.RMI})
public class CoupledServerSimulation extends AStandAloneTwoCoupledHalloweenSimulations implements ICoupledServerSimulation {
	public static String SERVER_NAME = "SERVER";
	private ServerConfigurer configurer;
	
	public CoupledServerSimulation() {
		this.configurer = new ServerConfigurer();
	}
	
	@Override
	public void registerClient(ICoupledClientSimulation o) {
		this.configurer.addClient(o);
	}
	
	@Override
	public void broadcast(String command, String sendingClientName) {
		this.configurer.broadcast(command, sendingClientName);
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
		Registry rmiRegistry = LocateRegistry.getRegistry(serverHost, serverPort);
		RMIRegistryLocated.newCase(this, serverHost, serverPort, rmiRegistry);
		
		return rmiRegistry;
	}
	
	public void performRebind(Registry rmiRegistry, ICoupledServerSimulation iCoupledServerSimulation) throws AccessException, RemoteException {
		rmiRegistry.rebind(SERVER_NAME, iCoupledServerSimulation);
		RMIObjectRegistered.newCase(this, SERVER_NAME, iCoupledServerSimulation, rmiRegistry);
	}

}

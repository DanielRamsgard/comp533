package broadcast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import assignments.util.inputParameters.SimulationParametersListener;
import assignments.util.mainArgs.ServerArgsProcessor;
import client.ICoupledClientSimulation;
import client.IGeneralizedIPCClient;
import inputport.nio.manager.NIOManager;
import inputport.nio.manager.listeners.SocketChannelAcceptListener;
import inputport.nio.manager.listeners.SocketChannelReadListener;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import port.ATracingConnectionListener;
import util.annotations.Tags;
import util.tags.DistributedTags;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.PortTraceUtility;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.nio.SocketChannelBound;
import util.trace.port.rpc.gipc.GIPCObjectRegistered;
import util.trace.port.rpc.gipc.GIPCRegistryCreated;
import util.trace.port.rpc.rmi.RMIObjectRegistered;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({DistributedTags.SERVER_CONFIGURER, DistributedTags.RMI, DistributedTags.GIPC})
public class ServerConfigurer implements SimulationParametersListener, SocketChannelAcceptListener, SocketChannelReadListener {
	private List<ICoupledClientSimulation> clients;
	private List<IGeneralizedIPCClient> clientsGIPC;
	private List<SocketChannel> clientChannels;
	
	protected void setTracing() {
		PortTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		FactoryTraceUtility.setTracing();		
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		trace(true);
	}
	
	public ServerConfigurer() {
		setTracing();
		this.clients = new ArrayList<>();
		this.clientsGIPC = new ArrayList<>();
	}
	
	public void addClient(ICoupledClientSimulation o) {
		clients.add(o);
	}
	
	public void addClientGIPC(IGeneralizedIPCClient o) {
		clientsGIPC.add(o);
	}
	
	public List<ICoupledClientSimulation> getClients() {
		return clients;
	}
	
	public List<IGeneralizedIPCClient> getClientsGIPC() {
		return clientsGIPC;
	}
	
	public List<SocketChannel> getClientsNIO() {
		return clientChannels;
	}
	
	public void performRebind(Registry rmiRegistry, ICoupledServerSimulation iCoupledServerSimulation, String serverName) throws AccessException, RemoteException {
		rmiRegistry.rebind(serverName, iCoupledServerSimulation);		
	}
	
	public void performRebindGIPC(GIPCRegistry gipcRegistry, ICoupledServerSimulation iCoupledServerSimulation) {
		gipcRegistry.rebind(CoupledServerSimulation.SERVER_NAME, iCoupledServerSimulation);
		gipcRegistry.getInputPort().addConnectionListener(new ATracingConnectionListener(gipcRegistry.getInputPort()));
		
		GIPCObjectRegistered.newCase(this, null, iCoupledServerSimulation, gipcRegistry);
	}
	
	public Registry setupConnection(String serverHost, int serverPort) throws RemoteException {
		Registry rmiRegistry = LocateRegistry.getRegistry(serverHost, serverPort);
		RMIRegistryLocated.newCase(this, serverHost, serverPort, rmiRegistry);
		
		return rmiRegistry;
	}
	
	public GIPCRegistry setupConnectionGIPC(String[] args) {
		int port = ServerArgsProcessor.getGIPCServerPort(args);
		GIPCRegistry gipcRegistry = GIPCLocateRegistry.createRegistry(port);
		
		GIPCRegistryCreated.newCase(gipcRegistry, port);
		
		return gipcRegistry;
	}
	
	public ServerSocketChannel setupNIO(NIOManager nioManager, String[] args) throws IOException {
		int port = ServerArgsProcessor.getNIOServerPort(args);
		
		ServerSocketChannel aServerFactoryChannel = ServerSocketChannel.open();
		InetSocketAddress anInternetSocketAddress = new InetSocketAddress(port);
		aServerFactoryChannel.socket().bind(anInternetSocketAddress);
		SocketChannelBound.newCase(this, aServerFactoryChannel, anInternetSocketAddress);
		nioManager.enableListenableAccepts(aServerFactoryChannel, SelectionKey.OP_READ, this);
		
		return aServerFactoryChannel;
	}

	@Override
	public void socketChannelRead(SocketChannel arg0, ByteBuffer arg1, int arg2) {
		// add to ArrayBlockingQueue and read thread will invoke server method to invoke a broadcast to all clients 
	}

	@Override
	public void socketChannelAccepted(ServerSocketChannel arg0, SocketChannel arg1) {
		// TODO Auto-generated method stub
		clientChannels.add(arg1);
	}
}

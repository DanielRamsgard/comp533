package client;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ArrayBlockingQueue;

import assignments.util.MiscAssignmentUtils;
import assignments.util.inputParameters.SimulationParametersListener;
import assignments.util.mainArgs.ClientArgsProcessor;
import broadcast.CoupledServerSimulation;
import broadcast.ICoupledServerSimulation;
import broadcast.IGeneralizedIPCServer;
import broadcast.Intermediate;
import inputport.nio.manager.NIOManager;
import inputport.nio.manager.listeners.SocketChannelConnectListener;
import inputport.nio.manager.listeners.SocketChannelReadListener;
import inputport.nio.manager.listeners.SocketChannelWriteListener;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import port.ATracingConnectionListener;
import util.annotations.Tags;
import util.interactiveMethodInvocation.IPCMechanism;
import util.tags.DistributedTags;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.PortTraceUtility;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.consensus.ProposedStateSet;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.gipc.GIPCObjectLookedUp;
import util.trace.port.rpc.gipc.GIPCRegistryLocated;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({DistributedTags.CLIENT_CONFIGURER, DistributedTags.RMI, DistributedTags.GIPC, DistributedTags.NIO})
public class ClientConfigurer implements SimulationParametersListener, SocketChannelConnectListener, SocketChannelWriteListener, SocketChannelReadListener {
	private ArrayBlockingQueue<Intermediate> messagesQueue;
	private NIOManager nioManager;
	
	protected void setTracing() {
		PortTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		FactoryTraceUtility.setTracing();		
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		trace(true);
	}
	
	public ClientConfigurer(ArrayBlockingQueue<Intermediate> messagesQueue, NIOManager nioManager) { 
		setTracing();
		this.messagesQueue = messagesQueue;
		this.nioManager = nioManager;
	}
	
	public Registry setupConnection(String clientHost, int clientPort) throws RemoteException {
		Registry rmiRegistry = LocateRegistry.getRegistry(clientHost, clientPort);
		RMIRegistryLocated.newCase(this, clientHost, clientPort, rmiRegistry);
		
		return rmiRegistry;
	}
	
	public GIPCRegistry setupConnectionGIPC(String clientHost, String clientName, String[] args) {
		int port = ClientArgsProcessor.getGIPCPort(args);
		GIPCRegistry gipcRegistry = GIPCLocateRegistry.getRegistry(clientHost, port, clientName);
		
		GIPCRegistryLocated.newCase(this, clientHost, port, clientName);
		
		return gipcRegistry;
	}
	
	public void setBroadcastMetaStateChild(CoupledClientSimulation coupledClientSimulation, boolean newValue) {
		coupledClientSimulation.setBroadcastMetaState(newValue);
	}
	
	public void setIPCChild(CoupledClientSimulation coupledClientSimulation, ICoupledServerSimulation server, IPCMechanism newValue) {
		if (coupledClientSimulation.isBroadcastMetaState()) {
			try {
				server.alterIpc(newValue, ClientArgsProcessor.getClientName(null));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ProposedStateSet.newCase(this, "ipc_mechanism", -1, newValue);
			coupledClientSimulation.setIPCMechanism(newValue);
		}
	}
	
	public IGeneralizedIPCServer performLookupGIPC(GIPCRegistry gipcRegistry) {
		IGeneralizedIPCServer retServer = (IGeneralizedIPCServer) gipcRegistry.lookup(IGeneralizedIPCServer.class, CoupledServerSimulation.SERVER_NAME);
		
		gipcRegistry.getInputPort().addConnectionListener(new ATracingConnectionListener(gipcRegistry.getInputPort()));
		
		GIPCObjectLookedUp.newCase(this, retServer, getClass(), null, gipcRegistry);
		
		return retServer;
	}
	
	public SocketChannel setupNIO(String[] args) throws IOException {
		int port = ClientArgsProcessor.getNIOServerPort(args);
		
		SocketChannel socketChannel = SocketChannel.open();
		InetAddress aServerAddress = InetAddress.getByName("localhost");
		nioManager.connect(socketChannel, aServerAddress, port, SelectionKey.OP_READ, this);		
		
		return socketChannel;
	}

	@Override
	public void written(SocketChannel serverChannel, ByteBuffer aMessage, int aLength) {
		// nothing
	}

	@Override
	public void connected(SocketChannel connectedChannel) {
		// add as read listener
		nioManager.addReadListener(connectedChannel, this);
		
	}

	@Override
	public void notConnected(SocketChannel arg0, Exception arg1) {
		// log the error
		arg1.printStackTrace();
		
	}

	@Override
	public void socketChannelRead(SocketChannel channel, ByteBuffer aMessage, int aLength) {
		// TODO Auto-generated method stub
		// write to shared ArrayBlockingQueue between client and reading thread
		ByteBuffer newBuffer = MiscAssignmentUtils.deepDuplicate(aMessage);
		
		Intermediate intermediate = new Intermediate(newBuffer, channel, aLength);
		
		messagesQueue.add(intermediate);
	}
}

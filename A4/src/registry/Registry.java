package registry;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import assignments.util.inputParameters.SimulationParametersListener;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.PortTraceUtility;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.rmi.RMIRegistryCreated;
import util.trace.port.rpc.rmi.RMITraceUtility;

public class Registry implements SimulationParametersListener {
	protected void setTracing() {
		PortTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		FactoryTraceUtility.setTracing();		
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		trace(true);
	}
	
	public void createRegistry(int port) throws RemoteException, InterruptedException {
		setTracing();
		RMITraceUtility.setTracing();
		LocateRegistry.createRegistry(port);
		RMIRegistryCreated.newCase(Main.class, port);
		Thread.sleep(Long.MAX_VALUE);
	}
}

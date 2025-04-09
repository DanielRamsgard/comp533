package registry;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

import assignments.util.mainArgs.RegistryArgsProcessor;
import util.annotations.Tags;
import util.tags.DistributedTags;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.PortTraceUtility;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.rmi.RMIRegistryCreated;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({DistributedTags.REGISTRY, DistributedTags.RMI})
public class Main {
	public static void main(String[] args) {
		int port = RegistryArgsProcessor.getRegistryPort(args);
		
		try {
			Registry registry = new Registry();
			registry.createRegistry(port);
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();
		} catch (RemoteException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

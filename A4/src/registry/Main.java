package registry;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import assignments.util.mainArgs.RegistryArgsProcessor;
import util.annotations.Tags;
import util.tags.DistributedTags;
import util.trace.port.rpc.rmi.RMIRegistryCreated;

@Tags({DistributedTags.REGISTRY, DistributedTags.RMI})
public class Main {
	public static void main(String[] args) {
		int port = RegistryArgsProcessor.getRegistryPort(args);
		
		try {
			LocateRegistry.createRegistry(port);
			RMIRegistryCreated.newCase(Main.class, port);
			Thread.sleep(Long.MAX_VALUE);
		} catch (RemoteException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

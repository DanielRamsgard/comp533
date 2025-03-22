package registry;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import assignments.util.mainArgs.RegistryArgsProcessor;
import util.trace.port.rpc.rmi.RMIRegistryCreated;

public class Main {
	public static void main(String[] args) {
		int port = RegistryArgsProcessor.getRegistryPort(args);
		
		try {
			LocateRegistry.createRegistry(port);
			RMIRegistryCreated.newCase(Main.class, port);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

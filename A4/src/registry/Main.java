package registry;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import assignments.util.mainArgs.RegistryArgsProcessor;

public class Main {
	public static void main(String[] args) {
		int port = RegistryArgsProcessor.getRegistryPort(args);
		
		try {
			LocateRegistry.createRegistry(port);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

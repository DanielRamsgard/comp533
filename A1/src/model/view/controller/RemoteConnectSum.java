package model.view.controller;

import java.util.Scanner;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RemoteConnectSum {
	static int SERVER_PORT = 4999;
	static String MODEL_NAME = "model";

	private final static void start() {
		final Model model = new Model();
		
		try {
			Registry rmiRegistry = LocateRegistry.createRegistry(SERVER_PORT);
			UnicastRemoteObject.exportObject(model, 0);
			rmiRegistry.rebind(MODEL_NAME, model);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
			
		final PropertyChangeListener view = new View();
		
		model.addPropertyChangeListener(view);
		
		final Controller controller = new Controller(model);
		
		final Scanner scanner = new Scanner(System.in);
		
		controller.gatherInputFromScanner(scanner, true);
	}
	
 	public static void main(final String[] args) {		
 		start();
	}
}

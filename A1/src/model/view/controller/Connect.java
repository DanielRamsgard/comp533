package model.view.controller;

import java.util.Scanner;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Connect {
	public static int SERVER_PORT = 8000;
	public static String MODEL_NAME = "model";
	
	private static void start() {
		final Model model = new Model();
		
		try {
			Registry rmiRegistry = LocateRegistry.createRegistry(SERVER_PORT);
			UnicastRemoteObject.exportObject(model, 0);
			rmiRegistry.rebind(MODEL_NAME, model);
		} catch (RemoteException e) {
			System.out.println(e.getMessage());
		}
		
		final PropertyChangeListener view = new View();
		
		model.addPropertyChangeListener(view);
		
		final Controller controller = new Controller(model);
		
		final Scanner scanner = new Scanner(System.in);
		
		controller.gatherInputFromScanner(scanner, false);
	}
	
	public static void main(final String[] args) {
		start();
	}
}

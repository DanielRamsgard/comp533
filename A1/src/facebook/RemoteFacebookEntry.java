package facebook;

import java.util.Scanner;

import model.view.controller.Controller;
import model.view.controller.Model;
import model.view.controller.View;

import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RemoteFacebookEntry {
	public static int SERVER_PORT = 50000;
	public static String MODEL_NAME = "facebookModel";
	
	private static void start() {
		final FacebookModel facebookModel = new FacebookModel();
		
		try {
			Registry rmiRegistry = LocateRegistry.createRegistry(SERVER_PORT);
			UnicastRemoteObject.exportObject(facebookModel, 0);
			rmiRegistry.rebind(MODEL_NAME, facebookModel);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		final PropertyChangeListener view = new View();
		
		facebookModel.addPropertyChangeListener(view);
		
		final FacebookController facebookController = new FacebookController(facebookModel);
		
		final Scanner scanner = new Scanner(System.in);
		
		facebookController.gatherInputFromScanner(scanner);
	}
	
	public static void main(final String[] args) {
		start();
	}
}

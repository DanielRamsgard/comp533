package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import model.view.controller.RemoteConnect;
import model.view.controller.RemoteModel;

public class MainClient {
	static String SERVER_HOST_NAME = "localhost";
	
	public static void main(String[] args) throws NotBoundException {
		try {			
			Registry rmiRegistry = LocateRegistry.getRegistry(SERVER_HOST_NAME, RemoteConnect.SERVER_PORT);
		    RemoteModel remoteModel = (RemoteModel) rmiRegistry.lookup(RemoteConnect.MODEL_NAME);
		    Client client = new ClientImpl(remoteModel);
		    remoteModel.registerRemoteClient(client);
		    client.block();
		} catch (RemoteException e) {
			System.out.println(e.getMessage());
		}
	}
}

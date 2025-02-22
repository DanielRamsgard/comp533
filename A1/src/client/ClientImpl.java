package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import model.view.controller.Connect;
import model.view.controller.RemoteModel;

public class ClientImpl implements Client {
	static String SERVER_HOST_NAME = "localhost";

	
	public ClientImpl() throws NotBoundException {
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry(SERVER_HOST_NAME, Connect.SERVER_PORT);
		    RemoteModel remoteModel = (RemoteModel) rmiRegistry.lookup(Connect.MODEL_NAME);
		    remoteModel.registerRemoteClient(this);
		} catch (RemoteException e) {
			System.out.println(e.getMessage());
		}
	}
}

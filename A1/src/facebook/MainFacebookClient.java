package facebook;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import model.view.controller.RemoteConnect;
import model.view.controller.RemoteModel;

public class MainFacebookClient {
	static String SERVER_HOST_NAME = "localhost";
	
	public static void main(String[] args) throws NotBoundException {
		try {			
			Registry rmiRegistry = LocateRegistry.getRegistry(SERVER_HOST_NAME, RemoteFacebookEntry.SERVER_PORT);
		    RemoteModel remoteModel = (RemoteModel) rmiRegistry.lookup(RemoteFacebookEntry.MODEL_NAME);
		    FacebookClientImpl facebookClientImpl = new FacebookClientImpl(remoteModel);
		    
		    FacebookClient client = (FacebookClient) UnicastRemoteObject.exportObject(facebookClientImpl, 0);
		    
//		    remoteModel.registerRemoteClient(facebookClientImpl);
		    facebookClientImpl.block();
		    			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}

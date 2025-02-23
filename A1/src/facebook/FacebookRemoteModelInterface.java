package facebook;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import client.Client;
import key.value.KeyValue;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FacebookRemoteModelInterface extends Remote {
	public void registerRemoteClient(FacebookClient facebookClient) throws RemoteException;
	public List<LinkedList<KeyValue<String, List<String>>>> getReductionQueueListRemote() throws RemoteException;
	public void quit() throws RemoteException;
}

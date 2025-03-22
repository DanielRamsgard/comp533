package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICoupledClientSimulation extends Remote {
	void setClientName(String name) throws RemoteException;
	String getClientName() throws RemoteException;
}

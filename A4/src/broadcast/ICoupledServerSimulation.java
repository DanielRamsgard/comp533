package broadcast;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.ICoupledClientSimulation;

public interface ICoupledServerSimulation extends Remote {
	void registerClient(ICoupledClientSimulation o) throws RemoteException;
}

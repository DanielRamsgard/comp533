package broadcast;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICoupledServerSimulation extends Remote {
	void registerClient(Object o) throws RemoteException;
}

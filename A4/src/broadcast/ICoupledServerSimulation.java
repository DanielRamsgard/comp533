package broadcast;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.ICoupledClientSimulation;
import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({DistributedTags.SERVER_REMOTE_INTERFACE, DistributedTags.RMI})
public interface ICoupledServerSimulation extends Remote {
	void registerClient(ICoupledClientSimulation o) throws RemoteException;
}

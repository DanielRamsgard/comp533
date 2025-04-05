package broadcast;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.ICoupledClientSimulation;
import util.annotations.Tags;
import util.interactiveMethodInvocation.IPCMechanism;
import util.tags.DistributedTags;

@Tags({DistributedTags.SERVER_REMOTE_INTERFACE, DistributedTags.RMI})
public interface ICoupledServerSimulation extends Remote {
	void registerClient(ICoupledClientSimulation o) throws RemoteException;
	void broadcast(String command, String sendingClientName) throws RemoteException;
	void alterIpc(IPCMechanism newIpc, String sendingClientName) throws RemoteException;
}

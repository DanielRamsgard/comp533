package broadcast;

import client.IGeneralizedIPCClient;
import util.interactiveMethodInvocation.IPCMechanism;

public interface IGeneralizedIPCServer {
	void registerClientGIPC(IGeneralizedIPCClient o);
	void broadcastGIPC(IPCMechanism ipcState, String sendingClientName);
}

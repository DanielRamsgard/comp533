package broadcast;

import client.IGeneralizedIPCClient;
import util.interactiveMethodInvocation.IPCMechanism;

public interface IGeneralizedIPCServer {
	void registerClientGIPC(IGeneralizedIPCClient o);
	void broadcastGIPC(String command, String sendingClientName);
}

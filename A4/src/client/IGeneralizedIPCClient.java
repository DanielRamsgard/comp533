package client;

import util.interactiveMethodInvocation.IPCMechanism;

public interface IGeneralizedIPCClient {
	void notifyIPCUpdate(IPCMechanism ipcState);
	void notifyNewCommandGIPC(String command);
	String getClientName();
}

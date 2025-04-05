package client;

import util.interactiveMethodInvocation.IPCMechanism;

public interface IGeneralizedIPCClient {
	void notifyNewCommandGIPC(IPCMechanism ipcState);
	String getClientName();
	void notifyNewCommandGIPC(String command);
}

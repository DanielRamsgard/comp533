package client;

import util.annotations.Tags;
import util.interactiveMethodInvocation.IPCMechanism;
import util.tags.DistributedTags;

@Tags({DistributedTags.CLIENT_REMOTE_INTERFACE, DistributedTags.GIPC})
public interface IGeneralizedIPCClient {
	void notifyIPCUpdate(IPCMechanism ipcState);
	void notifyNewCommandGIPC(String command);
	String getClientName();
}

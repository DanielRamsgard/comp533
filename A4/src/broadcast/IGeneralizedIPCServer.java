package broadcast;

import java.io.IOException;
import java.nio.ByteBuffer;

import client.IGeneralizedIPCClient;
import util.annotations.Tags;
import util.interactiveMethodInvocation.IPCMechanism;
import util.tags.DistributedTags;

@Tags({DistributedTags.SERVER_REMOTE_INTERFACE, DistributedTags.GIPC})
public interface IGeneralizedIPCServer {
	void registerClientGIPC(IGeneralizedIPCClient o);
	void broadcastGIPC(String command, String sendingClientName);
	void broadcastNIO(Intermediate intermediate) throws IOException;
}

package slave;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import key.value.KeyValue;

public interface RemoteSlave {
	Map<String, Integer> remoteCallReduce() throws RemoteException;
}

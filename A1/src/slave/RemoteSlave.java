package slave;

import java.rmi.RemoteException;
import java.util.List;

import key.value.KeyValue;

public interface RemoteSlave {
	void remoteCallProducMap() throws RemoteException;
}

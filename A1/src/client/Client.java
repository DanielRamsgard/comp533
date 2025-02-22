package client;

import java.rmi.RemoteException;
import java.util.List;

import key.value.KeyValue;

public interface Client {
	void remoteProduceMap(List<KeyValue<String, Integer>> inputList) throws RemoteException;
}

package client;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import key.value.KeyValue;

public interface Client extends Serializable {
	Map<String, Integer> reduce(List<KeyValue<String, Integer>> myList) throws RemoteException;
	void quit() throws RemoteException;
	void block();
}

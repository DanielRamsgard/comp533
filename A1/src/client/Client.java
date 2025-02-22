package client;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import key.value.KeyValue;

public interface Client {
	Map<String, Integer> reduce(List<KeyValue<String, Integer>> myList) throws RemoteException;
}

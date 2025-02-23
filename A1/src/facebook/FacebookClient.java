package facebook;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import key.value.KeyValue;

public interface FacebookClient extends Remote {
	public Map<String, List<String>> reduce(List<KeyValue<String, List<String>>> input) throws RemoteException;
	void quit() throws RemoteException;
}

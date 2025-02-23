package facebook;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface FacebookRemoteSlave {
	Map<String, List<String>> remoteCallReduce() throws RemoteException;
}

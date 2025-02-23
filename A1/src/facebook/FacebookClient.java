package facebook;

import java.rmi.Remote;
import java.util.List;
import java.util.Map;

public interface FacebookClient extends Remote {
	public Map<String, List<String>> reduce(List<Map<String, List<String>>> finalMappingResult);
}

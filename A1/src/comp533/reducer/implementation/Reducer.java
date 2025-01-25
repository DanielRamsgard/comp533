package comp533.reducer.implementation;

import java.util.List;
import java.util.Map;
import key.value.KeyValue;

public interface Reducer<T, E> {
	public Map<T, E> reduce(List<KeyValue<T, E>> myList);
}

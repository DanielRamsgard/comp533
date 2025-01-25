package reducer.implementation;

import java.util.*;
import key.value.*;

public interface Reducer<T, E> {
	public Map<T, E> reduce(List<KeyValue<T, E>> myList);
}

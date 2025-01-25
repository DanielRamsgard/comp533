package mapper;

import java.util.*;

import key.value.KeyValue;

public interface Mapper<T, E> {
	 List<KeyValue<T, E>> map(List<String> aStrings);
}

package assignment.mapper;

import java.util.*;

import key.value.KeyValue;

public interface AssignmentMapper<T, E> {
	 public List<KeyValue<T, E>> map(List<String> aStrings);
}

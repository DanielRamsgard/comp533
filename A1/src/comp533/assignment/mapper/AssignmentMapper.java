package comp533.assignment.mapper;

import java.util.List;

import key.value.KeyValue;

public interface AssignmentMapper<T, E> {
	 public List<KeyValue<T, E>> map(List<String> aStrings);
}

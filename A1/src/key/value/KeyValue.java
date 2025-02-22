package key.value;

import java.io.Serializable;

public interface KeyValue<T, E> extends Serializable {
	public T getKey();
	
	public E getValue();
	
	public void setValue(E value);
	
}

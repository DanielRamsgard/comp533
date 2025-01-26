package key.value;

public interface KeyValue<T, E> {
	public T getKey();
	
	public E getValue();
	
	public void setValue(E value);
	
}

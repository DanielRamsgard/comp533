package key.value;

public interface KeyValue<T, E> {
	public T getkey();
	
	public E getValue();
	
	public void setKey(T value);
	
}

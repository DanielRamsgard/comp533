package key.value;

public interface KeyValue<T, E> {
	public T getkey();
	
	public E getValue();
	
	public void setValue(E value);
	
}

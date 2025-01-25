package key.value;

public class KeyValueImpl<T, E> implements KeyValue<T, E> {
	private T key;
	private E value;

	@Override
	public T getkey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKey(T value) {
		// TODO Auto-generated method stub
		
	}
	
	public String toString() {
		return "(" + key + "," + value + ")";
	}

}

package key.value;

public class KeyValueImpl<T, E> implements KeyValue<T, E> {
	private T key;
	private E value;
	
	public KeyValueImpl(T key, E value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public T getkey() {
		// TODO Auto-generated method stub
		return key;
	}

	@Override
	public E getValue() {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public void setValue(E newValue) {
		// TODO Auto-generated method stub
		this.value = newValue;
		
	}
	
	public String toString() {
		return "(" + key + ",1)";
	}

}

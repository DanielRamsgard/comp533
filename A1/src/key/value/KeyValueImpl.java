package key.value;

public class KeyValueImpl<T, E> implements KeyValue<T, E> {
	private final T key;
	private E value;
	
	public KeyValueImpl(final T k, final E v) {
		this.key = k;
		this.value = v;
	}

	@Override
	public T getKey() {
		// TODO Auto-generated method stub
		return key;
	}

	@Override
	public E getValue() {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public void setValue(final E newValue) {
		// TODO Auto-generated method stub
		this.value = newValue;
		
	}
	
	@Override
	public String toString() {
		return "(" + key + ", " + value + ")";
	}

}

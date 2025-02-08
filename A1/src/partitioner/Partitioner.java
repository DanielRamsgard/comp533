package partitioner;

public interface Partitioner<K, V> {
	public int getPartition(K key,V value, int numberOfPartitions);
}

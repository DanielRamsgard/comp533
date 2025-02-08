package comp533.partitioner;

public interface Partitioner<K, V> {
	public int getPartition(K key,V value, int numberOfPartitions);
}

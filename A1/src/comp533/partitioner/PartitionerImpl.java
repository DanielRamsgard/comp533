package comp533.partitioner;

public class PartitionerImpl<V> implements Partitioner<String, V> {
	public int getPartition(String key, V value, int numberOfPartitions) {
		
		char firstLetter = key.charAt(0);
		
		if (Character.isLetter(firstLetter)) {
			firstLetter = Character.toLowerCase(firstLetter);
			
			int offset = firstLetter - 'a';
			
			double maxPartitionSize = Math.ceil(('z' - 'a' + 1) / ((double) numberOfPartitions));
			double calculatedPartition = Math.floor((offset + 1) / maxPartitionSize);
			
			return (int) calculatedPartition;
		} else {
			return 0;
		}
	}
}

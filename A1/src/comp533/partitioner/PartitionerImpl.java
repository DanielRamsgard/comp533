package comp533.partitioner;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class PartitionerImpl<V> extends AMapReduceTracer implements Partitioner<String, V> {
	public int getPartition(String key, V value, int numberOfPartitions) {
		
		char firstLetter = key.charAt(0);
		
		if (Character.isLetter(firstLetter)) {
			firstLetter = Character.toLowerCase(firstLetter);
			
			int offset = firstLetter - 'a';
			
			double maxPartitionSize = Math.ceil(('z' - 'a' + 1) / ((double) numberOfPartitions));
			double calculatedPartition = Math.floor((offset + 1) / maxPartitionSize);
			
			super.tracePartitionAssigned(key, value, (int) calculatedPartition, numberOfPartitions);
			
			return (int) calculatedPartition;
		} else {
			super.tracePartitionAssigned(key, value, 0, numberOfPartitions);
			
			return 0;
		}
	}
}

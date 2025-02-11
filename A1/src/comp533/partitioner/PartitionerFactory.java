package comp533.partitioner;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class PartitionerFactory extends AMapReduceTracer {
	public static Partitioner partitioner;
	
	public static void setPartitioner() {
		if (partitioner == null) {
			partitioner = new PartitionerImpl<Integer>();
		}				
	}
	
	public PartitionerFactory() {
		
	}
	
	public static Partitioner getPartitioner() {
		setPartitioner();
		
		PartitionerFactory factory = new PartitionerFactory();
//		factory.tracePartitionAssigned(factory, factory, 0, 0);
		factory.traceSingletonChange(PartitionerFactory.class, partitioner);
		
		return partitioner;
	}
	
	public String toString() {
		return super.PARTITIONER;
	}
}

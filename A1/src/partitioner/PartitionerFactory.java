package partitioner;

public class PartitionerFactory {
	public static Partitioner partitioner;
	
	public static void setPartitioner() {
		if (partitioner == null) {
			partitioner = new PartitionerImpl();
		}				
	}
	
	public static Partitioner getPartitioner() {
		setPartitioner();
		
		return partitioner;
	}
}

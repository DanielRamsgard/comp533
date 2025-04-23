package broadcast;

import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({DistributedTags.READ_THREAD_INTERFACE, DistributedTags.NIO})
public interface IReadingThread extends Runnable {
	
}

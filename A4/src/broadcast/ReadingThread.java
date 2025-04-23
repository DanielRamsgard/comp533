package broadcast;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({DistributedTags.SERVER_READ_THREAD, DistributedTags.NIO})
public class ReadingThread implements IReadingThread {
	private CoupledServerSimulation coupledServerSimulation;
	private ArrayBlockingQueue<Intermediate> arrayBlockingQueue;
	
	public ReadingThread(CoupledServerSimulation coupledServerSimulation, ArrayBlockingQueue<Intermediate> arrayBlockingQueue) {
		this.coupledServerSimulation = coupledServerSimulation;
		this.arrayBlockingQueue = arrayBlockingQueue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Intermediate intermediate = arrayBlockingQueue.take();
				coupledServerSimulation.broadcastNIO(intermediate);
				
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

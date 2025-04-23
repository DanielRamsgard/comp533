package client;

import java.util.concurrent.ArrayBlockingQueue;

import broadcast.IReadingThread;
import broadcast.Intermediate;
import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({DistributedTags.CLIENT_READ_THREAD, DistributedTags.NIO})
public class ClientReadingThread implements IReadingThread {
	private ArrayBlockingQueue<Intermediate> arrayBlockingQueue;
	private CoupledClientSimulation clientSim;
	
	public ClientReadingThread(ArrayBlockingQueue<Intermediate> arrayBlockingQueue, CoupledClientSimulation clientSim) {
		this.arrayBlockingQueue = arrayBlockingQueue;
		this.clientSim = clientSim;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Intermediate intermediate = arrayBlockingQueue.take();
				clientSim.notifyNewCommandNIO(intermediate);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

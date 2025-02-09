package comp533.joiner;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class JoinerImpl extends AMapReduceTracer implements Joiner {
	private int count;
	
	public JoinerImpl(int count) {
		this.count = count;
		
		super.traceJoinerCreated(JOINER, count);
	}
	
	public synchronized void finished() {
		super.traceJoinerFinishedTask(JOINER, count, count);
		this.count -= 1;		
	}
	
	public synchronized void join() {
		while (count != 0) {
			try {
				super.traceJoinerWaitStart(JOINER, count, count);
				this.wait();
				super.traceJoinerWaitStart(JOINER, count, count);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		notify();
		super.traceJoinerRelease(JOINER, count, count);
	}
	
	public String toString() {
		return super.JOINER;
	}
}

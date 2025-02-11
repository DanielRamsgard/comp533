package comp533.joiner;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class JoinerImpl extends AMapReduceTracer implements Joiner {
	private int threadCount;
	private int currentThreadCount;
	
	
	public JoinerImpl(int threadCount) {
		this.threadCount = threadCount;
		this.currentThreadCount = 0;
		
		super.traceJoinerCreated(JOINER, threadCount);
	}
	
	public synchronized void finished() {
		currentThreadCount += 1;
		super.traceJoinerFinishedTask(JOINER, threadCount, currentThreadCount);
		
		if (currentThreadCount == threadCount) {
			super.traceNotify();
			notify();
			super.traceJoinerRelease(JOINER, threadCount, currentThreadCount);
		}		
	}
	
	public synchronized void join() {
		if (currentThreadCount < threadCount) {
			try {				
				super.traceJoinerWaitStart(JOINER, threadCount, currentThreadCount);
				super.traceWait();
				this.wait();
				super.traceJoinerWaitEnd(JOINER, threadCount, currentThreadCount);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}		
	}
	
	public String toString() {
		return super.JOINER;
	}
	
	public void resetThreadCount() {
		currentThreadCount = 0; 
	}
	
	public int getFinishedThreads() {
		return currentThreadCount;
	}
}

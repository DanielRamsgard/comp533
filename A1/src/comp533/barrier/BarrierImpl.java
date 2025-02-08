package comp533.barrier;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class BarrierImpl extends AMapReduceTracer implements Barrier {
	private int count;
	
	public BarrierImpl(int count) {
		this.count = count;
	}
	
	public synchronized void barrier() {
		while (count == 0) {
			try {
				super.traceBarrierWaitStart(BARRIER, count, count);
				this.wait();
				super.traceBarrierWaitEnd(BARRIER, count, count);
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		this.notifyAll();	
		super.traceBarrierReleaseAll(BARRIER, count, count);
	}
	
	public String toString() {
		return super.BARRIER;
	}
}

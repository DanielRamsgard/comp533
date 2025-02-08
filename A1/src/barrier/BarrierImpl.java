package barrier;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class BarrierImpl extends AMapReduceTracer implements Barrier {
	private int count;
	
	public BarrierImpl(int count) {
		this.count = count;
	}
	
	public synchronized void barrier() {
		
	}
	
	public String toString() {
		return super.BARRIER;
	}
}

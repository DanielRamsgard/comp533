package grading;

//import grader.basics.execution.BasicProjectExecution;
import gradingTools.comp533s25.assignment6.S25Assignment6Suite;
import trace.grader.basics.GraderBasicsTraceUtility;
import gradingTools.comp533.flexible.PortNumbers;
import gradingTools.comp533s20.assignment4.Assignment4Suite;

public class Main {
	public static final int TIME_OUT = 1;
	public static final int MAX_TRACES = 2000;
	public static final int MAX_PRINTED_TRACES = 600;
	
	public static void main(final String[] args) {
		PortNumbers.setTestPortRMIStart(8000);
		PortNumbers.setTestPortRMIEnd(9000);
		PortNumbers.setTestPortGIPCStart(7001);
		PortNumbers.setTestPortNIOStart(1100);
		// if you set this to false, grader steps will not be traced
		GraderBasicsTraceUtility.setTracerShowInfo(true);	
		// if you set this to false, all grader steps will be traced,
		// not just the ones that failed		
		GraderBasicsTraceUtility.setBufferTracedMessages(true);
		// Change this number if a test trace gets longer than 600 and is clipped
		GraderBasicsTraceUtility.setMaxPrintedTraces(MAX_PRINTED_TRACES);
		// Change this number if all traces together are longer than 2000
		GraderBasicsTraceUtility.setMaxTraces(MAX_TRACES);
		// Change this number if your process times out prematurely
		Assignment4Suite.setProcessTimeOut(TIME_OUT);
		// change port numbers
//		PortNumbers.setTestPortNIOStart(1100);
		// You need to always call such a method
		S25Assignment6Suite.main(args);
	}
}

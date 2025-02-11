package tests;



import grader.basics.execution.BasicProjectExecution;
import gradingTools.comp533s25.assignment2.S25Assignment2Suite;
import trace.grader.basics.GraderBasicsTraceUtility;

public class RunTestsA1 {
	public static final int TIME_OUT = 5;
	public static final int MAX_TRACES = 2000;
	public static final int MAX_PRINTED_TRACES = 600;
	
	public static void main(final String[] args) {
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
		BasicProjectExecution.setProcessTimeOut(TIME_OUT);
		// You need to always call such a method
		S25Assignment2Suite.main(args);
	}
}

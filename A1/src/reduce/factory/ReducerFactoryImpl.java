package reduce.factory;

import comp533.reducer.implementation.ReducerImpl;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;


public class ReducerFactoryImpl extends AMapReduceTracer {
	private static ReducerImpl reducerImplSingleton;
	
	public static void setReducer() {
		if (reducerImplSingleton == null) {
			reducerImplSingleton = new ReducerImpl();
			
			ReducerFactoryImpl factory = new ReducerFactoryImpl();
            factory.traceSingletonChange(ReducerFactoryImpl.class, reducerImplSingleton);			
		}				
	}
	
	public static ReducerImpl getReducer() {
		setReducer();
		
		return reducerImplSingleton;
	}
}

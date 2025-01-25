package reduce.factory;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import reducer.implementation.ReducerImpl;


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

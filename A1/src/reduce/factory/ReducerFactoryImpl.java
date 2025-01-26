package reduce.factory;

import comp533.reducer.implementation.Reducer;
import comp533.reducer.implementation.ReducerImpl;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;


public class ReducerFactoryImpl extends AMapReduceTracer {
	private static Reducer reducerImplSingleton;
	
	public static void setReducer() {
		if (reducerImplSingleton == null) {
			reducerImplSingleton = new ReducerImpl();
			
			ReducerFactoryImpl.traceSingletonChange(ReducerFactoryImpl.class, reducerImplSingleton);			
		}				
	}
	
	public static Reducer getReducer() {
		setReducer();
		
		return reducerImplSingleton;
	}
}

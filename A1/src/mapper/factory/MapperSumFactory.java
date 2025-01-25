package mapper.factory;

import comp533.assignment.mapper.SumMapperImpl;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class MapperSumFactory extends AMapReduceTracer {
	private static SumMapperImpl mapperImplSingleton;
	
	public static void setMapper() {
		if (mapperImplSingleton == null) {
			mapperImplSingleton = new SumMapperImpl();
			
			MapperSumFactory factory = new MapperSumFactory();
            factory.traceSingletonChange(MapperSumFactory.class, mapperImplSingleton);			
		}				
	}
	
	public static SumMapperImpl getMapper() {
		setMapper();
		
		return mapperImplSingleton;
	}
}

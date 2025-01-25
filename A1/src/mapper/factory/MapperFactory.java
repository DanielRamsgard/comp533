package mapper.factory;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import mapper.MapperImpl;

public class MapperFactory extends AMapReduceTracer {
	private static MapperImpl mapperImplSingleton;
	
	public static void setMapper() {
		if (mapperImplSingleton == null) {
			mapperImplSingleton = new MapperImpl();
			
			MapperFactory factory = new MapperFactory();
            factory.traceSingletonChange(MapperFactory.class, mapperImplSingleton);			
		}				
	}
	
	public static MapperImpl getMapper() {
		setMapper();
		
		return mapperImplSingleton;
	}
}

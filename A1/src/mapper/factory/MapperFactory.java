package mapper.factory;

import comp533.assignment.mapper.AssignmentMapperImpl;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class MapperFactory extends AMapReduceTracer {
	private static AssignmentMapperImpl mapperImplSingleton;
	
	public static void setMapper() {
		if (mapperImplSingleton == null) {
			mapperImplSingleton = new AssignmentMapperImpl();
			
			MapperFactory factory = new MapperFactory();
            factory.traceSingletonChange(MapperFactory.class, mapperImplSingleton);			
		}				
	}
	
	public static AssignmentMapperImpl getMapper() {
		setMapper();
		
		return mapperImplSingleton;
	}
}

package mapper.factory;

import comp533.assignment.mapper.AssignmentMapper;
import comp533.assignment.mapper.SumMapperImpl;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class MapperSumFactory extends AMapReduceTracer {
	private static AssignmentMapper mapperImplSingleton;
	
	public static void setMapper() {
		if (mapperImplSingleton == null) {
			mapperImplSingleton = new SumMapperImpl();
						
			MapperSumFactory.traceSingletonChange(MapperSumFactory.class, mapperImplSingleton);			
		}				
	}
	
	public static AssignmentMapper getMapper() {
		setMapper();
		
		return mapperImplSingleton;
	}
}

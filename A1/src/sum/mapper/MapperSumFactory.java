package sum.mapper;

import comp533.assignment.mapper.AssignmentMapper;
import comp533.assignment.mapper.SumMapperImpl;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import mapper.factory.MapperFactory;

public class MapperSumFactory extends AMapReduceTracer {
	private static AssignmentMapper mapperImplSingleton;
	
	public static void setMapperSum() {
		if (mapperImplSingleton == null) {
			mapperImplSingleton = new SumMapperImpl();
						
			MapperSumFactory.traceSingletonChange(MapperSumFactory.class, mapperImplSingleton);			
		}				
	}
	
	public static void setMapper(final AssignmentMapper newMapper) {
		mapperImplSingleton = newMapper;
	    MapperFactory.traceSingletonChange(MapperFactory.class, mapperImplSingleton);
	}
	
	public static AssignmentMapper getMapper() {
		setMapperSum();
		
		return mapperImplSingleton;
	}
}

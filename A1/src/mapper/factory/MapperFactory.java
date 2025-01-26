package mapper.factory;

import comp533.assignment.mapper.AssignmentMapper;
import comp533.assignment.mapper.AssignmentMapperImpl;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class MapperFactory extends AMapReduceTracer {
	private static AssignmentMapper mapper;
	
	public static void setMapper() {
		if (mapper == null) {
			mapper = new AssignmentMapperImpl();
			
            MapperFactory.traceSingletonChange(MapperFactory.class, mapper);			
		}				
	}
	
	public static AssignmentMapper getMapper() {
		setMapper();
		
		return mapper;
	}
}

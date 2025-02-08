package reducer;

import gradingTools.comp533s21.assignment1.interfaces.MapReduceConfiguration;
import key.value.KeyValueImpl;
import mapper.factory.MapperFactory;
import model.view.controller.*;
import reduce.factory.ReducerFactoryImpl;
import slave.Slave;
import sum.mapper.MapperSumFactory;

import java.lang.Class;

import comp533.assignment.mapper.AssignmentMapper;
import comp533.assignment.mapper.SumMapperImpl;
import comp533.partitioner.PartitionerFactory;
import comp533.partitioner.PartitionerImpl;

public class MapReduce implements MapReduceConfiguration {
	
	public Class getControllerClass() {
		return Controller.class;
	}
	
	public Object getIntSummingMapper() {
		return MapperSumFactory.getMapper();
	}
	
	public Class getIntSummingMapperClass() {
		return MapperSumFactory.getMapper().getClass();
	}
	
	public Class getKeyValueClass() {
		return KeyValueImpl.class;
	}
	
	public Class getMapperFactory() {
		return MapperFactory.class;
	}
	
	public Class getModelClass() {
		return Model.class;
	}
	
	public Class getReducer() {
		return ReducerFactoryImpl.getReducer().getClass();
	}
	
	public Class getReducerClass() {
		return ReducerFactoryImpl.getReducer().getClass();
	}
	
	public Class getReducerFactory() {
		return ReducerFactoryImpl.class;
	}
	
	public Class getStandAloneIntegerSummer() {
		return ConnectSum.class;
	}

	public Class getStandAloneTokenCounter() {		
		return Connect.class;
	}

	public Object getTokenCountingMapper() {
		return MapperFactory.getMapper();
	}

	public Class getTokenCountingMapperClass() {
		return MapperFactory.getMapper().getClass();
	}
	
	public Class getViewClass() {
		return View.class;
	}
	
	public Class getBarrier(int input) {
		return null;
	}
	
	public Class getBarrierClass() {
		return null;
	}
	
	public Class getClientTokenCounter() {
		return null;
	}
	
	public Class getJoiner(int input) {
		return null;
	}
	
	public Class getJoinerClass() {
		return null;
	}
	
	public Object getPartitioner() {
		return PartitionerFactory.getPartitioner();
	}
	
	public Class getPartitionerClass() {
		return PartitionerImpl.class;
	}
	
	public Class getPartitionerFactory() {
		return PartitionerFactory.class;
	}
	
	public Class getRemoteClientFacebookMapReduce() {
		return null;
	}
	
	public Class getRemoteClientObjectClass() {
		return null;
	}
	
	public Class getRemoteClientObjectInterface() {
		return null;
	}
	
	public Class getRemoteModelInterface() {
		return null;
	}
	
	public Class getServerIntegerSummer() {
		return null;
	}
	
	public Class getServerTokenCounter() {
		return null;
	}
	
	public Class getSlaveClass() {
		return Slave.class;
	}
	
	public Class getStandAloneFacebookMapReduce() {
		return null;
	}
	
	public Class getServerFacebookMapReduce() {
		return null;
	}

}

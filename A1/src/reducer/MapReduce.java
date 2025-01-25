package reducer;

import gradingTools.comp533s21.assignment1.interfaces.MapReduceConfiguration;
import key.value.KeyValueImpl;
import mapper.factory.MapperFactory;
import model.view.controller.*;
import standalone.token.counter.StandaloneTokenCounter;

import java.lang.Class;

public class MapReduce implements MapReduceConfiguration {
	
	public Class getControllerClass() {
		return Controller.class;
	}
	
	public Class getIntSummingMapper() {
		return null;
	}
	
	public Class getIntSummingMapperClass() {
		return null;
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
		return null;
	}
	
	public Class getReducerClass() {
		return null;
	}
	
	public Class getReducerFactory() {
		return null;
	}
	
	public Class getStandAloneIntegerSummer() {
		return null;
	}

	public Class getStandAloneTokenCounter() {
		
		return Connect.class;
	}

	public Class getTokenCountingMapper() {
		return MapperFactory.getMapper().getClass();
	}

	public Class getTokenCountingMapperClass() {
		return null;
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
	
	public Class getPartitioner() {
		return null;
	}
	
	public Class getPartitionerClass() {
		return null;
	}
	
	public Class getPartitionerFactory() {
		return null;
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
		return null;
	}
	
	public Class getStandAloneFacebookMapReduce() {
		return null;
	}
	
	public Class getServerFacebookMapReduce() {
		return null;
	}

}

package ar.edu.itba.bigdata;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import ar.edu.itba.bigdata.avgTakeOffDelay.AVGTakeOffDelayMapper;
import ar.edu.itba.bigdata.cancelledFlights.CancelledFlightsMapper;
import ar.edu.itba.bigdata.flightHoursByManufacturer.FlightHoursByManufacturerMapper;
import ar.edu.itba.bigdata.milesFlown.MilesFlownMapper;
import ar.edu.itba.bigdata.optional.flightCount.FlightCountMapper;
import ar.edu.itba.bigdata.optional.proportionalCancelledFlights.ProportionalCancelledFlightsMapper;

public class AppConfig {

	private String inPath;
	private String outPath;
	private Class<? extends Mapper<?,?,?,?>> mapper;
	private Class<? extends Reducer<?,?,?,?>> reducer;
	private Map<String, String> extras = new HashMap<String, String>();

	public String getInPath() {
		return inPath;
	}

	public void setInPath(String inPath) {
		this.inPath = inPath;
	}

	public String getOutPath() {
		return outPath;
	}

	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}

	public Class<? extends Mapper<?, ?, ?, ?>> getMapper() {
		return mapper;
	}

	public void setMapper(Class<? extends Mapper<?, ?, ?, ?>> mapper) {
		this.mapper = mapper;
	}

	public Class<? extends Reducer<?, ?, ?, ?>> getReducer() {
		return reducer;
	}

	public void setReducer(Class<? extends Reducer<?, ?, ?, ?>> reducer) {
		this.reducer = reducer;
	}

	public Map<String, String> getExtras() {
		return extras;
	}

	public Class<?> getMapOutputKeyClass() {
		return Text.class;
	}

	public Class<?> getMapOutputValueClass() {
		
		if (mapper.equals(AVGTakeOffDelayMapper.class)) {
			return IntWritable.class;
		} else if(mapper.equals(CancelledFlightsMapper.class)) {
			return IntWritable.class;
		} else if(mapper.equals(MilesFlownMapper.class)) {
			return IntWritable.class;
		} else if(mapper.equals(FlightHoursByManufacturerMapper.class)) {
			return DoubleWritable.class;
		} else if(mapper.equals(FlightCountMapper.class)) {
			return IntWritable.class;
		} else if(mapper.equals(ProportionalCancelledFlightsMapper.class)) {
			return IntWritable.class;
		}
		
		return Text.class;
	}

	public Class<?> getOutputKeyClass() {
		return Text.class;
	}

	public Class<?> getOutputValueClass() {
		
		if(mapper.equals(AVGTakeOffDelayMapper.class)) {
			return DoubleWritable.class;
		} else if(mapper.equals(CancelledFlightsMapper.class)) {
			return IntWritable.class;
		} else if(mapper.equals(MilesFlownMapper.class)) {
			return LongWritable.class;
		} else if(mapper.equals(FlightHoursByManufacturerMapper.class)) {
			return DoubleWritable.class;
		} else if(mapper.equals(FlightCountMapper.class)) {
			return IntWritable.class;
		} else if(mapper.equals(ProportionalCancelledFlightsMapper.class)) {
			return DoubleWritable.class;
		}
		
		return Text.class;
	}
	
	public void setExtra(String key, String value) {
		extras.put(key, value);
	}
}

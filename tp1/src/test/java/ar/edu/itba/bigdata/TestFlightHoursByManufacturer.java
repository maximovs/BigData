package ar.edu.itba.bigdata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.edu.itba.bigdata.cancelledFlights.CancelledFlightsMapper;
import ar.edu.itba.bigdata.cancelledFlights.CancelledFlightsReducer;
import ar.edu.itba.bigdata.flightHoursByManufacturer.FlightHoursByManufacturerMapper;
import ar.edu.itba.bigdata.flightHoursByManufacturer.FlightHoursByManufacturerReducer;




public class TestFlightHoursByManufacturer{
	
	MapDriver<LongWritable, Text, Text, DoubleWritable> mapDriver;
	ReduceDriver<Text, DoubleWritable, Text, DoubleWritable> reduceDriver;
	MapReduceDriver<LongWritable, Text, Text, DoubleWritable, Text, DoubleWritable> mapReduceDriver;
	
	@Before
	public void init() throws Exception{
		FlightHoursByManufacturerMapper mapper = new FlightHoursByManufacturerMapper();
		FlightHoursByManufacturerReducer reducer = new FlightHoursByManufacturerReducer();
		mapDriver = MapDriver.newMapDriver(mapper);
		reduceDriver = ReduceDriver.newReduceDriver(reducer);
		mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);
		Configuration conf = new Configuration();
		mapDriver.setConfiguration(conf);
		File file = new File("src/test/resources/carriers.csv");
		DistributedCache.addArchiveToClassPath(
				new Path(file.getAbsolutePath()), conf, FileSystem.get(conf));
	}
	
//	@Test
//	public void TestMapper() {
//		mapDriver.withInput(new LongWritable(), new Text("1987,10,6,2,1251,1250,1343,1340,PS,1509,NA,52,50,NA,3,1,LAS,SNA,226,NA,NA,0,NA,0,NA,NA,NA,NA,NA"));
//	    mapDriver.withOutput(new Text("Continental Air Lines Inc."), new DoubleWritable(1));
//	    mapDriver.runTest();
//	}
	
	@Test
	public void TestReducer(){
		List<DoubleWritable> values = new ArrayList<DoubleWritable>();
		values.add(new DoubleWritable(20.4));
		values.add(new DoubleWritable(12.3));
		values.add(new DoubleWritable(37.5));
		values.add(new DoubleWritable(4.3));
		reduceDriver.withInput(new Text("Continental Air Lines Inc.-1987"), values);
		reduceDriver.withOutput(new Text("Continental Air Lines Inc.-1987"), new DoubleWritable(74.5));
		reduceDriver.runTest();
	}
	
}

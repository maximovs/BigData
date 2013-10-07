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

import ar.edu.itba.bigdata.avgTakeOffDelay.AVGTakeOffDelayMapper;
import ar.edu.itba.bigdata.avgTakeOffDelay.AVGTakeOffDelayReducer;
import ar.edu.itba.bigdata.cancelledFlights.CancelledFlightsMapper;
import ar.edu.itba.bigdata.cancelledFlights.CancelledFlightsReducer;




public class TestCancelledFlights{
	
	MapDriver<LongWritable, Text, Text, IntWritable> mapDriver;
	ReduceDriver<Text, IntWritable, Text, IntWritable> reduceDriver;
	MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable> mapReduceDriver;

	
	@Before
	public void init() throws Exception{
		CancelledFlightsMapper mapper = new CancelledFlightsMapper();
		CancelledFlightsReducer reducer = new CancelledFlightsReducer();
		mapDriver = MapDriver.newMapDriver(mapper);
		reduceDriver = ReduceDriver.newReduceDriver(reducer);
		mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);
		Configuration conf = new Configuration();
		mapDriver.setConfiguration(conf);
		File file = new File("src/test/resources/carriers.csv");
		DistributedCache.addArchiveToClassPath(
				new Path(file.getAbsolutePath()), conf, FileSystem.get(conf));
	}
	
	@Test
	public void TestMapper() {
		mapDriver.withInput(new LongWritable(), new Text("1987,12,28,1,NA,1510,NA,1810,CO,636,NA,NA,120,NA,NA,NA,ORD,EWR,719,NA,NA,1,NA,0,NA,NA,NA,NA,NA"));
	    mapDriver.withOutput(new Text("Continental Air Lines Inc."), new IntWritable(1));
	    mapDriver.runTest();
	    
	}
	
	
	@Test
	public void TestReducer(){
		List<IntWritable> values = new ArrayList<IntWritable>();
		values.add(new IntWritable(1));
		values.add(new IntWritable(1));
		values.add(new IntWritable(0));
		values.add(new IntWritable(0));
		values.add(new IntWritable(1));
		reduceDriver.withInput(new Text("Continental Air Lines Inc.-1987"), values);
		reduceDriver.withOutput(new Text("Continental Air Lines Inc.-1987"), new IntWritable(3));
		reduceDriver.runTest();
	}
	
}

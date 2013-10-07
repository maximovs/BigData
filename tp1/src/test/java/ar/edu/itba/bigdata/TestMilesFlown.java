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
import ar.edu.itba.bigdata.milesFlown.MilesFlownMapper;
import ar.edu.itba.bigdata.milesFlown.MilesFlownReducer;


public class TestMilesFlown{
	
	MapDriver<LongWritable, Text, Text, IntWritable> mapDriver;
	ReduceDriver<Text, IntWritable, Text, LongWritable> reduceDriver;
	MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, LongWritable> mapReduceDriver;
	
	@Before
	public void init() throws Exception{
		MilesFlownMapper mapper = new MilesFlownMapper();
		MilesFlownReducer reducer = new MilesFlownReducer();
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
			mapDriver.withInput(new LongWritable(), new Text("1987,10,5,1,1846,1846,2011,2018,TW,493,NA,85,92,NA,-7,0,STL,OKC,462,NA,NA,0,NA,0,NA,NA,NA,NA,NA"));
		    mapDriver.withOutput(new Text("Trans World Airways LLC-1987"), new IntWritable(462));
		    mapDriver.runTest();
	}
	
	@Test
	public void TestReducer(){
		List<IntWritable> values = new ArrayList<IntWritable>();
		values.add(new IntWritable(102));
		values.add(new IntWritable(462));
		values.add(new IntWritable(313));
		values.add(new IntWritable(258));
		reduceDriver.withInput(new Text("Trans World Airways LLC-1987"), values);
		reduceDriver.withOutput(new Text("Trans World Airways LLC-1987"), new LongWritable(1135));
		reduceDriver.runTest();
	}
	
}

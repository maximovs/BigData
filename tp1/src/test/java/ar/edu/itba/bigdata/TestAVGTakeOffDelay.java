package ar.edu.itba.bigdata;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import ar.edu.itba.bigdata.avgTakeOffDelay.AVGTakeOffDelayMapper;
import ar.edu.itba.bigdata.avgTakeOffDelay.AVGTakeOffDelayReducer;


public class TestAVGTakeOffDelay{
	
	MapDriver<LongWritable, Text, Text, IntWritable> mapDriver;
	ReduceDriver<Text, IntWritable, Text, DoubleWritable> reduceDriver;
	MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, DoubleWritable> mapReduceDriver;
	
	@Before
	public void init() throws Exception{
		AVGTakeOffDelayMapper mapper = new AVGTakeOffDelayMapper();
		AVGTakeOffDelayReducer reducer = new AVGTakeOffDelayReducer();
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
//			mapDriver.withInput(new LongWritable(), new Text("1989,1,12,4,2026,2035,2108,2109,UA,184,NA,42,34,NA,-1,-9,LIH,HNL,102,NA,NA,0,NA,0,NA,NA,NA,NA,NA"));
//		    mapDriver.withOutput(new Text("HI"), new IntWritable(-9));
//		    mapDriver.runTest();
//	}
	
	@Test
	public void TestReducer(){
		List<IntWritable> values = new ArrayList<IntWritable>();
		values.add(new IntWritable(17));
		values.add(new IntWritable(0));
		values.add(new IntWritable(-3));
		values.add(new IntWritable(-14));
		values.add(new IntWritable(5));
		reduceDriver.withInput(new Text("FL-June"), values);
		reduceDriver.withOutput(new Text("FL-June"), new DoubleWritable(1));
		reduceDriver.runTest();
	}
	
}

package ar.edu.itba.bigdata;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
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
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.edu.itba.bigdata.flightHoursByManufacturer.FlightHoursByManufacturerMapper;
import ar.edu.itba.bigdata.flightHoursByManufacturer.FlightHoursByManufacturerReducer;




public class TestFlightHoursByManufacturer{
	
	@Before
	public void init() throws Exception{
		Job job = new Job(); 
		job.setJarByClass(TestFlightHoursByManufacturer.class);

		FileInputFormat.addInputPath(job, new Path("/user/hadoop/ITBA/TP1/INPUT/SAMPLE/JUNITTEST"));
		FileOutputFormat.setOutputPath(job, new Path("/user/hadoop/ITBA/TP1/INPUT/SAMPLE/JUNITTEST"));

		job.setMapperClass(FlightHoursByManufacturerMapper.class);
		job.setReducerClass(FlightHoursByManufacturerReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		while(job.waitForCompletion(true));
	}
	
	@Test
	public void TestSetup() {
		
	}
	
}

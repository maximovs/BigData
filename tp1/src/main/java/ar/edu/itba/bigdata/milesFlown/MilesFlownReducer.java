package ar.edu.itba.bigdata.milesFlown;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MilesFlownReducer extends Reducer<Text, IntWritable, Text, LongWritable> {

	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

		long counter = 0;
		for (IntWritable value : values) {
			counter += value.get();
		}
		context.write(key, new LongWritable(counter));
	}

}

package ar.edu.itba.bigdata.optional.flightCount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FlightCountReducer extends Reducer<Text, IntWritable, Text, IntWritable>  {


	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

		int counter = 0;
		for (IntWritable value : values) {
			counter += value.get(); // could simply be counter++;
		}
		context.write(key, new IntWritable(counter));
	}
}
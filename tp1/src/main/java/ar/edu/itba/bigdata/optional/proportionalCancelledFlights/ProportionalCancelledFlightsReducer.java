package ar.edu.itba.bigdata.optional.proportionalCancelledFlights;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ProportionalCancelledFlightsReducer extends Reducer<Text, IntWritable, Text, DoubleWritable>  {


	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

		int counter = 0;
		int total = 0;
		for (IntWritable value : values) {
			counter += value.get();
			total++;
		}
		context.write(key, new DoubleWritable(counter / (double)total));
	}
}

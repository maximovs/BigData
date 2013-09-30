package ar.edu.itba.bigdata.avgTakeOffDelay;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class AVGTakeOffDelayReducer extends Reducer<Text, IntWritable, Text, DoubleWritable> {
	
	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

		int sumOfDelays = 0;
		int n = 0;

		for (IntWritable value : values) {
			sumOfDelays += value.get();
			n++;
		}

		if(n == 0 ) {
			context.write(key, new DoubleWritable(0));
		} else {
			context.write(key, new DoubleWritable(sumOfDelays / (double)n));
		}
	}
}

package ar.edu.itba.bigdata.flightHoursByManufacturer;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FlightHoursByManufacturerReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable>  {
	
	public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {

		double sumOfFlightHours = 0;

		for (DoubleWritable value : values) {
			sumOfFlightHours += value.get();
		}
		
		context.write(key, new DoubleWritable(sumOfFlightHours));
	}
}

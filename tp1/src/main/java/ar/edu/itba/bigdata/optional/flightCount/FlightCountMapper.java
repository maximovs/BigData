package ar.edu.itba.bigdata.optional.flightCount;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class FlightCountMapper extends Mapper<LongWritable, Text, Text, IntWritable>  {

	private static final int CARRIER_CODE_INDEX = 8;
	private HashMap<String, String> carriers = new HashMap<String, String>();
	private Logger logger = Logger.getLogger(this.getClass().getSimpleName() + " Mapper");

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String[] flightInfo = value.toString().split(",");
		String carrierCode = flightInfo[CARRIER_CODE_INDEX].replace("\"", "");
		String carrierName = carriers.get(carrierCode);
		
		if (carrierName != null) {
			context.write(new Text(carrierName), new IntWritable(1));
		} else {
			logger.log(Level.WARN, carrierCode + "wasn't found in input file carriers.csv.");
		}
	}

	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		
		BufferedReader reader = getBufferedReader(context);

		String line;
		while ((line = reader.readLine()) != null) {
			String carrierInfo[] = line.split(",", 2);
			String code = carrierInfo[0].replace("\"", "");;
			String name = carrierInfo[1].replace("\"", "");
			carriers.put(code, name);
		}
	}
	
	private BufferedReader getBufferedReader(Context context) {

		Path[] paths = DistributedCache.getArchiveClassPaths(context.getConfiguration());
		FileSystem fs = null;
		FSDataInputStream inputStream = null;
		
		try {
			fs = FileSystem.get(context.getConfiguration());
			inputStream = fs.open(paths[0]);
		} catch (IOException e) {
			logger.log(Level.ERROR, "Error trying to open input File. Caused by:" + paths[0]);
			e.printStackTrace();
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		return reader;
	}
	
}


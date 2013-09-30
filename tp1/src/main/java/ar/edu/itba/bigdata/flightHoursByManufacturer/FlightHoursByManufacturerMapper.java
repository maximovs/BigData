package ar.edu.itba.bigdata.flightHoursByManufacturer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class FlightHoursByManufacturerMapper extends Mapper<LongWritable, Text, Text, DoubleWritable>  {

	private static final String HBASE_CONFIGURATION_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";
	private static final String HBASE_CONFIGURATION_ZOOKEEPER_CLIENTPORT = "hbase.zookeeper.property.clientPort";
	private static final int TAIL_NUMBER_INDEX = 10;
	private static final int FLIGH_TIME_INDEX = 13;
	private static final byte[] GENERAL_FAMILY = "general".getBytes();
	private static final byte[] MANUFACTURER_QUALIFIER = "manufacturer".getBytes();
	private HashMap<String, String> planes = new HashMap<String, String>();
	private Logger logger = Logger.getLogger(this.getClass().getSimpleName() + " Mapper");
	private String targetManufacturer;
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String[] flightInfo = value.toString().split(",");
		String tailNumber = flightInfo[TAIL_NUMBER_INDEX];
		String manufacturer = planes.get(tailNumber);
		
		if (manufacturer != null) {
			
			try {
				Double flightHours = Double.parseDouble(flightInfo[FLIGH_TIME_INDEX]) / 60;
				context.write(new Text(tailNumber), new DoubleWritable(flightHours));
			} catch (NumberFormatException e) {
				
				if (value.toString().equals("NA")) {
					// NA == NULL, can happen	
				} else {
					logger.log(Level.ERROR, "NumberFormatException caused by" + flightInfo[FLIGH_TIME_INDEX] + "(not NA).");
				}
			}			
		}
	}
	
	@Override
	protected void setup(Context context) throws IOException, InterruptedException {

		targetManufacturer = context.getConfiguration().get("manufacturer");

		Configuration conf = HBaseConfiguration.create();
		conf.set(HBASE_CONFIGURATION_ZOOKEEPER_QUORUM, "hadoop-2013-datanode-1");
		conf.set(HBASE_CONFIGURATION_ZOOKEEPER_CLIENTPORT, "2181");
		HTable table = new HTable(conf, "itba_tp1_planes");
		
		Scan scan = new Scan();
		scan.setCaching(500);  
		scan.setCacheBlocks(false);
		scan.addFamily(GENERAL_FAMILY);

		ResultScanner scanner = table.getScanner(scan);
		Iterator<Result> iterator = scanner.iterator();

		while(iterator.hasNext()) {

			Result result = iterator.next();
			List<KeyValue> list = result.getColumn(GENERAL_FAMILY, MANUFACTURER_QUALIFIER);

			for(KeyValue keyvalue: list) {

				String key = Bytes.toStringBinary(keyvalue.getKey(), 2, keyvalue.getRowLength());
				String manufacturer = new String(keyvalue.getValue());
				if(manufacturer.toLowerCase().equals(targetManufacturer.toLowerCase())) {
					planes.put(key, manufacturer);
				}
			}
		}
	}
	
}

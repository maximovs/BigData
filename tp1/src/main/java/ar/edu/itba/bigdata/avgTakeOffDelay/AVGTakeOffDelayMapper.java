package ar.edu.itba.bigdata.avgTakeOffDelay;

import java.io.IOException;
import java.text.DateFormatSymbols;
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
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class AVGTakeOffDelayMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	private static final String HBASE_CONFIGURATION_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";
	private static final String HBASE_CONFIGURATION_ZOOKEEPER_CLIENTPORT = "hbase.zookeeper.property.clientPort";
	private static final int ORIGIN_IATA_INDEX = 16;
	private static final int DEP_DELAY_INDEX = 15;
	private static final int MONTH_INDEX = 1;
	private static final byte[] INFO_FAMILY = "info".getBytes();
	private static final byte[] STATE_QUALIFIER = "state".getBytes();
	private HashMap<String, String> aiports = new HashMap<String, String>();
	private Logger logger = Logger.getLogger(this.getClass().getSimpleName() + " Mapper");

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String[] flightInfo = value.toString().split(",");
		String originIATACode = flightInfo[ORIGIN_IATA_INDEX];
		String state = aiports.get(originIATACode);
		
		if (state != null) {

			String month = getMonth(flightInfo[MONTH_INDEX]);
			Integer delay = 0;
			try {
				delay = Integer.parseInt(flightInfo[DEP_DELAY_INDEX]);
			} catch(NumberFormatException e) {
				
				if (value.toString().equals("NA")) {
					// NA == NULL, can happen	
				} else {
					logger.log(Level.ERROR, "NumberFormatException caused by" + value.toString() + "(not NA).");
				}
			}

			if (delay > 0) {
				context.write(new Text(state + "-" + month), new IntWritable(delay));
			}
		} else {
			logger.log(Level.WARN, originIATACode + "not found in table itba_tp1_airports!");
		}
	}

	private String getMonth(String monthNumber) {
		Integer month = Integer.parseInt(monthNumber);
		return new DateFormatSymbols().getMonths()[month - 1];
	}

	@Override
	protected void setup(Context context) throws IOException, InterruptedException {

		Configuration conf = HBaseConfiguration.create();
		conf.set(HBASE_CONFIGURATION_ZOOKEEPER_QUORUM, "hadoop-2013-datanode-1");
		conf.set(HBASE_CONFIGURATION_ZOOKEEPER_CLIENTPORT, "2181");
		HTable table = new HTable(conf, "itba_tp1_airports");
		
		Scan scan = new Scan();
		scan.setCaching(500);  
		scan.setCacheBlocks(false);
		scan.addFamily(INFO_FAMILY);

		ResultScanner scanner = table.getScanner(scan);
		Iterator<Result> resultIterator = scanner.iterator();

		while(resultIterator.hasNext()) {
			
			Result result = resultIterator.next();
			List<KeyValue> list = result.getColumn(INFO_FAMILY, STATE_QUALIFIER);
			for(KeyValue keyvalue: list) {
				String key = Bytes.toStringBinary(keyvalue.getKey(), 2, keyvalue.getRowLength());
				String value = new String(keyvalue.getValue());
				aiports.put(key, value);
				logger.log(Level.DEBUG, "Inserting: " + key + "," + value);
			}
		}
	}
}

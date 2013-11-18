package com.itba.g2.storm.bolt;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Level;

public class HBaseGroupingBolt extends GroupingBolt{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2815093390055705758L;
	private static final String HBASE_CONFIGURATION_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";
	private static final String HBASE_CONFIGURATION_ZOOKEEPER_CLIENTPORT = "hbase.zookeeper.property.clientPort";
	private byte[] KEYWORD_FAMILY = "keyword".getBytes();
	Map<String,List<String>> values;
	
	public HBaseGroupingBolt(String tableName, String cf) throws IOException {
		super();
		values = new HashMap<String, List<String>>();
		if(cf!=null){
			KEYWORD_FAMILY = cf.getBytes();
		}
		Configuration conf = HBaseConfiguration.create();
		conf.set(HBASE_CONFIGURATION_ZOOKEEPER_QUORUM, "hadoop-2013-datanode-1");
		conf.set(HBASE_CONFIGURATION_ZOOKEEPER_CLIENTPORT, "2181");
		HTable table = new HTable(conf, tableName);
		
		Scan scan = new Scan();
		scan.setCaching(500);  
		scan.setCacheBlocks(false);
		scan.addFamily(KEYWORD_FAMILY);

		ResultScanner resultScanner = table.getScanner(scan);

		 for (Result result : resultScanner) {
			 LinkedList<String> l = new LinkedList<String>();
			 for (KeyValue kv : result.raw()) {
			   l.add(Bytes.toString(kv.getValue()));
			  }
			 values.put(Bytes.toString(result.getRow()), l);
			 }
		 for (String key : values.keySet()) {
			String s = key + ": ";
			for(String val:values.get(key)){
				s+= val + " ";
			}
			System.out.println(s);
		}
		 resultScanner.close();
		 table.close();
	}
	
	
	@Override
	public Map<String, List<String>> getGroups() {
		return values;
	}

}

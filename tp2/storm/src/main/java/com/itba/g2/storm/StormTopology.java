package com.itba.g2.storm;

import javax.jms.Session;

import org.apache.tools.ant.types.CommandlineJava.SysProperties;

import com.itba.g2.storm.bolt.CounterBolt;
import com.itba.g2.storm.bolt.HBaseGroupingBolt;
import com.itba.g2.storm.bolt.MockGroupingBolt;
import com.itba.g2.storm.bolt.RDBMSDumperBolt;
import com.itba.g2.storm.bolt.SystemOutBolt;
import com.itba.g2.storm.bolt.SystemOutDumperBolt;
import com.itba.g2.storm.jms.ActiveMQJmsProvider;
import com.itba.g2.storm.jms.JsonJmsTupleProducer;
import com.itba.g2.storm.spout.ClickGeneratorSpout;
import com.itba.g2.storm.spout.JmsSpout;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

/**
 * This is a basic example of a Storm topology.
 */
public class StormTopology {

	public static void main(String[] args) throws Exception {
		String dbUrl = "jdbc:mysql://localhost/itba_barry";
		String table = "GroupsCount";
		String mainField = "GroupId";
		String countField = "Ammount";
		String user = "barry";
		String pass = "1234";
		String cf = "keyword";
		if(args!=null){
			if(args.length>1){
				dbUrl = args[1];
				if(args.length>2){
					table = args[2];
					if(args.length>3){
						mainField = args[3];
						if(args.length>4){
							countField = args[4];
							if(args.length>5){
								user = args[5];
								if(args.length>6){
									pass = args[6];
									if(args.length>7){
										cf = args[7];
									}
								}
							}
						}
					}
				}
			}else{
				System.out.println("Running with default values. To change them use: topologyName dbUrl table mainField countField user pass columnFamily");
			}
		}
		TopologyBuilder builder = new TopologyBuilder();
		JmsSpout sp = new JmsSpout();
		sp.setJmsAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
		sp.setJmsProvider(new ActiveMQJmsProvider());
		sp.setJmsTupleProducer(new JsonJmsTupleProducer());
		RDBMSDumperBolt dumperBolt = new RDBMSDumperBolt(table, dbUrl, user, pass, mainField, countField);
		builder.setSpout("tweet", sp, 5);        
		builder.setBolt("counter", new HBaseGroupingBolt(cf), 10).shuffleGrouping("tweet");
		builder.setBolt("persister", dumperBolt, 3).fieldsGrouping("counter", new Fields("groupId"));

		Config conf = new Config();
		conf.setDebug(true);

		if(args!=null && args.length > 0) {
			conf.setNumWorkers(3);

			StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
		} else {

			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("test", conf, builder.createTopology());
			Utils.sleep(100000);
			cluster.killTopology("test");
			cluster.shutdown();    
		}
	}
}
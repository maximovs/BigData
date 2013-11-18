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
		String mqAddress = "tcp://hadoop-2013-datanode-7:61616";// http://hadoop-2013-datanode-2:8161/admin/queues.jsp
		String dbUrl = "jdbc:mysql://hadoop-2013-datanode-6/itba_barry";
		String table = "GroupsCount";
		String mainField = "GroupId";
		String countField = "Ammount";
		String user = "barry";
		String pass = "1234";
		String hBaseTable = "itba_tp2_twitter_words";
		String cf = "keyword";
		if(args!=null){
			if(args.length>1){
				mqAddress = args[1];
				if(args.length>2){
					dbUrl = args[2];
					if(args.length>3){
						table = args[3];
						if(args.length>4){
							mainField = args[4];
							if(args.length>5){
								countField = args[5];
								if(args.length>6){
									user = args[6];
									if(args.length>7){
										pass = args[7];
										if(args.length>8){
											hBaseTable = args[8];
											if(args.length>9){
												cf = args[9];
											}
										}
									}
								}
							}
						}
					}
				}
			}else{
				System.out.println("Running with default values. To change them use: topologyName activeMQaddress dbUrl sqltable mainField countField user pass hbaseTable columnFamily");
			}
		}
		TopologyBuilder builder = new TopologyBuilder();
		JmsSpout sp = new JmsSpout();
		sp.setJmsAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
		sp.setJmsProvider(new ActiveMQJmsProvider(mqAddress));
		sp.setJmsTupleProducer(new JsonJmsTupleProducer());
		RDBMSDumperBolt dumperBolt = new RDBMSDumperBolt(table, dbUrl, user, pass, mainField, countField);
		builder.setSpout("tweet", sp, 5);        
		builder.setBolt("counter", new HBaseGroupingBolt(hBaseTable, cf), 10).shuffleGrouping("tweet");
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
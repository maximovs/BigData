package com.itba.g2.storm;

import javax.jms.Session;

import com.itba.g2.storm.bolt.CounterBolt;
import com.itba.g2.storm.bolt.MockGroupingBolt;
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
        TopologyBuilder builder = new TopologyBuilder();
        JmsSpout sp = new JmsSpout();
        sp.setJmsAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
        sp.setJmsProvider(new ActiveMQJmsProvider());
        sp.setJmsTupleProducer(new JsonJmsTupleProducer());
        
        builder.setSpout("tweet", sp, 10);        
        builder.setBolt("counter", new MockGroupingBolt(), 3).shuffleGrouping("tweet");
        builder.setBolt("persister", new SystemOutDumperBolt(), 1).fieldsGrouping("counter", new Fields("groupId"));
                
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
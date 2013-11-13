package com.itba.g2.storm.bolt;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.itba.g2.storm.spout.ClickGeneratorSpout;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class SystemOutDumperBolt extends BaseRichBolt {

		private static final long serialVersionUID = 1L;
        OutputCollector _collector;
    	public static Logger LOG = Logger.getLogger(ClickGeneratorSpout.class);
    	private ConcurrentHashMap<String,AtomicInteger> groups = new ConcurrentHashMap<String, AtomicInteger>();

		@Override
        public void execute(Tuple tuple){
			String gid = (String) tuple.getValue(0);
			if(!groups.containsKey(gid)){
				groups.put(gid, new AtomicInteger());
			}
			int times = groups.get(gid).addAndGet(tuple.getInteger(1));
			
            System.out.println(gid + " " + times);
            _collector.ack(tuple);
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer ofd) {
        }

		@Override
		public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
			 _collector = collector;
		}
    }
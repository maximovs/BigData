package com.itba.g2.storm.bolt;

import java.util.Map;

import org.apache.log4j.Logger;

import com.itba.g2.storm.spout.ClickGeneratorSpout;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class SystemOutBolt extends BaseRichBolt {

		private static final long serialVersionUID = 1L;
        OutputCollector _collector;
    	public static Logger LOG = Logger.getLogger(ClickGeneratorSpout.class);

		@Override
        public void execute(Tuple tuple){
            System.out.println(tuple.getValue(0)+"  "+tuple.getValue(1));
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
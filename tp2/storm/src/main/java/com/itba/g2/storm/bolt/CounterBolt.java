package com.itba.g2.storm.bolt;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class CounterBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	OutputCollector _collector;

	Map<String, Integer> counts = new HashMap<String, Integer>();
	
    @SuppressWarnings("rawtypes")
	@Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        _collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
    	String url = tuple.getString(0);
    	url = url.substring(0, url.indexOf('?'));
        Integer count = counts.get(url);
        if(count==null){
        	count = 0;
        }
        count++;
        
        
        counts.put(url, count);
        _collector.emit(tuple, new Values(url, count));
        _collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word", "count"));
    }
}
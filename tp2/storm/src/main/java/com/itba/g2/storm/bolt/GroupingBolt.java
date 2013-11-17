package com.itba.g2.storm.bolt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public abstract class GroupingBolt extends BaseRichBolt {
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
    	String tweet = tuple.getString(0);
    	Map<String,List<String>> groups = getGroups();
    	for(String key: groups.keySet()){
    		int times = 0;
    		for(String value: groups.get(key)){
    			if(tweet.contains(value)){
    				times++;
    			}
    		}
    		if(times>0){
    			_collector.emit(tuple, new Values(key, times));
    		}
    	}

        _collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("groupId", "count"));
    }
    
    public abstract Map<String,List<String>> getGroups();
}
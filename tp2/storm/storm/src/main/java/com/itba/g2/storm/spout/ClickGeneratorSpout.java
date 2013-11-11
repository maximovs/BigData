package com.itba.g2.storm.spout;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class ClickGeneratorSpout extends BaseRichSpout {
	private static final long serialVersionUID = 1L;
	public static Logger LOG = Logger.getLogger(ClickGeneratorSpout.class);
    boolean _isDistributed;
    SpoutOutputCollector _collector;

    public ClickGeneratorSpout() {
        this(true);
    }

    public ClickGeneratorSpout(boolean isDistributed) {
        _isDistributed = isDistributed;
    }
        
    @SuppressWarnings("rawtypes")
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
    }
    
    public void close() {
        
    }
        
    public void nextTuple() {
        Utils.sleep(100);
        final String[] words = new String[] {"www.youtube.com", "www.google.com", "www.yahoo.com", "www.twitter.com", "www.facebook.com"};
        final Random rand = new Random();
        final String word = words[rand.nextInt(words.length)] + "?q="+ rand.nextLong();
        _collector.emit(new Values(word));
    }
    
    public void ack(Object msgId) {

    }

    public void fail(Object msgId) {
        
    }
    
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        if(!_isDistributed) {
            Map<String, Object> ret = new HashMap<String, Object>();
            ret.put(Config.TOPOLOGY_MAX_TASK_PARALLELISM, 1);
            return ret;
        } else {
            return null;
        }
    }    
}
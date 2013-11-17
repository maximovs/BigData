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

public class SystemOutDumperBolt extends DumperBolt {

	@Override
	boolean store(String gid, int times) {
		System.out.println(gid + " " + times);
		return true;
	}
   }
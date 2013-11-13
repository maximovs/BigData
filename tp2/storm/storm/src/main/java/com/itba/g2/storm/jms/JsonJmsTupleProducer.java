package com.itba.g2.storm.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class JsonJmsTupleProducer implements JmsTupleProducer {
	private static final long serialVersionUID = 1L;

	@Override
	public Values toTuple(Message msg) throws JMSException {
		if (msg instanceof TextMessage) {
			String json = ((TextMessage) msg).getText();
			return parseJson(json);
		} else {
			return null;
		}
	}


	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet"));
	}

	private Values parseJson(String json) {
		
		Values values = null;
		
		try {
			JSONObject obj = new JSONObject(json);
			String aux  = obj.getString("text");
			if(!aux.isEmpty()){
				values = new Values(aux);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return values;
	}
}
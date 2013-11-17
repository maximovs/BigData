package com.itba.g2.storm.bolt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MockGroupingBolt extends GroupingBolt{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2815093390055705758L;
	Map<String,List<String>> values;
	
	public MockGroupingBolt() {
		super();
		values = new HashMap<String, List<String>>();
		LinkedList<String> unen = new LinkedList<String>();
		unen.add("unen");
		unen.add("carrio");
		unen.add("pino");
		unen.add("solanas");
		LinkedList<String> pro = new LinkedList<String>();
		pro.add("pro");
		pro.add("macri");
		pro.add("michetti");
		values.put("unen", unen);
		values.put("pro", pro);
	}
	
	
	@Override
	public Map<String, List<String>> getGroups() {
		return values;
	}

}

package com.itba.g2.storm.bolt;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.itba.g2.storm.rDBMS.RDBMSCommunicator;
import com.itba.g2.storm.rDBMS.RDBMSConnector;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
/*
 * Bolt for dumping stream data into RDBMS
 */
public class RDBMSDumperBolt  extends DumperBolt {
    private static final long serialVersionUID = 1L;
    private static transient RDBMSCommunicator communicator = null;
    private transient RDBMSConnector connector = new RDBMSConnector();
    private transient Connection con = null;
    private String tableName = null;
    private String mainField = null;
    private String countField = null;
    
    private ArrayList<Object> fieldValues = new ArrayList<Object>();

    private List<String> list = null;
    public RDBMSDumperBolt(String tableName, String dBUrl, String username, String password, String mainField, String countField)           throws SQLException {
        super();
        this.tableName = tableName;
        this.mainField = mainField;
        this.countField = countField;
        try {
            con = connector.getConnection(dBUrl, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        communicator = new RDBMSCommunicator(con);
    }
   
	@Override
	boolean store(String gid, int times) {
		return communicator.addToRow(tableName, mainField, countField, gid, times);
	}


}
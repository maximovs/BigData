package com.itba.g2.storm.bolt;
import java.io.Serializable;
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
public class RDBMSDumperBolt  extends DumperBolt implements Serializable{
    private static final long serialVersionUID = 1L;
    private RDBMSConnector connector = new RDBMSConnector();
//    private Connection con = null;
    private String tableName = null;
    private String mainField = null;
    private String countField = null;
    private String dBUrl = null;
    private String username = null;
    private String password = null;
    
    private ArrayList<Object> fieldValues = new ArrayList<Object>();

    private List<String> list = null;
    public RDBMSDumperBolt(String tableName, String dBUrl, String username, String password, String mainField, String countField)           throws SQLException {
        super();
        this.tableName = tableName;
        this.mainField = mainField;
        this.countField = countField;
       this.dBUrl = dBUrl;
       this.username = username;
       this.password = password;
    }
   
	@Override
	boolean store(String gid, int times) {
		 Connection con = null;
	        try {
	        	con = connector.getConnection(dBUrl, username, password);
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        RDBMSCommunicator communicator = new RDBMSCommunicator(con);
		return communicator.addToRow(tableName, mainField, countField, gid, times);
	}


}
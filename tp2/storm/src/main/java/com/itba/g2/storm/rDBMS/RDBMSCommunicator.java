package com.itba.g2.storm.rDBMS;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * This class implements method for inserting data into RDBMS tables
 */
public class RDBMSCommunicator implements Serializable{
    private Connection con = null;
    private PreparedStatement prepstmt = null;
    private String queryStmt = null, queryValues = "";
    private int noOfColumns = 0, result = 0;
    private ResultSet rs = null;
    Map<String, String> tableDetails;
    public RDBMSCommunicator(Connection con) {
        super();
        this.con = con;
    }
    public int insertRow(String tableName, List<String> fieldNames, ArrayList<Object> fieldValues) throws SQLException {
        result = 0;
        try {       
            prepstmt = null;
            queryValues = "";
            noOfColumns = fieldNames.size();           
            queryStmt = "insert into " + tableName + " (";
            for (int i = 0; i <= noOfColumns - 1; i++) {
                if (i != noOfColumns - 1) {
                    queryStmt = queryStmt + fieldNames.get(i) + ", ";
                    queryValues = queryValues + "?,";
                } else {
                    queryStmt = queryStmt + fieldNames.get(i) + ") ";
                    queryValues = queryValues + "?";
                }
            }
            queryStmt = queryStmt + " values (" +  queryValues + ")";
            prepstmt = con.prepareStatement(queryStmt);
            for (int j = 0; j <= noOfColumns - 1; j++) {
                prepstmt.setObject(j + 1, fieldValues.get(j));
            }
            result = prepstmt.executeUpdate();
            if (result != 0) {
                System.out.println("Inserted data successfully ..");
            } else {
                System.out.println("Insertion failed ..");   
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public boolean updateRow(String table, String mainField, String countFielf,String gid, int ammount) {
    	int result = 0;
    	String sql = "INSERT INTO "+ table +" ("+mainField+","+countFielf+") VALUES (?,?) ON DUPLICATE KEY UPDATE "+countFielf+"=?";

    	PreparedStatement prepstmt;
		try {
			prepstmt = con.prepareStatement(sql);
//			prepstmt.setString(1, gid);
//	    	prepstmt.setInt(2, ammount);
//	    	prepstmt.setString(3, gid);
//	    	prepstmt.setString(4, gid);
//	    	prepstmt.setInt(5, ammount);
			prepstmt.setString(1, gid);
	    	prepstmt.setInt(2, ammount);
	    	prepstmt.setInt(3, ammount);
	    	result = prepstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result!=0;
    	
    }
    public Map<String, String> getTableInformation(String tableName) {
        tableDetails = new HashMap<String, String>();
        try {
        String stmt = "select column_name, data_type, character_maximum_length from information_schema.columns where table_name = '" + tableName + "'";
        System.out.println(stmt);
        PreparedStatement prepstmt = null;
        prepstmt = con.prepareStatement(stmt);
        rs = prepstmt.executeQuery();
        while(rs.next()) {
            tableDetails.put(rs.getString("column_name"), rs.getString("data_type"));
        }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return tableDetails;
    }
	public boolean addToRow(String tableName, String mainField,
			String countField, String gid, int times) {
		int result = 0;
    	String sql = "INSERT INTO "+ tableName +" ("+mainField+","+countField+") VALUES (?,?) ON DUPLICATE KEY UPDATE "+countField+"="+ countField +" + ?";

    	PreparedStatement prepstmt;
		try {
			prepstmt = con.prepareStatement(sql);
//			prepstmt.setString(1, gid);
//	    	prepstmt.setInt(2, ammount);
//	    	prepstmt.setString(3, gid);
//	    	prepstmt.setString(4, gid);
//	    	prepstmt.setInt(5, ammount);
			prepstmt.setString(1, gid);
	    	prepstmt.setInt(2, times);
	    	prepstmt.setInt(3, times);
	    	result = prepstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result!=0;
	}
}
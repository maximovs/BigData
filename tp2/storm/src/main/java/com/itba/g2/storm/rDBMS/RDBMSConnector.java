package com.itba.g2.storm.rDBMS;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/*
 * Class that establishes a connection with rdbms and returns an Connection object
 */
public class RDBMSConnector implements Serializable{
    String dbUrl = null;
    String dbClass = null;
//    Connection con = null;
    public Connection getConnection(final String sqlDBUrl, final String sqlUser, final String sqlPassword) throws ClassNotFoundException, SQLException {
        dbUrl =  sqlDBUrl + "?user="+ sqlUser +"&password=" + sqlPassword;
        dbClass = "com.mysql.jdbc.Driver";
        Class.forName(dbClass);
        Connection con = DriverManager.getConnection (dbUrl);
        return con;
    }
}
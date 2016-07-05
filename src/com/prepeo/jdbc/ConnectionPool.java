package com.prepeo.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.dbcp2.Utils;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class ConnectionPool {
	
	
	static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	static final String DBCP_DRIVER = "org.apache.commons.dbcp2.PoolingDriver";
	static final String POOL_NAME = "mysqlconnectionpool";
	static final String POOL_CONN_NAME = "jdbc:apache:commons:dbcp:" + POOL_NAME;
	
	private static ConnectionPool instance;
	
	private ConnectionPool() {
		
	}
	
	public static ConnectionPool getInstance() {
		if(instance == null) {
			instance = new ConnectionPool();
		}
		return instance;
	}
	
	
	private String uri;
	private String user;
	private String password;
	
	public static Class dirverClass;

	public void set(final String server, final int port, final String database
			, final String user, final String password) {
		
		uri = "jdbc:mysql://";
		uri += server;
		uri += ":";
		uri += Integer.toString(port);
		uri += "/";
		uri += database;
		uri += "?autoReconnect=true&useSSL=false";
		
		this.user = user;
		this.password = password;
	}

	
	public boolean startup() {
		
		try {
			dirverClass = Class.forName(MYSQL_DRIVER);
		} catch (ClassNotFoundException e) {
			System.err.println("There was not able to find the driver class " + MYSQL_DRIVER);
			return false;
		}
		
		ConnectionFactory dmcf = new DriverManagerConnectionFactory(
				this.uri, this.user, this.password);
		
		PoolableConnectionFactory pcf = new PoolableConnectionFactory(
				dmcf, null);
		
		ObjectPool op = new GenericObjectPool(pcf);
		pcf.setPool(op);
		
		PoolingDriver driver;
		try {
			Class.forName(DBCP_DRIVER);
			driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
		} catch (SQLException e) {
			System.err.println("There was an error: " + e.getMessage());
			return false;
		} catch (ClassNotFoundException e) {
			System.err.println("There was an error: " + e.getMessage());
			return false;
		}
		
		driver.registerPool(POOL_NAME, op);
		
		return true;
	}
	
	
	public Connection getConnection() {
		
		Connection conn;
		try {
			conn = DriverManager.getConnection(POOL_CONN_NAME);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return conn;
	}
	
	public void closeConnection(Connection con) {
		Utils.closeQuietly(con);
	}
	
}

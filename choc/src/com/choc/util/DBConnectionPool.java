package com.choc.util;

import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBConnectionPool {
	
	private static DBConnectionPool pool = new DBConnectionPool();
	
	private ComboPooledDataSource dataSource;
	
	public static DBConnectionPool getInstance() {
		return pool;
	}
	
	private DBConnectionPool() {
		dataSource = new ComboPooledDataSource();
		try {
			Properties prop = new Properties();
			InputStream inputstream = DBConnectionPool.class.getClassLoader().getResourceAsStream("./db.properties");
			prop.load(inputstream);
			String driver = prop.getProperty("driver");
			String url = prop.getProperty("url");
			String user = prop.getProperty("user");
			String password = prop.getProperty("password");
			
			dataSource.setDriverClass(driver);
			dataSource.setJdbcUrl(url);
			dataSource.setUser(user);
			dataSource.setPassword(password);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}
	
	private ComboPooledDataSource getDataSource() {
		return dataSource;
	}
	
	public Connection getConnection() {
		Connection con = null;
		try {
			con = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
}

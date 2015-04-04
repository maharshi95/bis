package test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public class Test {

	public static void main(String[] args) {

		
		try {
			Properties props = new Properties();
			InputStream fis = null;
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			try {
				fis = Test.class.getClassLoader().getResourceAsStream(
						"./db.properties");
				props.load(fis);
				String driver = props.getProperty("driver");
				String url = props.getProperty("url");
				String user = props.getProperty("user");
				String password = props.getProperty("password");
				
				DataSource unpooled = DataSources.unpooledDataSource(url, user, password);
				DataSource ds = DataSources.pooledDataSource(unpooled);
				
				cpds.setDriverClass(driver);
				cpds.setJdbcUrl(url);
				cpds.setUser(user);
				cpds.setPassword(password);
				
//				cpds.setMinPoolSize(5);                                     
//				cpds.setAcquireIncrement(5);
//				cpds.setMaxPoolSize(20);
				
			} catch (IOException | PropertyVetoException e) {
				e.printStackTrace();
			}
			
			System.out.println("num: " + cpds.getNumConnections());
			LinkedList<Connection> list = new LinkedList<Connection>();
			for(int i=0; i<100; i++) {
				Connection con = cpds.getConnection();
				list.add(con);
				System.out.println("num: " + cpds.getNumConnections());
				if(i > 8)
					list.remove().close();
			}
			
			System.out.println("num: " + cpds.getNumConnectionsDefaultUser());
			Connection con = cpds.getConnection();
			System.out.println("num: " + cpds.getNumConnectionsDefaultUser());
			
			System.out.println("con1: " + con);
			PreparedStatement statement = con
					.prepareStatement("select * from table_users");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String record = "";
				record += rs.getString("email_id");
				System.out.println(record);
			}
			rs.close();
			statement.close();
			System.out.println("num: " + cpds.getNumBusyConnections());
			con.close();
			while(!con.isClosed());
			System.out.println("num: " + cpds.getNumBusyConnections());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// EmailSenderPool pool = EmailSenderPool.getInstance();
		// String[] emails =
		// {"maharshigor@gmail.com","maharshigor18@gmail.com"};
		// String subject = "Simple Mail using java API";
		// String message =
		// "This mail is auto generated using java GMail API \n-Maharshi ";
		//
		// for(int i=0; i< emails.length; i++) {
		// EmailTask task = new EmailTask(emails[i], subject, message);
		// task.run();
		// // new Thread(task).start();
		// // pool.execute(task);
		// }
		// //pool.shutdown();
		// //while(!pool.isTerminated());
		// System.out.println("pool terminated!!");
	}
}
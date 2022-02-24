package com.creditSuisse.serverLogFIlter.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.creditSuisse.serverLogFIlter.dao.CEvent;
import com.creditSuisse.serverLogFIlter.util.ServerLogUtil;

public class ServerLogDB {
	public static Logger logger = LoggerFactory.getLogger(ServerLogDB.class);
	
	private static String connectionString = "jdbc:hsqldb:file:db-data/mydatabase";
	private final static String insertQuery = "insert into serverLog values (?,?,?,?,?);";
	private final static String createQuery = "create table if not exists serverLog"
			+ " (EVENT_ID varchar(100), EVENT_DURATION varchar(100), TYPE varchar(100), HOST varchar(100), alert varchar(10));";


	/**
	 * 
	 * @param eventList
	 */
	public static void addToDB(List<CEvent> eventList){
		logger.debug("addToDB: entering method");
	
		String tableName = ServerLogUtil.getNewTableNameBasedOnTime();
		logger.debug("addToDB: table[ "+tableName+"]");
		String createQueryNew = createQuery.replace("serverLog", tableName);
		logger.debug("addToDB: createQuery[ "+createQueryNew+"]");
		
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		try(Connection con = DriverManager.getConnection(connectionString, "SA", "");) {
			eventList.parallelStream().forEach((event)->{
				String log = null;
				try {
					con.createStatement().executeQuery(createQueryNew);
					PreparedStatement statment = con.prepareStatement(insertQuery);
					statment.setString(1, event.getEventId());
					statment.setString(2, event.getEventDuration());
					statment.setString(3, event.getType());
					statment.setString(4, event.getHost());
					statment.setString(5, event.getAlert());
					
					log = "addToDB: adding eventid["+event.getEventId()+"]";
					statment.executeUpdate();
					System.out.print(" added to DB.");					
					
				} catch (SQLException e) {
					log += " Failed to add to DB. reason:"+e.getMessage();										
				}
				logger.info(log);
			});

		} catch(Exception e) {
			logger.error("addToDB: exception caught", e);
			return;
		} 

		logger.debug("addToDB: exiting method");
	}

}
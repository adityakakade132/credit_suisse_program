package com.creditSuisse.serverLogFIlter;

import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.creditSuisse.serverLogFIlter.dao.CEvent;
import com.creditSuisse.serverLogFIlter.db.ServerLogDB;
import com.creditSuisse.serverLogFIlter.util.ServerLogUtil;

/**
 *  Aditya Kakade
 */
public class App 
{
	public static Logger logger = LoggerFactory.getLogger(App.class);



	public static void main( String[] args )
	{
		System.out.println( "Enter path of logfile.txt:" );
		Scanner scanner =  new Scanner(System.in);
		String filePath = scanner.nextLine();

		logger.info("filePath: "+filePath);
		//parse and filter
		List<CEvent> listEvent = ServerLogUtil.processLog(filePath);
		//Save to DB
		ServerLogDB.addToDB(listEvent);
		




	}
}

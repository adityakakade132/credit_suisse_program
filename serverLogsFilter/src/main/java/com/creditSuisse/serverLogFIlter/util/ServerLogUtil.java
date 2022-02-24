package com.creditSuisse.serverLogFIlter.util;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.creditSuisse.serverLogFIlter.App;
import com.creditSuisse.serverLogFIlter.dao.CEvent;
import com.creditSuisse.serverLogFIlter.dao.CLog;
import com.google.gson.Gson;

public class ServerLogUtil {
	public static Logger logger = LoggerFactory.getLogger(App.class);
	private static ConcurrentHashMap<String, CLog> logsMap = null;
	
	public static List<CEvent> processLog(String filePath) {
		logger.debug("processLog: entering method.");
		List<CEvent> returnList = null;
		List<String> fileData = null;
		if(StringUtils.isNotBlank(filePath)) {
			logsMap = new ConcurrentHashMap<String, CLog>();
			fileData = readFile(filePath);
			if(fileData != null) {
				returnList = parseFilterLogs(fileData);				
			}else {
				System.out.println("processLog: fileData is empty.");
			}
		}else {
			logger.error("readFile: fileName is empty.");
		}
		logger.debug("readFile: exiting method.");
		return returnList;
	}
	
	/**
	 * reading file
	 * @param filePath
	 */
	public static List<String> readFile(String filePath) {
		logger.debug("readFile: entering method.");
		List<String> fileDataReturn = null;
		if(StringUtils.isNotBlank(filePath)) {
			logsMap = new ConcurrentHashMap<String, CLog>();
			try {
				
				File file = new File(filePath);
				fileDataReturn = FileUtils.readLines(file,"utf8");
				logger.debug("readFile: File readed successfully.");

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("readFile: Exception caught", e);
			}

		}else {
			logger.error("readFile: fileName is empty.");
		}
		logger.debug("readFile: exiting method.");
		return fileDataReturn ;
	}

	
	/**
	 * filter out logs based on duration i.e, greater than 4 ms.
	 * @param logOne
	 */
	public static List<CEvent> parseFilterLogs(List<String> fileData) {
		logger.debug("processLog: entering method.");
		Gson gson = new Gson();
		List<CEvent> returnList = null;

		fileData.parallelStream().forEach((line)->{
			CLog logOne = gson.fromJson(line, CLog.class);
			if(logOne != null) {
				filterLogs(logOne);
			}else {
				logger.error("processLog: line is null.");
			}
		});
		returnList = getFilterLogList();
		logger.debug("readFile: exiting method.");
		return returnList;
	}
	/**
	 * 
	 * @param logOne
	 */
	private static void filterLogs(CLog logOne) {
		logger.debug("filterLogs: entering method.");
		Long completedTimeStamp = null;
		Long startedTimeStamp = null;
		Long difference = null;
		if(logOne != null) {
			CLog logTwo = logsMap.put(logOne.getId(), logOne);
			if(logTwo != null) {
				//one log already exist in current map.
				if(ServerLogConstant.STATE_STARTED.equals(logTwo.getState())) {
					startedTimeStamp = logTwo.getTimestamp();
					completedTimeStamp = logOne.getTimestamp();
				}else {
					startedTimeStamp = logOne.getTimestamp();
					completedTimeStamp = logTwo.getTimestamp();				
				}
			
				difference = completedTimeStamp - startedTimeStamp;
				if(difference <= 4l) {
					logger.debug("filterLogs: removing ID [{}] timestamp difference[{}] ",logOne.getId(), logOne.getTimestamp());
					logsMap.remove(logOne.getId());
				}else {
					logger.debug("filterLogs: updating ID [{}] timestamp difference[{}] ",logOne.getId(), difference);				
					logOne.setTimestamp(difference);
				}
			}
		}else {
			logger.error("filterLogs: logOne is null.");
		}
		logger.debug("filterLogs: exiting method.");
	}
	
	private static List<CEvent> getFilterLogList() {
		logger.debug("getFilterLogList: entering method.");
		
		List<CEvent> returnList = new CopyOnWriteArrayList<>();
		List<CLog> filterCLogList = null;
		if(logsMap != null) {
			filterCLogList = new ArrayList<CLog>(logsMap.values());
			filterCLogList.parallelStream().forEach((log)->{
				returnList.add(new CEvent(log.getId(), log.getTimestamp().toString(), log.getType(), log.getHost(), "true"));
			});
			logsMap.clear();		
		}else {
			logger.debug("getFilterLogList:: logsMap is null");
		}
		
		logger.debug("getFilterLogList: exiting method.");
		return returnList;		
	}
	
	public static String getNewTableNameBasedOnTime() {
		logger.debug("getNewTableNameBasedOnTime: entering method.");
		String table = "serverLog_";
		LocalDateTime now = LocalDateTime.now();  
        DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");  
        String formatDateTime = now.format(format);
        table = table.concat(formatDateTime);
        logger.debug("getNewTableNameBasedOnTime: exiting method.");
        return table;
	}


}

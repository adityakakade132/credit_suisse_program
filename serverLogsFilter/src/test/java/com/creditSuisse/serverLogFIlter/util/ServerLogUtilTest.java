package com.creditSuisse.serverLogFIlter.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.creditSuisse.serverLogFIlter.dao.CEvent;


public class ServerLogUtilTest {
	
	@Test
	public void testReadFilePositve() {
		String filePath = "logfile.txt";
		filePath = UnitTestUtil.getTestResourcePath(filePath);
		List<String> fileList = ServerLogUtil.readFile(filePath);;
		assertNotNull(fileList);		
	}
	@Test
	public void testReadFileNegative() {
		String filePath = "sdfsdf";
		List<String> fileList = ServerLogUtil.readFile(filePath);;
		assertNull(fileList);		
	}
	
	@Test
	public void testParseFilterLogsPositve() {
		List<String> list = new ArrayList<>();
		list.add("{\"id\":\"1\",\"state\":\"STARTED\",\"timestamp\":10,\"type\":\"APPLICATION LOG\",\"host\":\"abc\"}");
		list.add("{\"id\":\"2\",\"state\":\"FINISHED\",\"timestamp\":12,\"type\":\"APPLICATION LOG\",\"host\":\"abc\"}");
		list.add("{\"id\":\"2\",\"state\":\"STARTED\",\"timestamp\":10,\"type\":\"APPLICATION LOG\",\"host\":\"abc\"}");
		list.add("{\"id\":\"1\",\"state\":\"FINISHED\",\"timestamp\":15,\"type\":\"APPLICATION LOG\",\"host\":\"abc\"}");
		
		List<CEvent> fileList = ServerLogUtil.parseFilterLogs(list);
		assertEquals(1, fileList.size());		
	}
	
	@Test
	public void testParseFilterLogsNegative() {
		List<String> list = new ArrayList<>();
		list.add("{\"id\":\"1\",\"state\":\"STARTED\",\"timestamp\":10,\"type\":\"APPLICATION LOG\",\"host\":\"abc\"}");
		list.add("{\"id\":\"2\",\"state\":\"FINISHED\",\"timestamp\":12,\"type\":\"APPLICATION LOG\",\"host\":\"abc\"}");
		list.add("{\"id\":\"2\",\"state\":\"STARTED\",\"timestamp\":10,\"type\":\"APPLICATION LOG\",\"host\":\"abc\"}");
		list.add("{\"id\":\"1\",\"state\":\"FINISHED\",\"timestamp\":14,\"type\":\"APPLICATION LOG\",\"host\":\"abc\"}");
		
		List<CEvent> fileList = ServerLogUtil.parseFilterLogs(list);
		assertEquals(0, fileList.size());			
	}
	
	
	

	
}

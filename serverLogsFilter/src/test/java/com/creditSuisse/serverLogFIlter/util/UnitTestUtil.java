package com.creditSuisse.serverLogFIlter.util;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

public class UnitTestUtil {

	public static String getTestResourcePath(String strfile) {
		String absolutePath = null;
		if(StringUtils.isNotBlank(strfile)) {
			try {
				String path = "src/test/resources/"+strfile;
				File file = new File(path);
				absolutePath = file.getAbsolutePath();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return absolutePath;
	}
	

}

package com.properties;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesUtility {
	FileInputStream fis;
	Properties prop;

	public String readAnyProperty(String propName) {
		String val = null;
		try {
			fis = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/" + "/config.properties");
			prop = new Properties();
			prop.load(fis);

			val = prop.getProperty(propName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

}

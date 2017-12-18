package com.example.springboot;

import java.io.File;

import org.junit.BeforeClass;

public abstract class AbstractTest {
	
	@BeforeClass
	public static void initialize() {
		String workspaceLoc = System.getProperty("user.dir");
		System.setProperty("APP_HOME", workspaceLoc.concat(File.separator + "APP_HOME"));
	}
}

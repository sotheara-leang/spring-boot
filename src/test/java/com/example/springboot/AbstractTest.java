package com.example.springboot;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public abstract class AbstractTest {

	@BeforeClass
	public static void initialize() {
		String workspaceLoc = System.getProperty("user.dir");
		System.setProperty("APP_HOME", workspaceLoc.concat(File.separator + "APP_HOME"));
	}
}

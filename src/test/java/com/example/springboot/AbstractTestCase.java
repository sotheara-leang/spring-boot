package com.example.springboot;

import java.io.File;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public abstract class AbstractTestCase {

	@Autowired
	protected WebApplicationContext wac;
	
	protected MockMvc mockMvc;
	
	@BeforeClass
	public static void initialize() {
		String workspaceLoc = System.getProperty("user.dir");
		System.setProperty("APP_HOME", workspaceLoc.concat(File.separator + "APP_HOME"));
	}
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(this.wac)
				.build();
	}
}

package com.example.springboot.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public class AppProperties {

	@Value("${spring.application.name}")
	private String name;

	@Value("${APP_HOME}")
	private String home;

	private String configHome;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getConfigHome() {
		return configHome;
	}

	public void setConfigHome(String configHome) {
		this.configHome = configHome;
	}
}

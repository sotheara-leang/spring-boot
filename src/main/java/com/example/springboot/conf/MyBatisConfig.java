package com.example.springboot.conf;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.example.springboot.dao", annotationClass = Repository.class)
public class MyBatisConfig {
	
	@Autowired
	private AppProperties appProperties;

	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
//				.addScript("file:" + config.getConfigHome() + "/sql/create-schema.sql")
//				.addScript("file:" + config.getConfigHome() + "/sql/init-data.sql")
				.build();
	}

	@Bean
	public DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory(ApplicationContext appContext) throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setTypeAliasesPackage("com.example.springboot.dto");
		sessionFactory.setMapperLocations(appContext.getResources("file:" + appProperties.getConfigHome() + "/mybatis/mapper/**/*.xml"));
		sessionFactory.setConfigLocation(appContext.getResource("file:" + appProperties.getConfigHome() + "/mybatis/mybatis-config.xml"));
		sessionFactory.setDataSource(dataSource());
		return sessionFactory.getObject();
	}
}

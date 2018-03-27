package com.example.springboot.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springboot.AbstractServiceTest;
import com.example.springboot.dto.UserDto;

public class UserDaoTest extends AbstractServiceTest {

	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Test
	public void testSelectByLastNameAndFirstName() {
		MappedStatement mappedStatement = sqlSessionFactory
				.getConfiguration()
				.getMappedStatement( "com.example.springboot.dao.UserDao.selectByLastNameAndFirstName" );
		
		Map<String, Object> param = new HashMap<>();
		param.put( "firstName", "1" );
		
		BoundSql boundSql = mappedStatement.getBoundSql( param );
		
		System.out.println( boundSql.getSql() );
	}
	
	@Test
	public void testSelectByPage() {
		MappedStatement mappedStatement = sqlSessionFactory
				.getConfiguration()
				.getMappedStatement( "com.example.springboot.dao.UserDao.selectByPage" );
		
		Map<String, Object> param = new HashMap<>();
		param.put( "firstName", "2" );
		
		BoundSql boundSql = mappedStatement.getBoundSql( null );
		
		System.out.println( boundSql.getSql() );
	}
	
	@Test
	public void testInsertUserWithCondition() {
		MappedStatement mappedStatement = sqlSessionFactory
				.getConfiguration()
				.getMappedStatement( "com.example.springboot.dao.UserDao.insertUser" );
		
		Map<String, Object> subParam = new HashMap<>();
		subParam.put( "password", null );
		
		Map<String, Object> param = new HashMap<>();
		param.put( "user", subParam );
		
		BoundSql boundSql = mappedStatement.getBoundSql( new UserDto() );
		
		System.out.println( boundSql.getSql() );
	}
	
	@Test
	public void testInsertUserWithForeach() {
		MappedStatement mappedStatement = sqlSessionFactory
				.getConfiguration()
				.getMappedStatement( "com.example.springboot.dao.UserDao.insertUserBatch" );
		
	
		
		List<UserDto> userList = new ArrayList<>();
		//userList.add( new UserDto() );
		//userList.add( new UserDto() );
		
		Map<String, Object> param = new HashMap<>();
		param.put( "userList", userList );
		
		BoundSql boundSql = mappedStatement.getBoundSql( param );
		
		System.out.println( boundSql.getSql() );
	}
}

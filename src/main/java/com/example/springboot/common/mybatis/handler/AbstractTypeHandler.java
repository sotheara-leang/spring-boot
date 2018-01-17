package com.example.springboot.common.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public abstract class AbstractTypeHandler<T> extends BaseTypeHandler<T> {

	@Override
	public void setNonNullParameter( PreparedStatement ps, int i, T parameter, JdbcType jdbcType ) throws SQLException {
		Object dbValue = getDBValue( parameter );
		ps.setObject( i, dbValue );
	}

	@Override
	public T getNullableResult( ResultSet rs, String columnName ) throws SQLException {
		Object dbValue = rs.getObject(columnName);
		return getType( dbValue );
	}

	@Override
	public T getNullableResult( ResultSet rs, int columnIndex ) throws SQLException {
		Object dbValue = rs.getObject( columnIndex );
		return getType( dbValue );
	}

	@Override
	public T getNullableResult( CallableStatement cs, int columnIndex ) throws SQLException {
		Object dbValue = cs.getObject( columnIndex );
		return getType( dbValue );
	}
	
	public abstract T getType(Object dbValue);
	
	public abstract Object getDBValue(T type);
}

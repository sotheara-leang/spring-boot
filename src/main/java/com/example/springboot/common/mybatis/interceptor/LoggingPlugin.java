package com.example.springboot.common.mybatis.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.common.mybatis.util.SqlFormatter;

@Intercepts({ 
	@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
	@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }) ,
	@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) 
})
public class LoggingPlugin implements Interceptor {
	
	private static final Logger logger = LoggerFactory.getLogger(LoggingPlugin.class);
	
	private static final String PROP_PRETTY_PRINT = "prettyPrint";
	
	private boolean prettyPrint = false;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		try {
			Object[] arguments = invocation.getArgs();
			MappedStatement mappedStatement = (MappedStatement) arguments[0];
			
			Method method = getMethod( mappedStatement.getId() );
			if (isMethodPermitted( method )) {
				String sql = generateSQL(mappedStatement.getBoundSql( arguments[1] ));
				
				if (prettyPrint) {
					sql = SqlFormatter.format( sql );
				}
				
				logger.debug("==> SQL: {}", sql);
			}
			
		} catch (Exception e) {
			logger.error( e.getMessage(), e );
		}
		
		return invocation.proceed();
	}
	
	/**
	 * Check if method is permitted to logging
	 * @param method
	 * @return
	 */
	protected boolean isMethodPermitted(Method method) {
		return true;
	}
	
	/**
	 * Generate sql from {@link BoundSql}
	 * @param boundSql
	 * @return
	 * @throws Exception
	 */
	protected String generateSQL(BoundSql boundSql) throws Exception {
		String sql = boundSql.getSql().replaceAll("\\n", "").replaceAll("\\s{2,}", " ");
		
		try {
			Object parameter = boundSql.getParameterObject();
	
			List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
			for (ParameterMapping parameterMapping : parameterMappings) {
				String paramName = parameterMapping.getProperty();
				
				Object paramValue = null;
				if (boundSql.getAdditionalParameter(paramName) != null) {
					paramValue = boundSql.getAdditionalParameter(paramName);
				} else {
					paramValue = getParameterValue(parameter, paramName);
				}
				
				sql = sql.replaceFirst("\\?", formatParameterValue(paramValue));
			}
		} catch (Exception e) {
			logger.debug("Error generate SQL: {}", sql, e);
			throw e;
		}
		
		return sql;
	}
	
	/**
	 * Get parameter value from parameter object
	 * @param paramObject
	 * @param paramName
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	protected Object getParameterValue(Object paramObject, String paramName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Class<?> objClass = paramObject.getClass();
		
		// primitive type
		if (ClassUtils.isPrimitiveOrWrapper(objClass)) {
			return paramObject;
		}
		
		// other type
		Integer point = paramName.indexOf(".");
		Boolean isObjectMap = Map.class.isAssignableFrom(objClass);
		
		Object paramValue = null;
		
		// nested property
		if (point != -1) {
			String childParamName = paramName.substring(0, point);
			Object childObj = isObjectMap ? ((Map<?, ?>) paramObject).get(childParamName) : PropertyUtils.getProperty(paramObject, childParamName);
			
			paramValue = getParameterValue(childObj, paramName.substring(point + 1));
		} else {
			// basic property
			paramValue = isObjectMap ? ((Map<?, ?>) paramObject).get(paramName) : PropertyUtils.getProperty(paramObject, paramName);
		}
		
		return paramValue;
	}
	
	/**
	 * Format parameter value
	 * @param obj
	 * @return
	 */
	protected String formatParameterValue(Object obj) {
		Class<?> objClass = obj.getClass();
		
		if (CharSequence.class.isAssignableFrom(objClass)
				|| Enum.class.isAssignableFrom( objClass )) {
			return String.format("'%s'", obj);
		}
		
		return obj.toString();
	}
	
	/**
	 * Get dao method from statement id
	 * @param stmtId
	 * @return
	 * @throws Exception
	 */
	protected Method getMethod(String stmtId) throws Exception {
		int lastPoint = stmtId.lastIndexOf(".");
		String methodName = stmtId.substring(lastPoint + 1); 
		Class<?> daoClass = Class.forName(stmtId.substring(0, lastPoint));
		
		Method targetMethod = null;
		
		Method[] methods = daoClass.getDeclaredMethods();
		for (Method method : methods) {
			if (methodName.equals( method.getName() )) {
				targetMethod = method;
				break;
			}
		}
		
		return targetMethod;
	}
	
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		this.prettyPrint = Boolean.valueOf(properties.getProperty(PROP_PRETTY_PRINT));
	}
}

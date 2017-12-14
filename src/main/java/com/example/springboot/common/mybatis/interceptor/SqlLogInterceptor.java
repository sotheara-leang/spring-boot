package com.example.springboot.common.mybatis.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.common.mybatis.util.SqlFormatter;

@Intercepts({ 
	@Signature(type = StatementHandler.class, method = "update", args = { Statement.class }),
	@Signature(type = StatementHandler.class, method = "query", args = { Statement.class, ResultHandler.class }) 
})
public class SqlLogInterceptor implements Interceptor {

	private static final Logger logger = LoggerFactory.getLogger(SqlLogInterceptor.class);
	
	private static final String PRETTY_PRINT_PROPERTY_NAME = "prettyPrint";
	
	private boolean prettyPrint = false;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		final Object result = invocation.proceed();

		try {
			String sql = generateSQL(invocation);
			if (prettyPrint) {
				sql = SqlFormatter.format(sql);
			}
			
			logger.debug(sql);
			
		} catch (Exception e) {}
		
		return result;
	}
	
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		final String prettyPrint = properties.getProperty(PRETTY_PRINT_PROPERTY_NAME);
		if ("true".equals(prettyPrint) || "false".equals(prettyPrint)) {
			this.prettyPrint = Boolean.valueOf(prettyPrint);
		}
	}
	
	protected String generateSQL(Invocation invocation) throws Exception {
		String sql = null;
		
		try {
			final StatementHandler handler = (StatementHandler) invocation.getTarget();
			final BoundSql boundSql = handler.getBoundSql();
	
			final Object object = boundSql.getParameterObject();
	
			sql = boundSql.getSql().replaceAll("\\n", "").replaceAll("\\s{2,}", " ");
	
			final List<ParameterMapping> parameterMapping = boundSql.getParameterMappings();
			for (ParameterMapping pm : parameterMapping) {
				final String paramName = pm.getProperty();
				
				Object paramValue = null;
				if (boundSql.getAdditionalParameter(paramName) != null) {
					paramValue = boundSql.getAdditionalParameter(paramName);
				} else {
					paramValue = findParameterValue(object, paramName);
				}
	
				if (paramValue != null) {
					sql = sql.replaceFirst("\\?", formatPrimitiveType(paramValue));
				}
			}
		} catch (Exception e) {
			logger.debug("Error generate SQL: ", sql);
			throw e;
		}
		
		return sql;
	}
	
	protected Object findParameterValue(Object obj, String paramName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		final Class<?> objClass = obj.getClass();
		
		if (org.springframework.util.ClassUtils.isPrimitiveOrWrapper(objClass)) {
			return obj;
		}
		
		final Integer point = paramName.indexOf(".");
		final Boolean isObjectMap = Map.class.isAssignableFrom(objClass);
		
		if (point != -1) {
			final String childParamName = paramName.substring(0, point);
			final Object childObj = isObjectMap ? ((Map<?, ?>) obj).get(childParamName) : PropertyUtils.getProperty(obj, childParamName);
			
			return findParameterValue(childObj, paramName.substring(point + 1));
		}
		
		return isObjectMap ? ((Map<?, ?>) obj).get(paramName) : PropertyUtils.getProperty(obj, paramName);
	}
	
	protected String formatPrimitiveType(Object obj) {
		if (CharSequence.class.isAssignableFrom(obj.getClass())) {
			return String.format("'%s'", obj);
		}
		return obj.toString();
	}
}
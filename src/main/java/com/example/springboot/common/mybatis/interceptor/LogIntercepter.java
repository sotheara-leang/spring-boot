package com.example.springboot.common.mybatis.interceptor;

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

@Intercepts({ @Signature(type = StatementHandler.class, method = "update", args = { Statement.class }),
		@Signature(type = StatementHandler.class, method = "query", args = { Statement.class, ResultHandler.class }) })
public class LogIntercepter implements Interceptor {

	private static final Logger logger = LoggerFactory.getLogger(LogIntercepter.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		final StatementHandler handler = (StatementHandler) invocation.getTarget();
		final BoundSql boundSql = handler.getBoundSql();

		final Object object = boundSql.getParameterObject();
		final Class<?> objClass = object.getClass();

		String sql = boundSql.getSql().replaceAll("\\n", "").replaceAll("\\s{2,}", " ");

		final List<ParameterMapping> parameterMapping = boundSql.getParameterMappings();
		for (ParameterMapping pm : parameterMapping) {
			final String paramName = pm.getProperty();
			Object paramValue = null;

			if (org.springframework.util.ClassUtils.isPrimitiveOrWrapper(objClass)) {
				paramValue = object;
			} else if (Map.class.isAssignableFrom(objClass)) {
				paramValue = ((Map<?, ?>) object).get(paramName);
			} else {
				paramValue = PropertyUtils.getProperty(object, paramName);
			}

			if (paramValue != null) {
				sql = sql.replaceFirst("\\?", formatPrimitiveType(paramValue));
			}
		}

		Object result = invocation.proceed();

		logger.debug(sql);

		return result;
	}
	
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}
	
	private String formatPrimitiveType(Object obj) {
		if (CharSequence.class.isAssignableFrom(obj.getClass())) {
			return String.format("'%s'", obj);
		}
		return obj.toString();
	}
}

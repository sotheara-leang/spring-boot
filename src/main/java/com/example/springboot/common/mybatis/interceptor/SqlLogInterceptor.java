package com.example.springboot.common.mybatis.interceptor;

import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.ClassUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.common.mybatis.handler.AbstractTypeHandler;
import com.example.springboot.common.mybatis.util.SqlFormatter;

@Intercepts({ 
	@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
	@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) 
})
public class SqlLogInterceptor implements Interceptor {

	private static final Logger logger = LoggerFactory.getLogger(SqlLogInterceptor.class);
	
	private static final String PRETTY_PRINT_PROPERTY_NAME = "prettyPrint";
	
	private boolean prettyPrint = false;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		try {
			String sql = generateSQL(invocation);
			if (prettyPrint) {
				sql = SqlFormatter.format(sql);
			}
			
			logger.debug("==> SQL: {}", sql);
			
		} catch (Exception e) {}
		
		return invocation.proceed();
	}
	
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		this.prettyPrint = Boolean.valueOf(properties.getProperty(PRETTY_PRINT_PROPERTY_NAME));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String generateSQL(Invocation invocation) throws Exception {
		String sql = null;
		
		try {
			Object object = invocation.getArgs()[1];
			
			MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
			BoundSql boundSql = mappedStatement.getBoundSql(object);
			
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			
			sql = boundSql.getSql().replaceAll("\\n", "").replaceAll("\\s{2,}", " ");
	
			List<ParameterMapping> parameterMapping = boundSql.getParameterMappings();
			for (ParameterMapping pm : parameterMapping) {
				Class<?> paramType = pm.getJavaType();
				String paramName = pm.getProperty();
				PropertyTokenizer propTokenizer = new PropertyTokenizer( paramName );
				
				Object paramValue = object;
				if (paramName.startsWith(ForEachSqlNode.ITEM_PREFIX) && boundSql.hasAdditionalParameter(propTokenizer.getName())) {
					// for each
					paramValue = boundSql.getAdditionalParameter(propTokenizer.getName());
					if ( paramValue != null ) {
						paramValue = configuration.newMetaObject(paramValue).getValue(propTokenizer.getChildren());
					}
				} else if (boundSql.getAdditionalParameter(paramName) != null) {
					// additional parameter
					paramValue = boundSql.getAdditionalParameter(paramName);
				} else {
					if (!ClassUtils.isPrimitiveOrWrapper(object.getClass())) {
						paramValue = configuration.newMetaObject(object).getValue(paramName);
					}
				}
				
				if (Object.class.isAssignableFrom(paramType)) {
					paramType = paramValue.getClass();
				}
			
				// global type handler
				TypeHandler<? extends Object> typeHandler = typeHandlerRegistry.getTypeHandler(paramType);
				if (AbstractTypeHandler.class.isAssignableFrom(typeHandler.getClass())) {
					paramValue = ((AbstractTypeHandler) typeHandler).getDBValue(paramValue);
					
				} else if (AbstractTypeHandler.class.isAssignableFrom(pm.getTypeHandler().getClass())) {
					// specific type handler
					typeHandler = pm.getTypeHandler();
					paramValue = ((AbstractTypeHandler) typeHandler).getDBValue(paramValue);
				}
				
				sql = sql.replaceFirst("\\?", formatParameterValue(paramValue));
			}
		} catch (Exception e) {
			logger.debug("Error generating SQL: ", sql, e);
			throw e;
		}
		
		return sql;
	}
	
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	protected String generateSQL(Invocation invocation) throws Exception {
//		String sql = null;
//		
//		try {
//			Object object = invocation.getArgs()[1];
//			
//			MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
//			BoundSql boundSql = mappedStatement.getBoundSql(object);
//			
//			Configuration configuration = mappedStatement.getConfiguration();
//			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
//			
//			sql = boundSql.getSql().replaceAll("\\n", "").replaceAll("\\s{2,}", " ");
//	
//			List<ParameterMapping> parameterMapping = boundSql.getParameterMappings();
//			for (ParameterMapping pm : parameterMapping) {
//				Class<?> paramType = pm.getJavaType();
//				String paramName = pm.getProperty();
//				PropertyTokenizer propTokenizer = new PropertyTokenizer( paramName );
//				
//				Object paramValue = object;
//				if (paramName.startsWith(ForEachSqlNode.ITEM_PREFIX) && boundSql.hasAdditionalParameter(propTokenizer.getName())) {
//					// for each
//					paramValue = boundSql.getAdditionalParameter(propTokenizer.getName());
//					if ( paramValue != null ) {
//						paramValue = configuration.newMetaObject(paramValue).getValue(propTokenizer.getChildren());
//					}
//				} else if (boundSql.getAdditionalParameter(paramName) != null) {
//					paramValue = boundSql.getAdditionalParameter(paramName);
//					
//				} else if (typeHandlerRegistry.hasTypeHandler(paramType)) {
//					// global type handler
//					TypeHandler<? extends Object> typeHandler = typeHandlerRegistry.getTypeHandler(paramType);
//					if (AbstractTypeHandler.class.isAssignableFrom(typeHandler.getClass())) {
//						paramValue = configuration.newMetaObject(object).getValue(paramName);
//						paramValue = ((AbstractTypeHandler) typeHandler).getDBValue(paramValue);
//					}
//				} else if (AbstractTypeHandler.class.isAssignableFrom(pm.getTypeHandler().getClass())) {
//					// specific type handler
//					TypeHandler<?> typeHandler = pm.getTypeHandler();
//					if (object != null) {
//						paramValue = ((AbstractTypeHandler) typeHandler).getDBValue(object);
//					}
//				} else {
//					// simple
//					if (object != null) {
//						if (!ClassUtils.isPrimitiveOrWrapper(object.getClass())) {
//							paramValue = configuration.newMetaObject(object).getValue(paramName);
//						}
//					}
//				}
//				
//				sql = sql.replaceFirst("\\?", formatParameterValue(paramValue));
//				
//			}
//		} catch (Exception e) {
//			logger.debug("Error generating SQL: ", sql, e);
//			throw e;
//		}
//		
//		return sql;
//	}
//	
	protected String formatParameterValue(Object obj) {
		String formatStr = null;
		if (obj != null) {
			Class<?> objClass = obj.getClass();
			if (CharSequence.class.isAssignableFrom(objClass) || Enum.class.isAssignableFrom( objClass )) {
				formatStr = String.format("'%s'", obj);
			} else {
				formatStr = obj.toString();
			}
		}
		return formatStr;
	}
}

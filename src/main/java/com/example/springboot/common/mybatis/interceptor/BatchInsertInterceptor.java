package com.example.springboot.common.mybatis.interceptor;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.SerializationUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.common.mybatis.annotation.Batch;

@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class BatchInsertInterceptor implements Interceptor {
	
	private static final Logger logger = LoggerFactory.getLogger(BatchInsertInterceptor.class);

	private static final String DEFAULT_BATCH_SIZE_PROPERTY_NAME = "defaultBatchSize";
	
	private static final int DEFAULT_BATCH_SIZE = 50;
	
	private int defaultBatchSize;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		final MappedStatement stmt = (MappedStatement) invocation.getArgs()[0];
		
		if (stmt.getSqlCommandType() == SqlCommandType.INSERT) {
			final Object object = invocation.getArgs()[1];
			
			if (Map.class.isAssignableFrom(object.getClass()) && ((Map) object).size() == 2) {
				Map<String, Object> paramMap = (Map) object;

				final Object[] paramNames = paramMap.keySet().toArray();
				final Object param = paramMap.get(paramNames[0]);
				
				if (param != null && (List.class.isAssignableFrom(param.getClass()))
						|| Array.class.isAssignableFrom(param.getClass())) {
					
					final Method method = getBatchMethod(stmt.getId());
					final Batch batchAnnotation = method.getAnnotation(Batch.class);
					
					if (batchAnnotation != null) {
						int batchSize = batchAnnotation.size();
						if (batchSize == -1) {
							batchSize = defaultBatchSize;
						}
						
						final List<?> list = Array.class.isAssignableFrom(param.getClass()) ? Arrays.asList(param) : (List<Object>) param;
						
						if (list.size() <= batchSize) {
							return invocation.proceed();
						}

						int counter = 0;
						int rowEffect = 0;
						int batchIteration = 0;
						List<Object> subList = new ArrayList<>();
						for (int i = 0 ; i < list.size(); i++) {
							Object element = list.get(i);
							
							subList.add(element);
							counter++;
							
							if (counter == batchSize || (i == list.size() - 1)) {
								final Map<String, Object> subParamMap = (Map<String, Object>) SerializationUtils.clone((Serializable) object);
								subParamMap.put((String) paramNames[0], subList);
								subParamMap.put((String) paramNames[1], subList);
								
								final Object[] subArgs = invocation.getArgs();
								subArgs[1] = subParamMap;
								
								logger.debug("Invoke batch iteration {} with size {}: {}", ++batchIteration, batchSize, stmt.getId());
								logger.debug("Input data: {}", subList);
								
								Invocation subInvocation = new Invocation(invocation.getTarget(), invocation.getMethod(), subArgs);
								try {
									rowEffect += (int) subInvocation.proceed();
								} catch (Exception e) {
									logger.error("Error while invoking batch iteration {}", batchIteration);
									throw e;
								}
								
								subList = new ArrayList<>();
								counter = 0;
							}
						}
						return rowEffect;
					}
				}
			}
		}
		return invocation.proceed();
	}
	
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		final Object batchSize = properties.getOrDefault(DEFAULT_BATCH_SIZE_PROPERTY_NAME, DEFAULT_BATCH_SIZE);
		this.defaultBatchSize = batchSize instanceof String ? new Integer((String) batchSize) : (int) batchSize;
	}
	
	protected void processBatchInsert(Invocation invocation) {
		
	}
	
	protected Method getBatchMethod(String stmtId) throws Exception {
		final int lastPoint = stmtId.lastIndexOf(".");
		
		final String methodName = stmtId.substring(lastPoint + 1); 
		final Class<?> daoClass = Class.forName(stmtId.substring(0, lastPoint));
		
		Method method = null;
		try {
			method = daoClass.getMethod(methodName, List.class);
		} catch (Exception e) {
			
		}
		
		try {
			method = daoClass.getMethod(methodName, Array.class);
		} catch (Exception e) {
			
		}
		
		return method;
	}
}

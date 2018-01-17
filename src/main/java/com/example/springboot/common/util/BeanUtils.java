package com.example.springboot.common.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

public class BeanUtils {

	public static void copy( Object src, Object dest ) {

	}

	public static void copy( Object src, Object dest, Map<String, String> fielNameMapping )
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		org.apache.commons.beanutils.BeanUtils.copyProperties( dest, src );

		if ( fielNameMapping != null ) {
			Set<String> srcFieldNameSet = fielNameMapping.keySet();
			for ( String srcFieldName : srcFieldNameSet ) {
				String destFieldName = fielNameMapping.get( srcFieldName );

				try {
					Class<?> srcFieldType = PropertyUtils.getPropertyType( src, srcFieldName );
					Class<?> destFieldType = PropertyUtils.getPropertyType( dest, destFieldName );

					if ( srcFieldType != null && destFieldType != null ) {
						if ( destFieldType.isAssignableFrom( srcFieldType ) ) {
							Object srcFieldValue = PropertyUtils.getProperty( src, srcFieldName );
							if ( srcFieldValue != null ) {
								PropertyUtils.setProperty( dest, destFieldName, srcFieldValue );
							}
						}
					}
				} catch ( NoSuchMethodException e ) {
					throw e;
				}
			}
		}
	}
}

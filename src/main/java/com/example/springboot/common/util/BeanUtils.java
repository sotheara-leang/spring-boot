package com.example.springboot.common.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.common.Converter;
import com.example.springboot.common.FieldMapping;

public class BeanUtils {

	private static final Logger logger = LoggerFactory.getLogger( BeanUtils.class );

	public static void copy( Object src, Object dest )
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		copy( src, dest, new FieldMapping[]{}  );
	}

	public static void copy( Object src, Object dest, FieldMapping... fieldMappingList )
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		org.apache.commons.beanutils.BeanUtils.copyProperties( dest, src );

		if ( fieldMappingList != null ) {
			for ( FieldMapping fieldMapping : fieldMappingList ) {
				String srcFieldName = fieldMapping.getFromFieldName();
				String destFieldName = fieldMapping.getToFieldName();
				Converter fieldConverter = fieldMapping.getFieldConverter();

				try {
					if ( fieldConverter != null ) {
						Object srcValue = PropertyUtils.getProperty( src, srcFieldName );
						Object destField = fieldConverter.convert( srcValue );
						PropertyUtils.setProperty( dest, destFieldName, destField );

					} else {
						Class<?> srcFieldType = PropertyUtils.getPropertyType( src, srcFieldName );
						Class<?> destFieldType = PropertyUtils.getPropertyType( dest, destFieldName );

						if ( destFieldType.isAssignableFrom( srcFieldType ) ) {
							Object srcFieldValue = PropertyUtils.getProperty( src, srcFieldName );
							if ( srcFieldValue != null ) {
								PropertyUtils.setProperty( dest, destFieldName, srcFieldValue );
							}
						}
					}
				} catch ( Exception e ) {
					logger.error( "Error copy bean property: {}, {}", srcFieldName, destFieldName );
					throw e;
				}
			}
		}
	}
}

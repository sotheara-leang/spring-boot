package com.example.springboot.common.mybatis.handler;

import com.example.springboot.common.MultiValuesType;
import com.example.springboot.common.util.TypeUtils;

public class MultiValuesTypeHandler<E extends Enum<E>> extends AbstractTypeHandler<MultiValuesType<E>> {

	private Class<E> enumClass;
	
	public MultiValuesTypeHandler(Class<E> enumClass) {
		if (enumClass == null) {
			throw new IllegalArgumentException("enumClass must not be null");
		}
		this.enumClass = enumClass;
	}
	
	@Override
	public MultiValuesType<E> getType(Object dbValue) {
		MultiValuesType<E> multiValuesType = null;
		
		if (dbValue != null) {
			multiValuesType = TypeUtils.getMultiValuesType(enumClass, dbValue.toString());
		}
		
		return multiValuesType;
	}

	@Override
	public Object getDBValue(MultiValuesType<E> multiValuesType) {
		return TypeUtils.getBinaryMaskString(enumClass, multiValuesType);
	}
}
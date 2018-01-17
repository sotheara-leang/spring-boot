package com.example.springboot.common.mybatis.handler;

import com.example.springboot.common.EnumType;

public class EnumTypeHandler<E extends Enum<E> & EnumType<?>> extends AbstractTypeHandler<E> {

	private Class<E> enumClass;
	
	public EnumTypeHandler(Class<E> enumClass) {
		if (enumClass == null) {
			throw new IllegalArgumentException("enumClass must not be null");
		}
		this.enumClass = enumClass;
	}

	@Override
	public E getType( Object dbValue ) {
		return getEnumType(dbValue);
	}

	@Override
	public Object getDBValue( E type ) {
		return type.getValue();
	}
	
	protected E getEnumType(Object dbValue) {
		if (dbValue != null) {
			E[] enumConstants = enumClass.getEnumConstants();
			for (E enumContaint : enumConstants) {
				Object enumValue = enumContaint.getValue();
				if (enumValue.equals( dbValue ) ) {
					return enumContaint;
				}
			}
		}
		return null;
	}
}
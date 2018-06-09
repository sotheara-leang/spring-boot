package com.example.springboot.common.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.example.springboot.common.MultiValuesType;

public class TypeUtils {
	
	public static <E extends Enum<E>> String getBinaryMaskString(Class<E> enumClass, MultiValuesType<E> multiValuesType) {
		String bitArrayStr = "";
		
		E[] enumConstants = enumClass.getEnumConstants();
		
		if (multiValuesType != null) {
			String[] bitArray = new String[enumConstants.length];
			
			for (int i = 0; i < enumConstants.length; i++) {
				boolean exist = false;
				for (E value : multiValuesType) {
					if (enumConstants[i] == value) {
						exist = true;
						break;
					}
				}
				bitArray[i] = exist ? "1" : "0";
			}
			
			for (String bit : bitArray) {
				bitArrayStr += bit;
			}
		} else {
			bitArrayStr = StringUtils.left("0", enumConstants.length);
		}
		
		return bitArrayStr;
	}
	
	public static <E extends Enum<E>> MultiValuesType<E> getMultiValuesType(Class<E> enumClass, String binaryMaskString) {
		MultiValuesType<E> multiValuesType = null;
		
		if (binaryMaskString != null) {
			String bitArrayStr = binaryMaskString.toString();
			E[] enumConstants = enumClass.getEnumConstants();
			
			List<E> values = new ArrayList<E>();
			for (int i = 0; i < binaryMaskString.toString().length(); i++) {
				if (bitArrayStr.charAt(i) == '1') {
					values.add(enumConstants[i]);
				}
			}
			multiValuesType = new MultiValuesType<E>(values);
		}
		
		return multiValuesType;
	}
}

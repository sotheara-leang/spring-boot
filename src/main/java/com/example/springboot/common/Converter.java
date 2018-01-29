package com.example.springboot.common;

@FunctionalInterface
public interface Converter {
	
	Object convert( Object source );
}

package com.example.springboot.common;

import java.util.Collection;
import java.util.HashSet;

public class MultiValuesType<T extends Enum<T>> extends HashSet<T> {

	private static final long serialVersionUID = 1L;

	public MultiValuesType() {
		super();
	}

	public MultiValuesType(Collection<? extends T> c) {
		super(c);
	}
}
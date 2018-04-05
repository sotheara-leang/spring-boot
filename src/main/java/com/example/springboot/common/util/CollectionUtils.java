package com.example.springboot.common.util;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.util.Assert;

public abstract class CollectionUtils {

	public static <L, T> boolean existOfType( Collection<L> collection, Class<T> elementType ) {
		Assert.notNull( collection, "collection must not be null" );
		Assert.notNull( elementType, "elementType must not be null" );
		
		Iterator<L> iterator = collection.iterator();
		while ( iterator.hasNext() ) {
			L next = iterator.next();
			if ( elementType.isAssignableFrom( next.getClass() )) {
				return true;
			}
		}
		
		return false;
	}
}

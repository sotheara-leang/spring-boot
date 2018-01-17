package com.example.springboot.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtils {

	private static final Logger logger = LoggerFactory.getLogger( DateUtils.class );
	
	public static String formatDate( Date date, String format ) {
		if (date != null && format != null) {
			DateFormat df = new SimpleDateFormat(format); 
			df.setLenient( false );
			
			return df.format(date);
		}
		return null;
	}
	
	public static Date parseDate( String dateString, String format ) {
		DateFormat df = new SimpleDateFormat(format); 
		df.setLenient( false );
		try {
		    return df.parse(dateString);
		} catch (ParseException e) {
			logger.error( "Cannot parse date {} to {}", dateString, format, e );
		}
		return null;
	}
}

package com.example.springboot.common;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.example.springboot.common.util.BeanUtils;

public class BeanUtilsTest {
	
	@Test
	public void testCopy() throws Exception {
		SrcDto srcDto = new SrcDto();
		srcDto.setField1( "AAAA" );
		srcDto.setField3( "BBBB" );
		
		DestDto destDto = new DestDto();
		destDto.setField2( "BBBB" );
		destDto.setField3( 3 );
		
		Map<String, String> map = new HashMap<String, String>();
		map.put( "field1", "field3" );
		
		BeanUtils.copy( srcDto, destDto, map );
		
		System.out.println( srcDto );
		System.out.println( destDto );
	}
	
	class SrcDto {
		private String field1;
		private String field3;

		public String getField1() {
			return field1;
		}

		public void setField1( String field1 ) {
			this.field1 = field1;
		}

		public String getField3() {
			return field3;
		}

		public void setField3( String field3 ) {
			this.field3 = field3;
		}

		@Override
		public String toString() {
			return "SrcDto [field1=" + field1 + ", field3=" + field3 + "]";
		}
	}
	
	class DestDto {
		private String field2;
		private Integer field3;

		public String getField2() {
			return field2;
		}

		public void setField2( String field2 ) {
			this.field2 = field2;
		}
		
		public Integer getField3() {
			return field3;
		}

		public void setField3( Integer field3 ) {
			this.field3 = field3;
		}

		@Override
		public String toString() {
			return "DestDto [field2=" + field2 + ", field3=" + field3 + "]";
		}
	}
}

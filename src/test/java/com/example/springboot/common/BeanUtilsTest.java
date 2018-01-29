package com.example.springboot.common;

import org.junit.Test;

import com.example.springboot.common.util.BeanUtils;

public class BeanUtilsTest {
	
	@Test
	public void testCopy() throws Exception {
		SrcDto srcDto = new SrcDto();
		srcDto.setField1( "AAAA" );
		srcDto.setField3( "BBBB" );
		
		DestDto destDto = new DestDto();
		destDto.setField1( "CCCCC" );
		destDto.setField2( "DDDDD" );
		
		BeanUtils.copy( srcDto, destDto );
		
		System.out.println( srcDto );
		System.out.println( destDto );
	}
	
	@Test
	public void testCopyWithConverter() throws Exception {
		SrcDto srcDto = new SrcDto();
		srcDto.setField1( "AAAA" );
		srcDto.setField3( "BBBB" );
		
		DestDto destDto = new DestDto();
		destDto.setField1( "CCCCC" );
		destDto.setField2( "DDDDD" );
		
		FieldMapping mapping1 = new FieldMapping( "field3", "field2", field3 -> field3 + "Heello" );
		
		BeanUtils.copy( srcDto, destDto, mapping1 );
		
		System.out.println( srcDto );
		System.out.println( destDto );
	}
	
	public class SrcDto {
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
	
	public class DestDto {
		private String field1;
		private String field2;
		
		public String getField1() {
			return field1;
		}
		
		public void setField1( String field1 ) {
			this.field1 = field1;
		}
		
		public String getField2() {
			return field2;
		}
		
		public void setField2( String field2 ) {
			this.field2 = field2;
		}
		
		@Override
		public String toString() {
			return "DestDto [field1=" + field1 + ", field2=" + field2 + "]";
		}
	}
}

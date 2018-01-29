package com.example.springboot.common;

public class FieldMapping {
	private String fromFieldName;
	private String toFieldName;
	private Converter fieldConverter;

	public FieldMapping( String fromFieldName, String toFieldName ) {
		this( fromFieldName, toFieldName, null );
	}

	public FieldMapping( String fromFieldName, String toFieldName, Converter fieldConverter ) {
		this.fromFieldName = fromFieldName;
		this.toFieldName = toFieldName;
		this.fieldConverter = fieldConverter;
	}

	public String getFromFieldName() {
		return fromFieldName;
	}

	public void setFromFieldName( String fromFieldName ) {
		this.fromFieldName = fromFieldName;
	}

	public String getToFieldName() {
		return toFieldName;
	}

	public void setToFieldName( String toFieldName ) {
		this.toFieldName = toFieldName;
	}

	public Converter getFieldConverter() {
		return fieldConverter;
	}

	public void setFieldConverter( Converter fieldConverter ) {
		this.fieldConverter = fieldConverter;
	}

	@Override
	public String toString() {
		return "FieldMapping [fromFieldName=" + fromFieldName + ", toFieldName=" + toFieldName + ", fieldConverter="
				+ fieldConverter + "]";
	}
}
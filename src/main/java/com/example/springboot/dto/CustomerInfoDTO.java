package com.example.springboot.dto;

import lombok.Data;

@Data
public class CustomerInfoDTO {

	private String customerID;					
	private String frameworkChangeTimeStamp;
	private String customerStatusCode;    
	private String transactionCustomerYN;   
	private String customerCorpIndTypeCode; 
	private String customerIDNoTypeCode;    
	private String realIDNo;                
	private String realNameDivideNo;        
}

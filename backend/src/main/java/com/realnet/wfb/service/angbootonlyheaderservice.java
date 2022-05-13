package com.realnet.wfb.service;

public interface angbootonlyheaderservice {
	
	public String addhtmlfields(int id);
	
	public String updatefields(int id);
	
	public String readonlyfields(int id);
	
	public String dynamicfields(int id,String filetype,String operationtype , String formtype);

}
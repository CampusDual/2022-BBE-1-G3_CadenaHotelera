package com.ontimize.atomicHotelsApiRest.api.core.exceptions;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;

public class ValidateException extends Exception{
	private EntityResult entityResult;
	public ValidateException() {
		super();
	}
	public ValidateException(EntityResult entityResult){
		super();
		setEntityResult(entityResult);
	}
	public ValidateException(String msg,EntityResult entityResult){
		super(msg);
		setEntityResult(entityResult);
	}
	
	public ValidateException(String msg) {		
		super(msg);
	}
	
	public EntityResult getEntityResult() {
		if(entityResult== null) {
			entityResult = new EntityResultMapImpl();
			entityResult.setCode(EntityResult.OPERATION_WRONG);
			entityResult.setMessage(super.getMessage());
		}
		return entityResult;
	}
	public void setEntityResult(EntityResult entityResult) {
		this.entityResult = entityResult;
	}
	
	
}

package com.ontimize.atomicHotelsApiRest.api.core.exceptions;

import com.ontimize.jee.common.dto.EntityResult;

public class InfoValidateException extends ValidateException{

	public InfoValidateException() {
		super();
	}
	public InfoValidateException(String msg) {		
		super(msg);
	}
	public InfoValidateException(EntityResult entityResult){
		super();
		setEntityResult(entityResult);
	}
	public InfoValidateException(String msg,EntityResult entityResult){
		super(msg);
		setEntityResult(entityResult);
	}
}

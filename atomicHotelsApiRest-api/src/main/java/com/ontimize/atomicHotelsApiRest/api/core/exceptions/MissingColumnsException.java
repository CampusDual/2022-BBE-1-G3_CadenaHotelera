package com.ontimize.atomicHotelsApiRest.api.core.exceptions;

public class MissingColumnsException extends ValidateException{
	
	public MissingColumnsException() {
		super();
	}
	public MissingColumnsException(String msg) {		
		super(msg);
	}

}

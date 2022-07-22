package com.ontimize.atomicHotelsApiRest.api.core.exceptions;

public class MissingFieldsException extends ValidateException{

	public MissingFieldsException() {
		super();
	}
	public MissingFieldsException(String msg) {		
		super(msg);
	}
}

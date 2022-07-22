package com.ontimize.atomicHotelsApiRest.api.core.exceptions;

public class InvalidFieldsException extends ValidateException{

	public InvalidFieldsException() {
		super();
	}
	public InvalidFieldsException(String msg) {		
		super(msg);
	}
}

package com.ontimize.atomicHotelsApiRest.api.core.exceptions;

public class RestrictedFieldException extends ValidateException{

	public RestrictedFieldException() {
		super();
	}
	public RestrictedFieldException(String msg) {		
		super(msg);
	}
}

package com.ontimize.atomicHotelsApiRest.api.core.exceptions;

public class RestrictedFieldException extends Exception{

	public RestrictedFieldException() {
		super();
	}
	public RestrictedFieldException(String msg) {		
		super(msg);
	}
}

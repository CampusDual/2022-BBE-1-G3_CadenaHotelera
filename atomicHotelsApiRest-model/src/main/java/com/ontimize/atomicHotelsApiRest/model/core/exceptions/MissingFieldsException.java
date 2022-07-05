package com.ontimize.atomicHotelsApiRest.model.core.exceptions;

public class MissingFieldsException extends Exception{

	public MissingFieldsException() {
		super();
	}
	public MissingFieldsException(String msg) {		
		super(msg);
	}
}

package com.ontimize.atomicHotelsApiRest.api.core.exceptions;

public class InvalidFieldsValuesException extends Exception{

	public InvalidFieldsValuesException() {
		super();
	}
	public InvalidFieldsValuesException(String msg) {		
		super(msg);
	}
}

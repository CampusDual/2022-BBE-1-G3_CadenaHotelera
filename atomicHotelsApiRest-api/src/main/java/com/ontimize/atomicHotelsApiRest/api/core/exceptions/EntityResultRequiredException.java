package com.ontimize.atomicHotelsApiRest.api.core.exceptions;

public class EntityResultRequiredException extends Exception{

	public EntityResultRequiredException() {
		super();
	}
	public EntityResultRequiredException(String msg) {		
		super(msg);
	}
}

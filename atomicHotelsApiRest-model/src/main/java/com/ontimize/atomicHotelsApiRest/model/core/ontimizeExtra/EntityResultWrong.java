package com.ontimize.atomicHotelsApiRest.model.core.ontimizeExtra;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;


public class EntityResultWrong extends EntityResultMapImpl{

	public EntityResultWrong() {
		super(EntityResult.OPERATION_WRONG,EntityResult.NODATA_RESULT);
	}
	public EntityResultWrong(String resultMessage) {
		super(EntityResult.OPERATION_WRONG,EntityResult.NODATA_RESULT,resultMessage);
	}

}

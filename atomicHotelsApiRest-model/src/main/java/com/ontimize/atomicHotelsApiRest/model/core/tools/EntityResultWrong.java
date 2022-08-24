package com.ontimize.atomicHotelsApiRest.model.core.tools;
import java.util.HashMap;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;


public class EntityResultWrong extends EntityResultMapImpl{

	public EntityResultWrong() {
		super(EntityResult.OPERATION_WRONG,EntityResult.NODATA_RESULT);
	}
	public EntityResultWrong(HashMap h) {
		super(EntityResult.OPERATION_WRONG,EntityResult.NODATA_RESULT);
		if (h != null) {
            this.data = (Map) h.clone();
        }
	}
	public EntityResultWrong(String resultMessage) {
		super(EntityResult.OPERATION_WRONG,EntityResult.NODATA_RESULT,resultMessage);
	}
	

}

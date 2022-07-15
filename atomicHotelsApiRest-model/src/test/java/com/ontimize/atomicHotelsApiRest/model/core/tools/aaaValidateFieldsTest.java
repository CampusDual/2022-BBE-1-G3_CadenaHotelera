package com.ontimize.atomicHotelsApiRest.model.core.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.atomicHotelsApiRest.model.core.service.RoomService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Nested
@DisplayName("Test for ValidateFields")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)	
public class aaaValidateFieldsTest {
	
	
		@Test
		@DisplayName("Clave duplicada")
		void when_already_exist() {
//			Map<String, Object> attrMap = new HashMap<>() {{
//				put(RoomDao.ATTR_HOTEL_ID,1);
//				put(RoomDao.ATTR_NUMBER,1);			
//			}};			
////			EntityResult entityResult = service.roomInsert(attrMap);
////			EntityResult entityResult = service.roomInsert(new HashMap<>());
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(),entityResult.getMessage());
//			assertEquals(entityResult.getMessage(), ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);

		

	}
}
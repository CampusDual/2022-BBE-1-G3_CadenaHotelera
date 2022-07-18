package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BedComboDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class HotelServiceExtraServiceTest {

	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@InjectMocks
	HotelServiceExtraService service;
	
	@Autowired
	ValidateFields vf;

	// @Mock/@Autowired/@InjectMocks
	MissingFieldsException e;

	@Nested
	@DisplayName("Test for HotelServiceExtraServbice inserts")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class InsertQuery {

//		@Test
//		@DisplayName("Insert HotelServiceExtraService")
//		void when_hotelServiceExtraService_insert_is_succsessfull() {
//			Map<String, Object> attrMap = new HashMap<>() {
//				/**
//				 * 
//				 */
//				private static final long serialVersionUID = 1L;
//
//				{
//					put(BedComboDao.ATTR_ID, 1);
//					put(BedComboDao.ATTR_NAME, "Cama real para 2");
//					put(BedComboDao.ATTR_SLOTS, 8);
//				}
//			};
//			EntityResult resultado = new EntityResultMapImpl();
//			resultado.addRecord(attrMap);
//			resultado.setCode(EntityResult.OPERATION_SUCCESSFUL);
//			resultado.setMessage("Tipo de cama insertado");
//			when(daoHelper.insert(any(), anyMap())).thenReturn(resultado);
//			EntityResult entityResult = service.bedComboInsert(attrMap);
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
//			assertEquals(entityResult.getMessage(), "Tipo de cama insertado");
//		}
		
	@Test
	void test() {
		fail("Not yet implemented");
	}

	}
}

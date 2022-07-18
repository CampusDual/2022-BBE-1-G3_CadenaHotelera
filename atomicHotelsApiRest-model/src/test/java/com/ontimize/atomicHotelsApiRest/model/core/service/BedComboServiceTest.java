package com.ontimize.atomicHotelsApiRest.model.core.service;

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
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BedComboDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;

@Service("BedComboService")
@Lazy
public class BedComboServiceTest {

	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@InjectMocks
	BedComboService service;

	@Autowired
	BedComboDao bedComboDao;
	@Autowired
	ValidateFields vf;

	// @Mock/@Autowired/@InjectMocks
	MissingFieldsException e;

	@Nested
	@DisplayName("Test for BedCombo inserts")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class InsertQuery {

		@Test
		@DisplayName("Insert BedCombo")
		void when_bedCombo_insert_is_succsessfull() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(BedComboDao.ATTR_ID, 1);
					put(BedComboDao.ATTR_NAME, "Cama real para 2");
					put(BedComboDao.ATTR_SLOTS, 8);
				}
			};
			EntityResult resultado = new EntityResultMapImpl();
			resultado.addRecord(attrMap);
			resultado.setCode(EntityResult.OPERATION_SUCCESSFUL);
			resultado.setMessage("Tipo de cama insertado");
			when(daoHelper.insert(any(), anyMap())).thenReturn(resultado);
			EntityResult entityResult = service.bedComboInsert(attrMap);
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(entityResult.getMessage(), "Tipo de cama insertado");
		}

		@Test
		@DisplayName("Duplicated Key")
		void when_already_exist() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(BedComboDao.ATTR_ID, 1);
					put(BedComboDao.ATTR_NAME, "Cama real para 2");
					put(BedComboDao.ATTR_SLOTS, 8);
				}
			};
			when(daoHelper.insert(any(), anyMap())).thenThrow(DuplicateKeyException.class);
			EntityResult entityResult = service.bedComboInsert(attrMap);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(entityResult.getMessage(), ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}

		@Test
		@DisplayName("Miss Field Name")
		void when_Unable_Insert_Name() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
				put(BedComboDao.ATTR_ID, 1);
				put(BedComboDao.ATTR_NAME, null);
				put(BedComboDao.ATTR_SLOTS, 8);
				}	
			};
				EntityResult entityResult = service.bedComboInsert(attrMap);
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
				assertEquals(ErrorMessage.CREATION_ERROR + "El campo " + BedComboDao.ATTR_NAME + " es nulo",entityResult.getMessage());

		}
		@Test
		@DisplayName("Miss Field Slots")
		void when_Unable_Insert_Slots() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
				put(BedComboDao.ATTR_ID, 1);
				put(BedComboDao.ATTR_NAME,"La habitacion de test");
				put(BedComboDao.ATTR_SLOTS, null);
				}	
			};
				EntityResult entityResult = service.bedComboInsert(attrMap);
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
				assertEquals(ErrorMessage.CREATION_ERROR + "El campo " + BedComboDao.ATTR_SLOTS + " es nulo",entityResult.getMessage());

		}
		@Test
		@DisplayName("Negative Slots")
		void when_Slots_isNegative() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
				put(BedComboDao.ATTR_ID, 1);
				put(BedComboDao.ATTR_NAME,"La habitacion de test");
				put(BedComboDao.ATTR_SLOTS, -1);
				}	
			};
				EntityResult entityResult = service.bedComboInsert(attrMap);
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
				assertEquals(ErrorMessage.NEGATIVE_OR_CERO_NOT_ALLOWED + "El campo " + BedComboDao.ATTR_SLOTS + " es negativo",entityResult.getMessage());

		}
	}
}
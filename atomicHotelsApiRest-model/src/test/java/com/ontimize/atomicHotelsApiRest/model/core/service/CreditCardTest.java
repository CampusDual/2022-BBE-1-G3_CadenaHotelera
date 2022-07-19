package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CreditCardDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;

@ExtendWith(MockitoExtension.class)
public class CreditCardTest {

	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@InjectMocks
	CreditCardService service;

	@Autowired
	CreditCardDao creditCardDao;
	@Autowired
	ValidateFields vf;

	// @Mock/@Autowired/@InjectMocks
	MissingFieldsException e;
	
	@Nested
	@DisplayName("Test for creditCard queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class CreditCardQuery {

		@Test
		@DisplayName("Obtain all data from CreditCard table")
		void when_queryOnlyWithAllColumns_return_allCreditCardData() {
			doReturn(getCreditAllCardData()).when(daoHelper).query(any(), anyMap(), anyList());
			EntityResult entityResult = service.creditCardQuery (new HashMap<>(), new ArrayList<>());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(3, entityResult.calculateRecordNumber());
		}

		@Test
		@DisplayName("Obtain all data columns from CreditCard table when crd_id is -> 2")
		void when_queryAllColumns_return_specificData() {
			HashMap<String, Object> keyMap = new HashMap<>() {
				{
					put(CreditCardDao.ATTR_ID, 2);
				}
			};
			List<String> attrList = Arrays.asList(CreditCardDao.ATTR_ID, CreditCardDao.ATTR_NUMBER, CreditCardDao.ATTR_DATE_EXPIRY);
			doReturn(getSpecificCreditCardData(keyMap, attrList)).when(daoHelper).query(any(), anyMap(), anyList());
			EntityResult entityResult = service.creditCardQuery(new HashMap<>(), new ArrayList<>());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(1, entityResult.calculateRecordNumber());
			assertEquals(2, entityResult.getRecordValues(0).get(CreditCardDao.ATTR_ID));
		}

		@Test
		@DisplayName("Obtain all data columns from CreditCard table when cdc_id not exist")
		void when_queryAllColumnsNotExisting_return_empty() {
			HashMap<String, Object> keyMap = new HashMap<>() {
				{
					put(CreditCardDao.ATTR_ID, 5);
				}
			};
			List<String> attrList = Arrays.asList(CreditCardDao.ATTR_ID, CreditCardDao.ATTR_NUMBER, CreditCardDao.ATTR_DATE_EXPIRY);
			when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificCreditCardData(keyMap, attrList));
			EntityResult entityResult = service.creditCardQuery(new HashMap<>(), new ArrayList<>());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(0, entityResult.calculateRecordNumber());
		}

		@ParameterizedTest(name = "Obtain data with cdc_id -> {1}")
		@MethodSource("randomIDGenerator")
		@DisplayName("Obtain all data columns from CreditCard table when cdc_id is random")
		void when_queryAllColumnsWithRandomValue_return_specificData(int random) {
			HashMap<String, Object> keyMap = new HashMap<>() {
				{
					put(CreditCardDao.ATTR_ID, random);
				} 
			};
			List<String> attrList = Arrays.asList(CreditCardDao.ATTR_ID, CreditCardDao.ATTR_NUMBER, CreditCardDao.ATTR_DATE_EXPIRY);
			when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificCreditCardData(keyMap, attrList));
			EntityResult entityResult = service.creditCardQuery(new HashMap<>(), new ArrayList<>());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(1, entityResult.calculateRecordNumber());
			assertEquals(random, entityResult.getRecordValues(0).get(CreditCardDao.ATTR_ID));
		}

		public EntityResult getCreditAllCardData() {
			List<String> columnList = Arrays.asList(CreditCardDao.ATTR_ID, CreditCardDao.ATTR_NUMBER,CreditCardDao.ATTR_DATE_EXPIRY);
			EntityResult er = new EntityResultMapImpl(columnList);
			er.addRecord(new HashMap<String, Object>() {
				{
					put(CreditCardDao.ATTR_ID, 1);
					put(CreditCardDao.ATTR_NUMBER, "11111111111111");
					put(CreditCardDao.ATTR_DATE_EXPIRY,"2022-07-29");
				}
			});
			er.addRecord(new HashMap<String, Object>() {
				{
					put(CreditCardDao.ATTR_ID, 2);
					put(CreditCardDao.ATTR_NUMBER, "22222222222222");
					put(CreditCardDao.ATTR_DATE_EXPIRY,"2022-07-30");
				}
			});
			er.addRecord(new HashMap<String, Object>() {
				{
					put(CreditCardDao.ATTR_ID, 2);
					put(CreditCardDao.ATTR_NUMBER, "22222222222222");
					put(CreditCardDao.ATTR_DATE_EXPIRY,"2022-07-30");
				}
			});
			er.setCode(EntityResult.OPERATION_SUCCESSFUL);
			er.setColumnSQLTypes(new HashMap<String, Number>() {
				{
					put(CreditCardDao.ATTR_ID, Types.INTEGER);
					put(CreditCardDao.ATTR_NUMBER, Types.BIGINT);
					put(CreditCardDao.ATTR_DATE_EXPIRY , Types.DATE);
				}
			});
			return er;
		}

		public EntityResult getSpecificCreditCardData(Map<String, Object> keyValues, List<String> attributes) {
			EntityResult allData = this.getCreditAllCardData();
			int recordIndex = allData.getRecordIndex(keyValues);
			HashMap<String, Object> recordValues = (HashMap) allData.getRecordValues(recordIndex);
			List<String> columnList = Arrays.asList(CreditCardDao.ATTR_ID, CreditCardDao.ATTR_NUMBER,CreditCardDao.ATTR_DATE_EXPIRY);
			EntityResult er = new EntityResultMapImpl(columnList);
			if (recordValues != null) {
				er.addRecord(recordValues);
			}
			er.setCode(EntityResult.OPERATION_SUCCESSFUL);
			er.setColumnSQLTypes(new HashMap<String, Number>() {
				{
					put(CreditCardDao.ATTR_ID, Types.INTEGER);
					put(CreditCardDao.ATTR_NUMBER, Types.BIGINT);
					put(CreditCardDao.ATTR_DATE_EXPIRY , Types.DATE);
				}
			});
			return er;
		}

		List<Integer> randomIDGenerator() {
			List<Integer> list = new ArrayList<>();
			for (int i = 0; i < 5; i++) {
				list.add(ThreadLocalRandom.current().nextInt(1, 4));
			}
			return list;
		}

	}
 
	@Nested
	@DisplayName("Test for CreditCard inserts")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class InsertQuery {

		@Test
		@DisplayName("Insert CreditCard")
		void when_bedCombo_insert_is_succsessfull() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(CreditCardDao.ATTR_ID, 2);
					put(CreditCardDao.ATTR_NUMBER, "22222222222222");
					put(CreditCardDao.ATTR_DATE_EXPIRY,"2022-07-30");
				}
			};
			EntityResult resultado = new EntityResultMapImpl();
			resultado.addRecord(attrMap);
			resultado.setCode(EntityResult.OPERATION_SUCCESSFUL);
			resultado.setMessage("Tipo de cama insertado");
			doReturn(resultado).when(daoHelper).insert(any(),anyMap());
			EntityResult entityResult = service.creditCardInsert(attrMap);
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(entityResult.getMessage(), "Tarjeta registrada");
		}

		@Test
		@DisplayName("Duplicated Key")
		void when_already_exist() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(CreditCardDao.ATTR_ID, 2);
					put(CreditCardDao.ATTR_NUMBER, "22222222222222");
					put(CreditCardDao.ATTR_DATE_EXPIRY,"2022-07-30");
				}
			};
			when(daoHelper.insert(any(), anyMap())).thenThrow(DuplicateKeyException.class);
			EntityResult entityResult = service.creditCardInsert(attrMap);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(entityResult.getMessage(), ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}

		@Test
		@DisplayName("Miss Field Number")
		void when_Unable_Insert_Number() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(CreditCardDao.ATTR_ID, 2);
					put(CreditCardDao.ATTR_NUMBER, null);
					put(CreditCardDao.ATTR_DATE_EXPIRY,"2022-07-30");
				}	
			};
				EntityResult entityResult = service.creditCardInsert(attrMap);
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
				assertEquals(ErrorMessage.CREATION_ERROR,entityResult.getMessage());

		}
		@Test
		@DisplayName("Miss Field Expiry")
		void when_Unable_Insert_Expiry() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(CreditCardDao.ATTR_ID, 2);
					put(CreditCardDao.ATTR_NUMBER,"222222222222" );
					put(CreditCardDao.ATTR_DATE_EXPIRY,null);
				}	
			};
				EntityResult entityResult = service.creditCardInsert(attrMap);
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
				assertEquals(ErrorMessage.CREATION_ERROR ,entityResult.getMessage());

		}
		@Test
		@DisplayName("Negative number")
		void when_Number_isNegative() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(CreditCardDao.ATTR_ID, 2);
					put(CreditCardDao.ATTR_NUMBER, "-1234567890123");
					put(CreditCardDao.ATTR_DATE_EXPIRY,"2022-07-30");
				}	
			};
				EntityResult entityResult = service.creditCardInsert(attrMap);
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
				assertEquals(ErrorMessage.NEGATIVE_OR_CERO_NOT_ALLOWED ,entityResult.getMessage());

		}
	}
	
	
	
	
	
	
	
}
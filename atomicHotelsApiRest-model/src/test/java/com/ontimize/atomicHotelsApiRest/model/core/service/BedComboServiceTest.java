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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BedComboDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;

@ExtendWith(MockitoExtension.class)
public class BedComboServiceTest {

	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@Spy
	ControlFields cf;

	@InjectMocks
	BedComboService service;

	@Autowired
	BedComboDao bedComboDao;


	
	@Nested
	@DisplayName("Test for bedcombo queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class BedComboQuery { 

		@Test
		@DisplayName("Valores de entrada válidos")
		void when_queryOnlyWithAllColumns_return_allBedComboData() {
			
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList());
			
			EntityResult entityResult = service.bedComboQuery(new HashMap<>(), new ArrayList<>(){{
			add(BedComboDao.ATTR_NAME);	
			}
			});
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			
			entityResult=service.bedComboQuery(new HashMap<>(){
				{
				put(BedComboDao.ATTR_ID,1);
			};
				},new ArrayList<>() {
					{
				add(BedComboDao.ATTR_NAME);
			};
					});
			assertEquals(EntityResult.OPERATION_SUCCESSFUL,entityResult.getCode());
		}

		@Test
		@DisplayName("Obtain all data columns from bed table when bdc_id is -> 2")
		void when_queryAllColumns_return_specificData() {
			HashMap<String, Object> keyMap = new HashMap<>() {
				{
					put(BedComboDao.ATTR_ID, 2);
				}
			};
			List<String> attrList = Arrays.asList(BedComboDao.ATTR_ID, BedComboDao.ATTR_NAME,
					BedComboDao.ATTR_SLOTS);
			doReturn(getSpecificBedComboData(keyMap, attrList)).when(daoHelper).query(any(), anyMap(), anyList());
			EntityResult entityResult = service.bedComboQuery(new HashMap<>(), new ArrayList<>());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(1, entityResult.calculateRecordNumber());
			assertEquals(2, entityResult.getRecordValues(0).get(bedComboDao.ATTR_ID));
		}

		@Test
		@DisplayName("Obtain all data columns from Bedcombo table when bdc_id not exist")
		void when_queryAllColumnsNotExisting_return_empty() {
			HashMap<String, Object> keyMap = new HashMap<>() {
				{
					put(BedComboDao.ATTR_ID, 5);
				}
			};
			List<String> attrList = Arrays.asList(BedComboDao.ATTR_ID, BedComboDao.ATTR_NAME, BedComboDao.ATTR_SLOTS);
			when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificBedComboData(keyMap, attrList));
			EntityResult entityResult = service.bedComboQuery(new HashMap<>(), new ArrayList<>());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(0, entityResult.calculateRecordNumber());
		} 
//
//		@ParameterizedTest(name = "Obtain data with bdc_id -> {1}")
//		@MethodSource("randomIDGenerator")
//		@DisplayName("Obtain all data columns from BedCombo table when bdc_id is random")
//		void when_queryAllColumnsWithRandomValue_return_specificData(int random) {
//			HashMap<String, Object> keyMap = new HashMap<>() {
//				{
//					put(bedComboDao.ATTR_ID, random);
//				} 
//			};
//			List<String> attrList = Arrays.asList(BedComboDao.ATTR_ID, BedComboDao.ATTR_NAME, BedComboDao.ATTR_SLOTS);
//			when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificBedComboData(keyMap, attrList));
//			EntityResult entityResult = service.bedComboQuery(new HashMap<>(), new ArrayList<>());
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
//			assertEquals(1, entityResult.calculateRecordNumber());
//			assertEquals(random, entityResult.getRecordValues(0).get(BedComboDao.ATTR_ID));
//		}

		public EntityResult getAllBedComboData() {
			List<String> columnList = Arrays.asList(BedComboDao.ATTR_ID, BedComboDao.ATTR_NAME, BedComboDao.ATTR_SLOTS
					);
			EntityResult er = new EntityResultMapImpl(columnList);
			er.addRecord(new HashMap<String, Object>() {
				{
					put(BedComboDao.ATTR_ID, 1);
					put(BedComboDao.ATTR_NAME, "Cama simple");
					put(BedComboDao.ATTR_SLOTS, 1);
				}
			});
			er.addRecord(new HashMap<String, Object>() {
				{
					put(BedComboDao.ATTR_ID, 2);
					put(BedComboDao.ATTR_NAME, "Cama doble");
					put(BedComboDao.ATTR_SLOTS, 2);
				}
			});
			er.addRecord(new HashMap<String, Object>() {
				{
					put(BedComboDao.ATTR_ID, 1);
					put(BedComboDao.ATTR_NAME, "Cama Triple");
					put(BedComboDao.ATTR_SLOTS, 3);
				}
			});
			er.setCode(EntityResult.OPERATION_SUCCESSFUL);
			er.setColumnSQLTypes(new HashMap<String, Number>() {
				{
					put(BedComboDao.ATTR_ID, Types.INTEGER);
					put(BedComboDao.ATTR_NAME, Types.VARCHAR);
					put(BedComboDao.ATTR_SLOTS, Types.INTEGER);
				}
			});
			return er;
		}

		public EntityResult getSpecificBedComboData(Map<String, Object> keyValues, List<String> attributes) {
			EntityResult allData = this.getAllBedComboData();
			int recordIndex = allData.getRecordIndex(keyValues);
			HashMap<String, Object> recordValues = (HashMap) allData.getRecordValues(recordIndex);
			List<String> columnList = Arrays.asList(BedComboDao.ATTR_ID, BedComboDao.ATTR_NAME, BedComboDao.ATTR_SLOTS);
			EntityResult er = new EntityResultMapImpl(columnList);
			if (recordValues != null) {
				er.addRecord(recordValues);
			}
			er.setCode(EntityResult.OPERATION_SUCCESSFUL);
			er.setColumnSQLTypes(new HashMap<String, Number>() {
				{
					put(BedComboDao.ATTR_ID, Types.INTEGER);
					put(BedComboDao.ATTR_NAME, Types.VARCHAR);
					put(BedComboDao.ATTR_SLOTS, Types.INTEGER);
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
	@DisplayName("Test for BedCombo inserts")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class InsertQuery {

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
			doReturn(resultado).when(daoHelper).insert(any(),anyMap());
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
				assertEquals(ErrorMessage.NEGATIVE_OR_CERO_NOT_ALLOWED ,entityResult.getMessage());

		}
	}
	@Nested
	@DisplayName("Test for BedCombo updates")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class UpdateQuery {

		@Test
		@DisplayName("Update BedCombo")
		void when_BedCombo_update_is_succsessfull() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(BedComboDao.ATTR_ID, 1);
				}
			}; 
			Map<String, Object> keyMap = new HashMap<>() {
				{
					put(BedComboDao.ATTR_ID, 1);
					put(BedComboDao.ATTR_NAME,"La habitacion de test está cambiada");
					put(BedComboDao.ATTR_SLOTS, 6);
				}
			};
			EntityResult resultado = new EntityResultMapImpl();
			when(daoHelper.update(any(), anyMap(), anyMap())).thenReturn(resultado);
			EntityResult entityResult = service.bedComboUpdate(attrMap, keyMap);
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(entityResult.getMessage(), "Tipo de cama actualizado");
		} 

		@Test
		@DisplayName("Duplicated Key")
		void when_already_exist() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(BedComboDao.ATTR_ID, 1);
				
				}
			};
			Map<String, Object> keyMap = new HashMap<>() {
				{
					put(BedComboDao.ATTR_ID, 1);
					put(BedComboDao.ATTR_NAME, "Cama simple");
					put(BedComboDao.ATTR_SLOTS, 1);
				}
			};
			when(daoHelper.update(any(), anyMap(), anyMap())).thenThrow(DuplicateKeyException.class);
			EntityResult entityResult = service.bedComboUpdate(attrMap, keyMap);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(entityResult.getMessage(), ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		}
	
	}
	@Nested
	@DisplayName("Test for BedCombo delete")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class deleteQuery {
		
		@Test
		@DisplayName("Delete BedCombo")
		void when_bedCombo_delete_is_succsessfull() {
			
			
			
			Map<String, Object> attrMap = new HashMap<>() {
				
					{
						put(BedComboDao.ATTR_ID, 1);
				
					}
				
			};
			BedComboQuery culo=new BedComboQuery();
			doReturn(culo.getAllBedComboData()).when(daoHelper).query(any(), anyMap(), anyList());
			EntityResult resultado = new EntityResultMapImpl();
			resultado.addRecord(attrMap);
			resultado.setCode(EntityResult.OPERATION_SUCCESSFUL);
			resultado.setMessage("Tipo de cama borrado");
			doReturn(resultado).when(daoHelper).delete(any(),anyMap());
			EntityResult entityResult = service.bedComboDelete(new HashMap<>());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(entityResult.getMessage(), "Tipo de cama borrada");
		}

		
	}
	
	}
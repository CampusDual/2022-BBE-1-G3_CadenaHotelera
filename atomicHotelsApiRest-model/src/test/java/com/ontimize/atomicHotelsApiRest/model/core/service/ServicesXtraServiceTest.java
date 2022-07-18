package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
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
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServicesXtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class ServicesXtraServiceTest {
	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@InjectMocks
	ServicesXtraService service;

	@Autowired
	ServicesXtraDao servicesXtraDao;
	@Autowired
	ValidateFields vf;

	// @Mock/@Autowired/@InjectMocks
	MissingFieldsException e;

	@Nested
	@DisplayName("Test for ServicesXtra queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class ServicesXtraQuery {
		
		@Test
		@DisplayName("Obtain all data from ServicesXtra table")
		void when_queryOnlyWithAllColumns_return_allServicesXtraData() {
			doReturn(getAllServicesXtraData()).when(daoHelper).query(any(), anyMap(), anyList());
			EntityResult entityResult = service.servicesXtraQuery(new HashMap<>(), new ArrayList<>());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(3, entityResult.calculateRecordNumber());
		}

		@Test
		@DisplayName("Obtain all data columns from ServicesXtra table when sxt_id is -> 2")
		void when_queryAllColumns_return_specificData() {
			HashMap<String, Object> keyMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 2);
				}
			};
			List<String> attrList = Arrays.asList(ServicesXtraDao.ATTR_ID, ServicesXtraDao.ATTR_NAME, ServicesXtraDao.ATTR_DESCRIPTION);
			doReturn(getSpecificServicesXtraData(keyMap, attrList)).when(daoHelper).query(any(), anyMap(), anyList());
			EntityResult entityResult = service.servicesXtraQuery(new HashMap<>(), new ArrayList<>());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(1, entityResult.calculateRecordNumber());
			assertEquals(2, entityResult.getRecordValues(0).get(ServicesXtraDao.ATTR_ID));
		}
		
		@Test
		@DisplayName("Obtain all data from ServicesXtra table using a personalized query")
		void when_queryOnlyWithAllColumns_return_allHotelsData_fromPersonalizedQuery() {
			HashMap<String, Object> keyMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 2);
				} 
			};
			List<String> attrList = Arrays.asList(ServicesXtraDao.ATTR_ID, ServicesXtraDao.ATTR_NAME, ServicesXtraDao.ATTR_DESCRIPTION);
			
			doReturn(getSpecificServicesXtraData(keyMap, attrList)).when(daoHelper).query(any(), anyMap(), anyList(),anyString());
			EntityResult entityResult = service.servicesXtraQuery(new HashMap<>(), new ArrayList<>());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(1, entityResult.calculateRecordNumber());
			assertEquals(2, entityResult.getRecordValues(0).get(ServicesXtraDao.ATTR_ID));
		}

		@Test
		@DisplayName("Obtain all data columns from ServicesXtra table when sxt_id not exist")
		void when_queryAllColumnsNotExisting_return_empty() {
			HashMap<String, Object> keyMap = new HashMap<>() {
				{
					put(HotelDao.ATTR_ID, 5);
				}
			};
			List<String> attrList = Arrays.asList(ServicesXtraDao.ATTR_ID, ServicesXtraDao.ATTR_NAME, ServicesXtraDao.ATTR_DESCRIPTION);
			when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificServicesXtraData(keyMap, attrList));
			EntityResult entityResult = service.servicesXtraQuery(new HashMap<>(), new ArrayList<>());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(0, entityResult.calculateRecordNumber());
		}

		@ParameterizedTest(name = "Obtain data with sxt_id -> {1}")
		@MethodSource("randomIDGenerator")
		@DisplayName("Obtain all data columns from ServicesXtra table when sxt_id is random")
		void when_queryAllColumnsWithRandomValue_return_specificData(int random) {
			HashMap<String, Object> keyMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, random);
				}
			};
			List<String> attrList = Arrays.asList(ServicesXtraDao.ATTR_ID, ServicesXtraDao.ATTR_NAME, ServicesXtraDao.ATTR_DESCRIPTION);
			when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificServicesXtraData(keyMap, attrList));
			EntityResult entityResult = service.servicesXtraQuery(new HashMap<>(), new ArrayList<>());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(1, entityResult.calculateRecordNumber());
			assertEquals(random, entityResult.getRecordValues(0).get(ServicesXtraDao.ATTR_ID));
		}

		public EntityResult getAllServicesXtraData() {
			List<String> columnList = Arrays.asList(ServicesXtraDao.ATTR_ID, ServicesXtraDao.ATTR_NAME, ServicesXtraDao.ATTR_DESCRIPTION);
			EntityResult er = new EntityResultMapImpl(columnList);
			er.addRecord(new HashMap<String, Object>() {
				{
					put(ServicesXtraDao.ATTR_ID, 1);
					put(ServicesXtraDao.ATTR_NAME, "paseacanes");
					put(ServicesXtraDao.ATTR_DESCRIPTION, "canes que pasean humanos");
				}
			});
			er.addRecord(new HashMap<String, Object>() {
				{
					put(ServicesXtraDao.ATTR_ID, 2);
					put(ServicesXtraDao.ATTR_NAME, "Gatería");
					put(ServicesXtraDao.ATTR_DESCRIPTION, "Humanos domesticados por gatos");
				}
			});
			er.addRecord(new HashMap<String, Object>() {
				{
					put(ServicesXtraDao.ATTR_ID, 3);
					put(ServicesXtraDao.ATTR_NAME, "Pajarería");
					put(ServicesXtraDao.ATTR_DESCRIPTION, "Vienen volando");
				}
			});
			er.setCode(EntityResult.OPERATION_SUCCESSFUL);
			er.setColumnSQLTypes(new HashMap<String, Number>() {
				{
					put(ServicesXtraDao.ATTR_ID, Types.INTEGER);
					put(ServicesXtraDao.ATTR_NAME, Types.VARCHAR);
					put(ServicesXtraDao.ATTR_DESCRIPTION, Types.VARCHAR);
				}
			});
			return er;
		}

		public EntityResult getSpecificServicesXtraData(Map<String, Object> keyValues, List<String> attributes) {
			EntityResult allData = this.getAllServicesXtraData();
			int recordIndex = allData.getRecordIndex(keyValues);
			HashMap<String, Object> recordValues = (HashMap) allData.getRecordValues(recordIndex);
			List<String> columnList = Arrays.asList(ServicesXtraDao.ATTR_ID, ServicesXtraDao.ATTR_NAME,ServicesXtraDao.ATTR_DESCRIPTION);
			EntityResult er = new EntityResultMapImpl(columnList);
			if (recordValues != null) {
				er.addRecord(recordValues);
			}
			er.setCode(EntityResult.OPERATION_SUCCESSFUL);
			er.setColumnSQLTypes(new HashMap<String, Number>() {
				{
					put(ServicesXtraDao.ATTR_ID, Types.INTEGER);
					put(ServicesXtraDao.ATTR_NAME, Types.VARCHAR);
					put(ServicesXtraDao.ATTR_DESCRIPTION, Types.VARCHAR);
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
/*
 * 
					put(ServicesXtraDao.ATTR_DESCRIPTION, "canes que pasean humanos");
				}
			});
			er.addRecord(new HashMap<String, Object>() {
				{
					put(ServicesXtraDao.ATTR_ID, 2);
					put(ServicesXtraDao.ATTR_NAME, "Gatería");
					put(ServicesXtraDao.ATTR_DESCRIPTION, "Humanos domesticados por gatos");
				}
			});
			er.addRecord(new HashMap<String, Object>() {
				{
					put(ServicesXtraDao.ATTR_ID, 3);
					put(ServicesXtraDao.ATTR_NAME, "Pajarería");
					put(ServicesXtraDao.ATTR_DESCRIPTION, "Vienen volando");
 */
	@Nested
	@DisplayName("Test for ServicesXtra inserts")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class InsertQuery {

		@Test
		@DisplayName("Insert ServicesXtra")
		void when_ServicesXtra_insert_is_succsessfull() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 1);
					put(ServicesXtraDao.ATTR_NAME, "paseacanes");
					put(HotelDao.ATTR_DESCRIPTION, "Servicio extra a registrar");
				}
			};
			EntityResult resultado = new EntityResultMapImpl();
			resultado.addRecord(attrMap);
			resultado.setCode(EntityResult.OPERATION_SUCCESSFUL);
			resultado.setMessage("Servicio extra registrado");
			when(daoHelper.insert(any(), anyMap())).thenReturn(resultado);
			EntityResult entityResult = service.servicesXtraInsert(attrMap);
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(entityResult.getMessage(), "Servicio extra registrado");
		}

		@Test
		@DisplayName("Duplicated Key")
		void when_already_exist() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 1);
					put(ServicesXtraDao.ATTR_NAME, "paseacanes");
					put(HotelDao.ATTR_DESCRIPTION, "Este Servicio extra ya estaría registrado");
				}
			};
			when(daoHelper.insert(any(), anyMap())).thenThrow(DuplicateKeyException.class);
			EntityResult entityResult = service.servicesXtraInsert(attrMap);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(entityResult.getMessage(), ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}

		@Test
		@DisplayName("Missing Fields")
		void when_unable_insert() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 1);
					put(ServicesXtraDao.ATTR_NAME, null);
					put(ServicesXtraDao.ATTR_DESCRIPTION, "Faltan campos no nullables");
				}
			};
			
				EntityResult entityResult = service.servicesXtraInsert(attrMap);
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
				assertEquals(ErrorMessage.CREATION_ERROR + "El campo " + ServicesXtraDao.ATTR_NAME + " es nulo",entityResult.getMessage());

		}
	}

	@Nested
	@DisplayName("Test for servicesXtra updates")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class UpdateQuery {

		@Test
		@DisplayName("Update servicesXtra")
		void when_servicesXtra_insert_is_succsessfull() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(HotelDao.ATTR_ID, 1);
				}
			};
			Map<String, Object> keyMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 1);
					put(ServicesXtraDao.ATTR_NAME, "1 actualizado");
					put(ServicesXtraDao.ATTR_DESCRIPTION, "servicio extra actualizado");
				}
			};
			EntityResult resultado = new EntityResultMapImpl();
			resultado.addRecord(keyMap);
			resultado.setCode(EntityResult.OPERATION_SUCCESSFUL);
			resultado.setMessage("Servicio extra actualizado");

			when(daoHelper.update(any(), anyMap(), anyMap())).thenReturn(resultado);
			EntityResult entityResult = service.servicesXtraUpdate(attrMap, keyMap);
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(entityResult.getMessage(), "Hotel actualizado");
		}

		@Test
		@DisplayName("Duplicated Key")
		void when_already_exist() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(HotelDao.ATTR_ID, 2);
				}
			};
			Map<String, Object> keyMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 2);// ???
					put(ServicesXtraDao.ATTR_NAME, "paseahumanos");// Este hotel ya existe
					put(ServicesXtraDao.ATTR_DESCRIPTION, "servicio actualizado");
				}
			};
			when(daoHelper.update(any(), anyMap(), anyMap())).thenThrow(DuplicateKeyException.class);
			EntityResult entityResult = service.servicesXtraUpdate(attrMap, keyMap);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(entityResult.getMessage(), ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		}

//    	@Test
//		@DisplayName("Missing Fields")
//		void when_unable_insert() {
//			when(daoHelper.insert(any(),anyMap())).thenThrow(MissingFieldsException.class);
//			Map<String, Object> attrMap = new HashMap<>() {{
//				put(HotelDao.ATTR_ID, 1);
////                put(HotelDao.ATTR_NAME, "Hotel 23");
////                put(HotelDao.ATTR_STREET, "Avenida Sin Nombre Nº 1");
//                put(HotelDao.ATTR_CITY, "Vigo");
//                put(HotelDao.ATTR_CP, "36211");
//                put(HotelDao.ATTR_STATE, "Galicia");
//                put(HotelDao.ATTR_COUNTRY, "Spain");
//                put(HotelDao.ATTR_PHONE, "+34 986 111 111");
//                put(HotelDao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//                put(HotelDao.ATTR_DESCRIPTION, "Faltan campos no nullables");
//                put(HotelDao.ATTR_IS_OPEN, 1);	
//			}};			
//			EntityResult entityResult = service.hotelInsert(attrMap);
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//    		assertEquals(entityResult.getMessage(), ErrorMessage.CREATION_ERROR+e.getMessage());
//    	}
	}

}
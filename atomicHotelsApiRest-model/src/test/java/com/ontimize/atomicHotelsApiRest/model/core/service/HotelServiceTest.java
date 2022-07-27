package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@Mock
	ControlFields cf;

	@InjectMocks
	HotelService service;

	@Autowired
	HotelDao hotelDao;

	// datos entrada

	HashMap<String, Object> getFilterId() {
		HashMap<String, Object> filters = new HashMap<>() {
			{
				put(HotelDao.ATTR_ID, 1);
			}
		};
		return filters;
	};

	HashMap<String, Object> getFilterIdWrongValue() {
		HashMap<String, Object> filters = new HashMap<>() {
			{
				put(HotelDao.ATTR_ID, "albaricoque");
			}
		};
		return filters;
	};

	List<String> getColumsName() {
		List<String> columns = new ArrayList<>() {
			{
				add(HotelDao.ATTR_NAME);
			}
		};
		return columns;
	}
	// fin datos entrada

	@Nested
	@DisplayName("Test for Hotel queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class HotelQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testHotelQueryControlFieldsReset() {
			service.hotelQuery(TestingTools.getMapEmpty(), getColumsName());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testHotelQueryOK() {
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList());

			// válido: HashMap vacio (sin filtros)
			EntityResult entityResult = service.hotelQuery(TestingTools.getMapEmpty(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());

			// válido: HashMap con filtro que existe (sin filtros)
			entityResult = service.hotelQuery(getFilterId(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testHotelQueryKO() {
			try {
				EntityResult entityResult;
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				entityResult = service.hotelQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				entityResult = service.hotelQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				entityResult = service.hotelQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				entityResult = service.hotelQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				entityResult = service.hotelQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}

//		@Test
//		@DisplayName("XXX Valores de entrada NO válidos")
//		void testHotelQueryKO2() {
//			EntityResult entityResult ;
//
//			//no existen campos
//			entityResult = service.hotelQuery(TestingTools.getMapEmpty(), TestingTools.getListColumsNoExist());
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());
//
//			entityResult = service.hotelQuery(TestingTools.getMapKeyNoExist(), getColumsName());
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//
//			//valor erroneo en filtro
//			entityResult = service.hotelQuery(getFilterIdWrongValue(), getColumsName());
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//			
//			//null
//			entityResult = service.hotelQuery(null, getColumsName());
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//			
//			entityResult = service.hotelQuery(TestingTools.getMapEmpty(), null);
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());
//			
//			//columnas vacias
//			entityResult = service.hotelQuery(TestingTools.getMapEmpty(), TestingTools.getListEmpty());
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());
//						
//			System.err.println(entityResult.getMessage());
//						 			
//		}

//		@Test
//		@DisplayName("Obtain all data columns from hotels table when htl_id is -> 2")
//		void when_queryAllColumns_return_specificData() {
//			HashMap<String, Object> keyMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, 2);
//				}
//			};
//			List<String> attrList = Arrays.asList(HotelDao.ATTR_ID, HotelDao.ATTR_NAME, HotelDao.ATTR_STREET,
//					HotelDao.ATTR_CITY, HotelDao.ATTR_CP, HotelDao.ATTR_STATE, HotelDao.ATTR_COUNTRY,
//					HotelDao.ATTR_PHONE, HotelDao.ATTR_PHONE, HotelDao.ATTR_EMAIL, HotelDao.ATTR_DESCRIPTION,
//					HotelDao.ATTR_IS_OPEN);
//			doReturn(getSpecificHotelData(keyMap, attrList)).when(daoHelper).query(any(), anyMap(), anyList());
//			EntityResult entityResult = service.hotelQuery(new HashMap<>(), new ArrayList<>());
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
//			assertEquals(1, entityResult.calculateRecordNumber());
//			assertEquals(2, entityResult.getRecordValues(0).get(HotelDao.ATTR_ID));
//		}
//
//		@Test
//		@DisplayName("Obtain all data from Hotel table using a personalized query")
//		void when_queryOnlyWithAllColumns_return_allHotelsData_fromPersonalizedQuery() {
//			HashMap<String, Object> keyMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, 2);
//				}
//			};
//			List<String> attrList = Arrays.asList(HotelDao.ATTR_ID, HotelDao.ATTR_NAME, HotelDao.ATTR_STREET,
//					HotelDao.ATTR_CITY, HotelDao.ATTR_CP, HotelDao.ATTR_STATE, HotelDao.ATTR_COUNTRY,
//					HotelDao.ATTR_PHONE, HotelDao.ATTR_PHONE, HotelDao.ATTR_EMAIL, HotelDao.ATTR_DESCRIPTION,
//					HotelDao.ATTR_IS_OPEN);
//
//			doReturn(getSpecificHotelData(keyMap, attrList)).when(daoHelper).query(any(), anyMap(), anyList(),
//					anyString());
//			EntityResult entityResult = service.hotelDataQuery(new HashMap<>(), new ArrayList<>());
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
//			assertEquals(1, entityResult.calculateRecordNumber());
//			assertEquals(2, entityResult.getRecordValues(0).get(HotelDao.ATTR_ID));
//		}

//		@Test
//		@DisplayName("Obtain all data columns from hotels table when htl_id not exist")
//		void when_queryAllColumnsNotExisting_return_empty() {
//			HashMap<String, Object> keyMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, 5);
//				}
//			};
//			List<String> attrList = Arrays.asList(HotelDao.ATTR_ID, HotelDao.ATTR_NAME, HotelDao.ATTR_STREET,
//					HotelDao.ATTR_CITY, HotelDao.ATTR_CP, HotelDao.ATTR_STATE, HotelDao.ATTR_COUNTRY,
//					HotelDao.ATTR_PHONE, HotelDao.ATTR_PHONE, HotelDao.ATTR_EMAIL, HotelDao.ATTR_DESCRIPTION,
//					HotelDao.ATTR_IS_OPEN);
//			when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificHotelData(keyMap, attrList));
//			EntityResult entityResult = service.hotelQuery(new HashMap<>(), new ArrayList<>());
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
//			assertEquals(0, entityResult.calculateRecordNumber());
//		}

//		@ParameterizedTest(name = "Obtain data with htl_id -> {1}")
//		@MethodSource("randomIDGenerator")
//		@DisplayName("Obtain all data columns from hotels table when htl_id is random")
//		void when_queryAllColumnsWithRandomValue_return_specificData(int random) {
//			HashMap<String, Object> keyMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, random);
//				}
//			};
//			List<String> attrList = Arrays.asList(HotelDao.ATTR_ID, HotelDao.ATTR_NAME, HotelDao.ATTR_STREET,
//					HotelDao.ATTR_CITY, HotelDao.ATTR_CP, HotelDao.ATTR_STATE, HotelDao.ATTR_COUNTRY,
//					HotelDao.ATTR_PHONE, HotelDao.ATTR_PHONE, HotelDao.ATTR_EMAIL, HotelDao.ATTR_DESCRIPTION,
//					HotelDao.ATTR_IS_OPEN);
//			when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificHotelData(keyMap, attrList));
//			EntityResult entityResult = service.hotelQuery(new HashMap<>(), new ArrayList<>());
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
//			assertEquals(1, entityResult.calculateRecordNumber());
//			assertEquals(random, entityResult.getRecordValues(0).get(HotelDao.ATTR_ID));
//		}
//
//		public EntityResult getAllHotelsData() {
//			List<String> columnList = Arrays.asList(HotelDao.ATTR_ID, HotelDao.ATTR_NAME, HotelDao.ATTR_STREET,
//					HotelDao.ATTR_CITY, HotelDao.ATTR_CP, HotelDao.ATTR_STATE, HotelDao.ATTR_COUNTRY,
//					HotelDao.ATTR_PHONE, HotelDao.ATTR_PHONE, HotelDao.ATTR_EMAIL, HotelDao.ATTR_DESCRIPTION,
//					HotelDao.ATTR_IS_OPEN);
//			EntityResult er = new EntityResultMapImpl(columnList);
//			er.addRecord(new HashMap<String, Object>() {
//				{
//					put(HotelDao.ATTR_ID, 1);
//					put(HotelDao.ATTR_NAME, "Hotel 1");
//					put(HotelDao.ATTR_STREET, "Avenida Sin Nombre Nº 1");
//					put(HotelDao.ATTR_CITY, "Vigo");
//					put(HotelDao.ATTR_CP, "36211");
//					put(HotelDao.ATTR_STATE, "Galicia");
//					put(HotelDao.ATTR_COUNTRY, "Spain");
//					put(HotelDao.ATTR_PHONE, "+34 986 111 111");
//					put(HotelDao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//					put(HotelDao.ATTR_DESCRIPTION, "Hotel para pruebas unitarias");
//					put(HotelDao.ATTR_IS_OPEN, 1);
//				}
//			});
//			er.addRecord(new HashMap<String, Object>() {
//				{
//					put(HotelDao.ATTR_ID, 2);
//					put(HotelDao.ATTR_NAME, "Hotel 2");
//					put(HotelDao.ATTR_STREET, "Avenida Sin Nombre Nº 2");
//					put(HotelDao.ATTR_CITY, "Vigo");
//					put(HotelDao.ATTR_CP, "36211");
//					put(HotelDao.ATTR_STATE, "Galicia");
//					put(HotelDao.ATTR_COUNTRY, "Spain");
//					put(HotelDao.ATTR_PHONE, "+34 986 222 222");
//					put(HotelDao.ATTR_EMAIL, "hotel2@atomicHotels.com");
//					put(HotelDao.ATTR_DESCRIPTION, "Hotel para pruebas unitarias");
//					put(HotelDao.ATTR_IS_OPEN, 1);
//				}
//			});
//			er.addRecord(new HashMap<String, Object>() {
//				{
//					put(HotelDao.ATTR_ID, 3);
//					put(HotelDao.ATTR_NAME, "Hotel 3");
//					put(HotelDao.ATTR_STREET, "Avenida Sin Nombre Nº 3");
//					put(HotelDao.ATTR_CITY, "Vigo");
//					put(HotelDao.ATTR_CP, "36211");
//					put(HotelDao.ATTR_STATE, "Galicia");
//					put(HotelDao.ATTR_COUNTRY, "Spain");
//					put(HotelDao.ATTR_PHONE, "+34 986 333 333");
//					put(HotelDao.ATTR_EMAIL, "hotel3@atomicHotels.com");
//					put(HotelDao.ATTR_DESCRIPTION, "Hotel para pruebas unitarias");
//					put(HotelDao.ATTR_IS_OPEN, 0);
//				}
//			});
//			er.setCode(EntityResult.OPERATION_SUCCESSFUL);
//			er.setColumnSQLTypes(new HashMap<String, Number>() {
//				{
//					put(HotelDao.ATTR_ID, Types.INTEGER);
//					put(HotelDao.ATTR_NAME, Types.VARCHAR);
//					put(HotelDao.ATTR_STREET, Types.VARCHAR);
//					put(HotelDao.ATTR_CITY, Types.VARCHAR);
//					put(HotelDao.ATTR_CP, Types.VARCHAR);
//					put(HotelDao.ATTR_STATE, Types.VARCHAR);
//					put(HotelDao.ATTR_COUNTRY, Types.VARCHAR);
//					put(HotelDao.ATTR_PHONE, Types.VARCHAR);
//					put(HotelDao.ATTR_EMAIL, Types.VARCHAR);
//					put(HotelDao.ATTR_DESCRIPTION, Types.VARCHAR);
//					put(HotelDao.ATTR_IS_OPEN, Types.BINARY);
//				}
//			});
//			return er;
//		}
//
//		public EntityResult getSpecificHotelData(Map<String, Object> keyValues, List<String> attributes) {
//			EntityResult allData = this.getAllHotelsData();
//			int recordIndex = allData.getRecordIndex(keyValues);
//			HashMap<String, Object> recordValues = (HashMap) allData.getRecordValues(recordIndex);
//			List<String> columnList = Arrays.asList(HotelDao.ATTR_ID, HotelDao.ATTR_NAME, HotelDao.ATTR_STREET,
//					HotelDao.ATTR_CITY, HotelDao.ATTR_CP, HotelDao.ATTR_STATE, HotelDao.ATTR_COUNTRY,
//					HotelDao.ATTR_PHONE, HotelDao.ATTR_PHONE, HotelDao.ATTR_EMAIL, HotelDao.ATTR_DESCRIPTION,
//					HotelDao.ATTR_IS_OPEN);
//			EntityResult er = new EntityResultMapImpl(columnList);
//			if (recordValues != null) {
//				er.addRecord(recordValues);
//			}
//			er.setCode(EntityResult.OPERATION_SUCCESSFUL);
//			er.setColumnSQLTypes(new HashMap<String, Number>() {
//				{
//					put(HotelDao.ATTR_ID, Types.INTEGER);
//					put(HotelDao.ATTR_NAME, Types.VARCHAR);
//					put(HotelDao.ATTR_STREET, Types.VARCHAR);
//					put(HotelDao.ATTR_CITY, Types.VARCHAR);
//					put(HotelDao.ATTR_CP, Types.VARCHAR);
//					put(HotelDao.ATTR_STATE, Types.VARCHAR);
//					put(HotelDao.ATTR_COUNTRY, Types.VARCHAR);
//					put(HotelDao.ATTR_PHONE, Types.VARCHAR);
//					put(HotelDao.ATTR_EMAIL, Types.VARCHAR);
//					put(HotelDao.ATTR_DESCRIPTION, Types.VARCHAR);
//					put(HotelDao.ATTR_IS_OPEN, Types.BINARY);
//				}
//			});
//			return er;
//		}
//
//		List<Integer> randomIDGenerator() {
//			List<Integer> list = new ArrayList<>();
//			for (int i = 0; i < 5; i++) {
//				list.add(ThreadLocalRandom.current().nextInt(1, 4));
//			}
//			return list;
//		}

	}

	@Nested
	@DisplayName("Test for Hotel inserts")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class HotelInsert {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testhotelInsertControlFieldsReset() {
			service.hotelInsert(TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testhotelInsertOK() {
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList());

			// válido: HashMap campos mínimos
			EntityResult entityResult = service.hotelInsert(getMapRequiredInsert());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());

			// válido: HashMap campos mínimos y mas
			entityResult = service.hotelInsert(getMapRequiredInsertExtended());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testhotelInsertKO() {
			try {
				EntityResult entityResult;
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				entityResult = service.hotelInsert(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				entityResult = service.hotelInsert(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				entityResult = service.hotelInsert(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				entityResult = service.hotelInsert(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				entityResult = service.hotelInsert(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}
	}
//	@Nested
//	@DisplayName("Test for Hotel inserts")
//	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//	public class InsertQuery {
//
//		@Test
//		@DisplayName("Insert Hotel")
//		void when_hotel_insert_is_succsessfull() {
//			Map<String, Object> attrMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, 1);
//					put(HotelDao.ATTR_NAME, "Hotel 1");
//					put(HotelDao.ATTR_STREET, "Avenida Sin Nombre Nº 1");
//					put(HotelDao.ATTR_CITY, "Vigo");
//					put(HotelDao.ATTR_CP, "36211");
//					put(HotelDao.ATTR_STATE, "Galicia");
//					put(HotelDao.ATTR_COUNTRY, "Spain");
//					put(HotelDao.ATTR_PHONE, "+34 986 111 111");
//					put(HotelDao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//					put(HotelDao.ATTR_DESCRIPTION, "Hotel a registrar");
//					put(HotelDao.ATTR_IS_OPEN, 1);
//				}
//			};
//			EntityResult resultado = new EntityResultMapImpl();
//			resultado.addRecord(attrMap);
//			resultado.setCode(EntityResult.OPERATION_SUCCESSFUL);
//			resultado.setMessage("Hotel registrado");
//			when(daoHelper.insert(any(), anyMap())).thenReturn(resultado);
//			EntityResult entityResult = service.hotelInsert(attrMap);
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
//			assertEquals(entityResult.getMessage(), "Hotel registrado");
//		}
//
//		@Test
//		@DisplayName("Duplicated Key")
//		void when_already_exist() {
//			Map<String, Object> attrMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, 1);
//					put(HotelDao.ATTR_NAME, "Hotel 1");
//					put(HotelDao.ATTR_STREET, "Avenida Sin Nombre Nº 1");
//					put(HotelDao.ATTR_CITY, "Vigo");
//					put(HotelDao.ATTR_CP, "36211");
//					put(HotelDao.ATTR_STATE, "Galicia");
//					put(HotelDao.ATTR_COUNTRY, "Spain");
//					put(HotelDao.ATTR_PHONE, "+34 986 111 111");
//					put(HotelDao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//					put(HotelDao.ATTR_DESCRIPTION, "Este hotel ya estaría registrado");
//					put(HotelDao.ATTR_IS_OPEN, 1);
//				}
//			};
//			when(daoHelper.insert(any(), anyMap())).thenThrow(DuplicateKeyException.class);
//			EntityResult entityResult = service.hotelInsert(attrMap);
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//			assertEquals(entityResult.getMessage(), ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
//		}
//
//		@Test
//		@DisplayName("Missing Fields")
//		void when_unable_insert() {
//			Map<String, Object> attrMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, 1);
//					put(HotelDao.ATTR_NAME, null);
//					put(HotelDao.ATTR_STREET, "Avenida Sin Nombre Nº 1");
//					put(HotelDao.ATTR_CITY, "Vigo");
//					put(HotelDao.ATTR_CP, "36211");
//					put(HotelDao.ATTR_STATE, "Galicia");
//					put(HotelDao.ATTR_COUNTRY, "Spain");
//					put(HotelDao.ATTR_PHONE, "+34 986 111 111");
//					put(HotelDao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//					put(HotelDao.ATTR_DESCRIPTION, "Faltan campos no nullables");
//					put(HotelDao.ATTR_IS_OPEN, 1);
//				}
//			};
//			// try (MockedStatic<ValidateFields> vf =
//			// Mockito.mockStatic(ValidateFields.class)) {
//			// vf.when(() -> ValidateFields.required(anyMap(),
//			// anyString())).thenThrow(MissingFieldsException.class);
//			EntityResult entityResult = service.hotelInsert(attrMap);
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//			assertEquals(ErrorMessage.CREATION_ERROR + "El campo " + HotelDao.ATTR_NAME + " es nulo",
//					entityResult.getMessage());
//			// }
//
////			doThrow().when(ValidateFields.required(anyMap(), anyString())).thenThrow(MissingFieldsException.class);
////			when(daoHelper.insert(any(),anyMap())).thenThrow(new MissingFieldsException("El campo " + HotelDao.ATTR_NAME + " es nulo"));
////    		entityResult = service.hotelInsert(anyMap());
////			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//
//		}
//	}
//
//	@Nested
//	@DisplayName("Test for Hotel updates")
//	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//	public class UpdateQuery {
//
//		@Test
//		@DisplayName("Update Hotel")
//		void when_hotel_insert_is_succsessfull() {
//			Map<String, Object> attrMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, 1);
//				}
//			};
//			Map<String, Object> keyMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, 1);
//					put(HotelDao.ATTR_NAME, "Hotel 1 actualizado");
//					put(HotelDao.ATTR_STREET, "Avenida Sin Nombre Nº 1 actualizado");
//					put(HotelDao.ATTR_CITY, "Vigo actualizado");
//					put(HotelDao.ATTR_CP, "36211 actualizado");
//					put(HotelDao.ATTR_STATE, "Galicia");
//					put(HotelDao.ATTR_COUNTRY, "Spain");
//					put(HotelDao.ATTR_PHONE, "+34 986 111 111");
//					put(HotelDao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//					put(HotelDao.ATTR_DESCRIPTION, "Hotel actualizado");
//					put(HotelDao.ATTR_IS_OPEN, 0);
//				}
//			};
//			EntityResult resultado = new EntityResultMapImpl();
//			resultado.addRecord(keyMap);
//			resultado.setCode(EntityResult.OPERATION_SUCCESSFUL);
//			resultado.setMessage("Hotel actualizado");
//
//			when(daoHelper.update(any(), anyMap(), anyMap())).thenReturn(resultado);
//			EntityResult entityResult = service.hotelUpdate(attrMap, keyMap);
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
//			assertEquals(entityResult.getMessage(), "Hotel actualizado");
//		}
//
//		@Test
//		@DisplayName("Duplicated Key")
//		void when_already_exist() {
//			Map<String, Object> attrMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, 2);
//				}
//			};
//			Map<String, Object> keyMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, 2);// ???
//					put(HotelDao.ATTR_NAME, "Hotel 1");// Este hotel ya existe
//					put(HotelDao.ATTR_STREET, "Avenida Sin Nombre Nº 1 actualizado");
//					put(HotelDao.ATTR_CITY, "Vigo actualizado");
//					put(HotelDao.ATTR_CP, "36211 actualizado");
//					put(HotelDao.ATTR_STATE, "Galicia");
//					put(HotelDao.ATTR_COUNTRY, "Spain");
//					put(HotelDao.ATTR_PHONE, "+34 986 111 111");
//					put(HotelDao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//					put(HotelDao.ATTR_DESCRIPTION, "Hotel actualizado");
//					put(HotelDao.ATTR_IS_OPEN, 1);
//				}
//			};
//			when(daoHelper.update(any(), anyMap(), anyMap())).thenThrow(DuplicateKeyException.class);
//			EntityResult entityResult = service.hotelUpdate(attrMap, keyMap);
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//			assertEquals(entityResult.getMessage(), ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
//		}

	Map<String, Object> getMapRequiredInsert() {
		return new HashMap<>() {
			{
				put(HotelDao.ATTR_NAME, "Hotel 23");
				put(HotelDao.ATTR_STREET, "Avenida Sin Nombre Nº 1");
				put(HotelDao.ATTR_CITY, "Vigo");
				put(HotelDao.ATTR_CP, "36211");
				put(HotelDao.ATTR_STATE, "Galicia");
				put(HotelDao.ATTR_COUNTRY, "ES");
			}
		};
	}

	Map<String, Object> getMapRequiredInsertExtended() {

		return new HashMap<>() {
			{
				put(HotelDao.ATTR_NAME, "Hotel 23");
				put(HotelDao.ATTR_STREET, "Avenida Sin Nombre Nº 1");
				put(HotelDao.ATTR_CITY, "Vigo");
				put(HotelDao.ATTR_CP, "36211");
				put(HotelDao.ATTR_STATE, "Galicia");
				put(HotelDao.ATTR_COUNTRY, "Spain");
				put(HotelDao.ATTR_PHONE, "+34 986 111 111");
				put(HotelDao.ATTR_EMAIL, "hotel1@atomicHotels.com");
				put(HotelDao.ATTR_DESCRIPTION, "Faltan campos no nullables");
				put(HotelDao.ATTR_IS_OPEN, 1);
			}
		};
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
//	}

}

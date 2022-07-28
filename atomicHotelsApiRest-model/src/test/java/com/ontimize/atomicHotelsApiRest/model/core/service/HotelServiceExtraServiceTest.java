package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;

import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class HotelServiceExtraServiceTest {

	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@Spy
	ControlFields cf;

	@InjectMocks
	HotelServiceExtraService service;

	@Autowired
	HotelServiceExtraDao dao;

	EntityResult eR;

	@Nested
	@DisplayName("Test for Hotel Services Extra queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class hotelServiceExtraQuery {

		//hotelQuery
		@Test
		@DisplayName("ControlFields usar reset()")
		void testHotelQueryControlFieldsReset() {
			service.hotelServiceExtraQuery(TestingTools.getMapEmpty(), getColumsName());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testHotelQueryControlFieldsValidate() {
			service.hotelServiceExtraQuery(TestingTools.getMapEmpty(), getColumsName());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyList());
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testHotelQueryOK() {
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList());

			// válido: HashMap vacio (sin filtros)
			eR = service.hotelServiceExtraQuery(TestingTools.getMapEmpty(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

			// válido: HashMap con filtro que existe (sin filtros)
			eR = service.hotelServiceExtraQuery(getMapId(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testHotelQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}
		
	}

	@Nested
	@DisplayName("Test for Hotel Service Extra inserts")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class hotelServiceExtraInsert {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testhotelInsertControlFieldsReset() throws OntimizeJEERuntimeException, MissingFieldsException {
			service.hotelServiceExtraInsert(TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map ")
		void testHotelInsertControlFieldsValidate() throws OntimizeJEERuntimeException, MissingFieldsException {
			service.hotelServiceExtraInsert(TestingTools.getMapEmpty());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());

			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testhotelInsertOK() throws OntimizeJEERuntimeException, MissingFieldsException {
			doReturn(new EntityResultMapImpl()).when(daoHelper).insert(any(), anyMap());

			// válido: HashMap campos mínimos
			eR = service.hotelServiceExtraInsert(getMapRequiredInsert());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

			// válido: HashMap campos mínimos y mas
			eR = service.hotelServiceExtraInsert(getMapRequiredInsertExtended());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testhotelInsertKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(cf);
				// extra para controlar required:
				eR = service.hotelServiceExtraInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				System.out.println(eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.hotelServiceExtraInsert(getMapRequiredInsertExtendedWidthRestricted());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				System.out.println(eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}
	}

	@Nested
	@DisplayName("Test for Hotel updates")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class HotelUpdate {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testhotelUpdateControlFieldsReset() {
			service.hotelServiceExtraUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map ")
		void testHotelUpdateControlFieldsValidate() {
			service.hotelServiceExtraUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testhotelUpdateOK() {
			doReturn(new EntityResultMapImpl()).when(daoHelper).update(any(), anyMap(),anyMap());

			// válido: HashMap campos y filtros
			eR = service.hotelServiceExtraUpdate(getMapUpdate(), getMapId());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testhotelServiceExtraUpdateKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				// lanzamos todas las excepciones de SQL para comprobar que están bien
				// recojidas.
				doThrow(DuplicateKeyException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(DataIntegrityViolationException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(cf);
				// extra para controlar required:
				eR = service.hotelServiceExtraUpdate(getMapUpdate(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.CREATION_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.hotelServiceExtraUpdate(getMapId(), getMapId());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.CREATION_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}
	}

	@Nested
	@DisplayName("Test for Hotel deletes")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class HotelDelete {
//TODO PENDIENTE TERMINAR DELETE
		@Test
		@DisplayName("ControlFields usar reset()")
		void testhotelDeleteControlFieldsReset() {
			service.hotelServiceExtraDelete(TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map ")
		void testHotelDeleteControlFieldsValidate() {
			service.hotelServiceExtraDelete(TestingTools.getMapEmpty());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());

			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testhotelDeleteOK() {
			
			doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(),anyList());
			doReturn(new EntityResultMapImpl()).when(daoHelper).delete(any(), anyMap());

			// válido: HashMap campo único y exclusivo
			eR = service.hotelServiceExtraDelete(getMapId());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());
 
		}
		
		@Test
		@DisplayName("Valores Subcontulta Error")
		void testhotelDeleteSubQueryKO() {
			doReturn(new EntityResultWrong()).when(daoHelper).query(any(), anyMap(),anyList());
//			doReturn(new EntityResultMapImpl()).when(daoHelper).delete(any(), anyMap());
			
			// 
			eR = service.hotelServiceExtraDelete(getMapId());
			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
			assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
		}
		
		@Test
		@DisplayName("Valores Subconsultta 0 resultados")
		void testhotelDeleteSubQueryNoResults() {
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(),anyList());
//			doReturn(new EntityResultMapImpl()).when(daoHelper).delete(any(), anyMap());
			
			// 
			eR = service.hotelServiceExtraDelete(getMapId());
			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
			assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
		}
		
		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testhotelDeleteKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				// lanzamos todas las excepciones de SQL para comprobar que están bien
				// recojidas.
				/*doThrow(DataIntegrityViolationException.class).when(cf).validate(anyMap());
				eR = service.hotelServiceExtraDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());*/

				reset(cf); //para quitar doThrow anterior
				// extra para controlar required:
				eR = service.hotelServiceExtraDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.hotelServiceExtraDelete(getMapRequiredDeletetExtendedWidthRestricted());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}
	}

	// datos entrada

	Map<String, Object> getMapRequiredInsert() {
		return new HashMap<>() {
			{
				put(dao.ATTR_ID_HTL, 2);
				put(dao.ATTR_ID_SXT, 3);
				put(dao.ATTR_PRECIO, 30.75);
			}
		};
	}

	Map<String, Object> getMapUpdate() {
		return getMapRequiredInsert();
	}

	

	Map<String, Object> getMapRequiredInsertExtended() {
		return new HashMap<>() {
			{
				put(dao.ATTR_ID_HTL, 2);
				put(dao.ATTR_ID_SXT, 3);
				put(dao.ATTR_PRECIO, 30.75);
			}
		};
	}

	Map<String, Object> getMapRequiredInsertExtendedWidthRestricted() {
		return new HashMap<>() {
			{
				put(dao.ATTR_ID,1);
				put(dao.ATTR_ID_HTL, 4);
				put(dao.ATTR_ID_SXT, 3);
				put(dao.ATTR_PRECIO, 22.3);
			}
		};
	}

	Map<String, Object> getMapRequiredDeletetExtendedWidthRestricted() {
		return getMapRequiredInsertExtendedWidthRestricted();
	}

	HashMap<String, Object> getMapId() {
		HashMap<String, Object> filters = new HashMap<>() {
			{
				put(dao.ATTR_ID, 1);
			}
		};
		return filters;
	};
	//
//		HashMap<String, Object> getMapIdWrongValue() {
//			HashMap<String, Object> filters = new HashMap<>() {
//				{
//					put(hoteldao.ATTR_ID, "albaricoque");
//				}
//			};
//			return filters;
//		};

	List<String> getColumsName() {
		List<String> columns = new ArrayList<>() {
			{
				add(dao.ATTR_ID_HTL);
			}
		};
		return columns;
	}
	// fin datos entrada

	

//	@Test
//	@DisplayName("XXX Valores de entrada NO válidos")
//	void testHotelQueryKO2() {
//		eR ;
//
//		//no existen campos
//		eR = service.hotelQuery(TestingTools.getMapEmpty(), TestingTools.getListColumsNoExist());
//		assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//
//		eR = service.hotelQuery(TestingTools.getMapKeyNoExist(), getColumsName());
//					assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//
//		//valor erroneo en filtro
//		eR = service.hotelQuery(getFilterIdWrongValue(), getColumsName());
//					assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//		
//		//null
//		eR = service.hotelQuery(null, getColumsName());
//					assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//		
//		eR = service.hotelQuery(TestingTools.getMapEmpty(), null);
//		assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//		
//		//columnas vacias
//		eR = service.hotelQuery(TestingTools.getMapEmpty(), TestingTools.getListEmpty());
//		assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//					
//		System.err.println(eR.getMessage());
//					 			
//	}

//	@Test
//	@DisplayName("Obtain all data columns from hotels table when htl_id is -> 2")
//	void when_queryAllColumns_return_specificData() {
//		HashMap<String, Object> keyMap = new HashMap<>() {
//			{
//				put(hoteldao.ATTR_ID, 2);
//			}
//		};
//		List<String> attrList = Arrays.asList(hoteldao.ATTR_ID, hoteldao.ATTR_NAME, hoteldao.ATTR_STREET,
//				hoteldao.ATTR_CITY, hoteldao.ATTR_CP, hoteldao.ATTR_STATE, hoteldao.ATTR_COUNTRY,
//				hoteldao.ATTR_PHONE, hoteldao.ATTR_PHONE, hoteldao.ATTR_EMAIL, hoteldao.ATTR_DESCRIPTION,
//				hoteldao.ATTR_IS_OPEN);
//		doReturn(getSpecificHotelData(keyMap, attrList)).when(daoHelper).query(any(), anyMap(), anyList());
//		eR = service.hotelQuery(new HashMap<>(), new ArrayList<>());
//					assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());
//		assertEquals(1, eR.calculateRecordNumber());
//		assertEquals(2, eR.getRecordValues(0).get(hoteldao.ATTR_ID));
//	}
//
//	@Test
//	@DisplayName("Obtain all data from Hotel table using a personalized query")
//	void when_queryOnlyWithAllColumns_return_allHotelsData_fromPersonalizedQuery() {
//		HashMap<String, Object> keyMap = new HashMap<>() {
//			{
//				put(hoteldao.ATTR_ID, 2);
//			}
//		};
//		List<String> attrList = Arrays.asList(hoteldao.ATTR_ID, hoteldao.ATTR_NAME, hoteldao.ATTR_STREET,
//				hoteldao.ATTR_CITY, hoteldao.ATTR_CP, hoteldao.ATTR_STATE, hoteldao.ATTR_COUNTRY,
//				hoteldao.ATTR_PHONE, hoteldao.ATTR_PHONE, hoteldao.ATTR_EMAIL, hoteldao.ATTR_DESCRIPTION,
//				hoteldao.ATTR_IS_OPEN);
//
//		doReturn(getSpecificHotelData(keyMap, attrList)).when(daoHelper).query(any(), anyMap(), anyList(),
//				anyString());
//		eR = service.hotelDataQuery(new HashMap<>(), new ArrayList<>());
//					assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());
//		assertEquals(1, eR.calculateRecordNumber());
//		assertEquals(2, eR.getRecordValues(0).get(hoteldao.ATTR_ID));
//	}

//	@Test
//	@DisplayName("Obtain all data columns from hotels table when htl_id not exist")
//	void when_queryAllColumnsNotExisting_return_empty() {
//		HashMap<String, Object> keyMap = new HashMap<>() {
//			{
//				put(hoteldao.ATTR_ID, 5);
//			}
//		};
//		List<String> attrList = Arrays.asList(hoteldao.ATTR_ID, hoteldao.ATTR_NAME, hoteldao.ATTR_STREET,
//				hoteldao.ATTR_CITY, hoteldao.ATTR_CP, hoteldao.ATTR_STATE, hoteldao.ATTR_COUNTRY,
//				hoteldao.ATTR_PHONE, hoteldao.ATTR_PHONE, hoteldao.ATTR_EMAIL, hoteldao.ATTR_DESCRIPTION,
//				hoteldao.ATTR_IS_OPEN);
//		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificHotelData(keyMap, attrList));
//		eR = service.hotelQuery(new HashMap<>(), new ArrayList<>());
//					assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());
//		assertEquals(0, eR.calculateRecordNumber());
//	}

//	@ParameterizedTest(name = "Obtain data with htl_id -> {1}")
//	@MethodSource("randomIDGenerator")
//	@DisplayName("Obtain all data columns from hotels table when htl_id is random")
//	void when_queryAllColumnsWithRandomValue_return_specificData(int random) {
//		HashMap<String, Object> keyMap = new HashMap<>() {
//			{
//				put(hoteldao.ATTR_ID, random);
//			}
//		};
//		List<String> attrList = Arrays.asList(hoteldao.ATTR_ID, hoteldao.ATTR_NAME, hoteldao.ATTR_STREET,
//				hoteldao.ATTR_CITY, hoteldao.ATTR_CP, hoteldao.ATTR_STATE, hoteldao.ATTR_COUNTRY,
//				hoteldao.ATTR_PHONE, hoteldao.ATTR_PHONE, hoteldao.ATTR_EMAIL, hoteldao.ATTR_DESCRIPTION,
//				hoteldao.ATTR_IS_OPEN);
//		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificHotelData(keyMap, attrList));
//		eR = service.hotelQuery(new HashMap<>(), new ArrayList<>());
//					assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());
//		assertEquals(1, eR.calculateRecordNumber());
//		assertEquals(random, eR.getRecordValues(0).get(hoteldao.ATTR_ID));
//	}
//
//	public EntityResult getAllHotelsData() {
//		List<String> columnList = Arrays.asList(hoteldao.ATTR_ID, hoteldao.ATTR_NAME, hoteldao.ATTR_STREET,
//				hoteldao.ATTR_CITY, hoteldao.ATTR_CP, hoteldao.ATTR_STATE, hoteldao.ATTR_COUNTRY,
//				hoteldao.ATTR_PHONE, hoteldao.ATTR_PHONE, hoteldao.ATTR_EMAIL, hoteldao.ATTR_DESCRIPTION,
//				hoteldao.ATTR_IS_OPEN);
//		EntityResult er = new EntityResultMapImpl(columnList);
//		er.addRecord(new HashMap<String, Object>() {
//			{
//				put(hoteldao.ATTR_ID, 1);
//				put(hoteldao.ATTR_NAME, "Hotel 1");
//				put(hoteldao.ATTR_STREET, "Avenida Sin Nombre Nº 1");
//				put(hoteldao.ATTR_CITY, "Vigo");
//				put(hoteldao.ATTR_CP, "36211");
//				put(hoteldao.ATTR_STATE, "Galicia");
//				put(hoteldao.ATTR_COUNTRY, "Spain");
//				put(hoteldao.ATTR_PHONE, "+34 986 111 111");
//				put(hoteldao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//				put(hoteldao.ATTR_DESCRIPTION, "Hotel para pruebas unitarias");
//				put(hoteldao.ATTR_IS_OPEN, 1);
//			}
//		});
//		er.addRecord(new HashMap<String, Object>() {
//			{
//				put(hoteldao.ATTR_ID, 2);
//				put(hoteldao.ATTR_NAME, "Hotel 2");
//				put(hoteldao.ATTR_STREET, "Avenida Sin Nombre Nº 2");
//				put(hoteldao.ATTR_CITY, "Vigo");
//				put(hoteldao.ATTR_CP, "36211");
//				put(hoteldao.ATTR_STATE, "Galicia");
//				put(hoteldao.ATTR_COUNTRY, "Spain");
//				put(hoteldao.ATTR_PHONE, "+34 986 222 222");
//				put(hoteldao.ATTR_EMAIL, "hotel2@atomicHotels.com");
//				put(hoteldao.ATTR_DESCRIPTION, "Hotel para pruebas unitarias");
//				put(hoteldao.ATTR_IS_OPEN, 1);
//			}
//		});
//		er.addRecord(new HashMap<String, Object>() {
//			{
//				put(hoteldao.ATTR_ID, 3);
//				put(hoteldao.ATTR_NAME, "Hotel 3");
//				put(hoteldao.ATTR_STREET, "Avenida Sin Nombre Nº 3");
//				put(hoteldao.ATTR_CITY, "Vigo");
//				put(hoteldao.ATTR_CP, "36211");
//				put(hoteldao.ATTR_STATE, "Galicia");
//				put(hoteldao.ATTR_COUNTRY, "Spain");
//				put(hoteldao.ATTR_PHONE, "+34 986 333 333");
//				put(hoteldao.ATTR_EMAIL, "hotel3@atomicHotels.com");
//				put(hoteldao.ATTR_DESCRIPTION, "Hotel para pruebas unitarias");
//				put(hoteldao.ATTR_IS_OPEN, 0);
//			}
//		});
//		er.setCode(EntityResult.OPERATION_SUCCESSFUL);
//		er.setColumnSQLTypes(new HashMap<String, Number>() {
//			{
//				put(hoteldao.ATTR_ID, Types.INTEGER);
//				put(hoteldao.ATTR_NAME, Types.VARCHAR);
//				put(hoteldao.ATTR_STREET, Types.VARCHAR);
//				put(hoteldao.ATTR_CITY, Types.VARCHAR);
//				put(hoteldao.ATTR_CP, Types.VARCHAR);
//				put(hoteldao.ATTR_STATE, Types.VARCHAR);
//				put(hoteldao.ATTR_COUNTRY, Types.VARCHAR);
//				put(hoteldao.ATTR_PHONE, Types.VARCHAR);
//				put(hoteldao.ATTR_EMAIL, Types.VARCHAR);
//				put(hoteldao.ATTR_DESCRIPTION, Types.VARCHAR);
//				put(hoteldao.ATTR_IS_OPEN, Types.BINARY);
//			}
//		});
//		return er;
//	}
//
//	public EntityResult getSpecificHotelData(Map<String, Object> keyValues, List<String> attributes) {
//		EntityResult allData = this.getAllHotelsData();
//		int recordIndex = allData.getRecordIndex(keyValues);
//		HashMap<String, Object> recordValues = (HashMap) allData.getRecordValues(recordIndex);
//		List<String> columnList = Arrays.asList(hoteldao.ATTR_ID, hoteldao.ATTR_NAME, hoteldao.ATTR_STREET,
//				hoteldao.ATTR_CITY, hoteldao.ATTR_CP, hoteldao.ATTR_STATE, hoteldao.ATTR_COUNTRY,
//				hoteldao.ATTR_PHONE, hoteldao.ATTR_PHONE, hoteldao.ATTR_EMAIL, hoteldao.ATTR_DESCRIPTION,
//				hoteldao.ATTR_IS_OPEN);
//		EntityResult er = new EntityResultMapImpl(columnList);
//		if (recordValues != null) {
//			er.addRecord(recordValues);
//		}
//		er.setCode(EntityResult.OPERATION_SUCCESSFUL);
//		er.setColumnSQLTypes(new HashMap<String, Number>() {
//			{
//				put(hoteldao.ATTR_ID, Types.INTEGER);
//				put(hoteldao.ATTR_NAME, Types.VARCHAR);
//				put(hoteldao.ATTR_STREET, Types.VARCHAR);
//				put(hoteldao.ATTR_CITY, Types.VARCHAR);
//				put(hoteldao.ATTR_CP, Types.VARCHAR);
//				put(hoteldao.ATTR_STATE, Types.VARCHAR);
//				put(hoteldao.ATTR_COUNTRY, Types.VARCHAR);
//				put(hoteldao.ATTR_PHONE, Types.VARCHAR);
//				put(hoteldao.ATTR_EMAIL, Types.VARCHAR);
//				put(hoteldao.ATTR_DESCRIPTION, Types.VARCHAR);
//				put(hoteldao.ATTR_IS_OPEN, Types.BINARY);
//			}
//		});
//		return er;
//	}
//
//	List<Integer> randomIDGenerator() {
//		List<Integer> list = new ArrayList<>();
//		for (int i = 0; i < 5; i++) {
//			list.add(ThreadLocalRandom.current().nextInt(1, 4));
//		}
//		return list;
//	}
	
//    	@Test
//		@DisplayName("Missing Fields")
//		void when_unable_insert() {
//			when(daoHelper.insert(any(),anyMap())).thenThrow(MissingFieldsException.class);
//			Map<String, Object> attrMap = new HashMap<>() {{
//				put(hoteldao.ATTR_ID, 1);
////                put(hoteldao.ATTR_NAME, "Hotel 23");
////                put(hoteldao.ATTR_STREET, "Avenida Sin Nombre Nº 1");
//                put(hoteldao.ATTR_CITY, "Vigo");
//                put(hoteldao.ATTR_CP, "36211");
//                put(hoteldao.ATTR_STATE, "Galicia");
//                put(hoteldao.ATTR_COUNTRY, "Spain");
//                put(hoteldao.ATTR_PHONE, "+34 986 111 111");
//                put(hoteldao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//                put(hoteldao.ATTR_DESCRIPTION, "Faltan campos no nullables");
//                put(hoteldao.ATTR_IS_OPEN, 1);	
//			}};			
//			eR = service.hotelInsert(attrMap);
//						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//    		assertEquals(eR.getMessage(), ErrorMessage.CREATION_ERROR+e.getMessage());
//    	}
//	}

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
//					put(hoteldao.ATTR_ID, 1);
//					put(hoteldao.ATTR_NAME, "Hotel 1");
//					put(hoteldao.ATTR_STREET, "Avenida Sin Nombre Nº 1");
//					put(hoteldao.ATTR_CITY, "Vigo");
//					put(hoteldao.ATTR_CP, "36211");
//					put(hoteldao.ATTR_STATE, "Galicia");
//					put(hoteldao.ATTR_COUNTRY, "Spain");
//					put(hoteldao.ATTR_PHONE, "+34 986 111 111");
//					put(hoteldao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//					put(hoteldao.ATTR_DESCRIPTION, "Hotel a registrar");
//					put(hoteldao.ATTR_IS_OPEN, 1);
//				}
//			};
//			EntityResult resultado = new EntityResultMapImpl();
//			resultado.addRecord(attrMap);
//			resultado.setCode(EntityResult.OPERATION_SUCCESSFUL);
//			resultado.setMessage("Hotel registrado");
//			when(daoHelper.insert(any(), anyMap())).thenReturn(resultado);
//			eR = service.hotelInsert(attrMap);
//						assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());
//			assertEquals(eR.getMessage(), "Hotel registrado");
//		}
//
//		@Test
//		@DisplayName("Duplicated Key")
//		void when_already_exist() {
//			Map<String, Object> attrMap = new HashMap<>() {
//				{
//					put(hoteldao.ATTR_ID, 1);
//					put(hoteldao.ATTR_NAME, "Hotel 1");
//					put(hoteldao.ATTR_STREET, "Avenida Sin Nombre Nº 1");
//					put(hoteldao.ATTR_CITY, "Vigo");
//					put(hoteldao.ATTR_CP, "36211");
//					put(hoteldao.ATTR_STATE, "Galicia");
//					put(hoteldao.ATTR_COUNTRY, "Spain");
//					put(hoteldao.ATTR_PHONE, "+34 986 111 111");
//					put(hoteldao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//					put(hoteldao.ATTR_DESCRIPTION, "Este hotel ya estaría registrado");
//					put(hoteldao.ATTR_IS_OPEN, 1);
//				}
//			};
//			when(daoHelper.insert(any(), anyMap())).thenThrow(DuplicateKeyException.class);
//			eR = service.hotelInsert(attrMap);
//						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//			assertEquals(eR.getMessage(), ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
//		}
//
//		@Test
//		@DisplayName("Missing Fields")
//		void when_unable_insert() {
//			Map<String, Object> attrMap = new HashMap<>() {
//				{
//					put(hoteldao.ATTR_ID, 1);
//					put(hoteldao.ATTR_NAME, null);
//					put(hoteldao.ATTR_STREET, "Avenida Sin Nombre Nº 1");
//					put(hoteldao.ATTR_CITY, "Vigo");
//					put(hoteldao.ATTR_CP, "36211");
//					put(hoteldao.ATTR_STATE, "Galicia");
//					put(hoteldao.ATTR_COUNTRY, "Spain");
//					put(hoteldao.ATTR_PHONE, "+34 986 111 111");
//					put(hoteldao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//					put(hoteldao.ATTR_DESCRIPTION, "Faltan campos no nullables");
//					put(hoteldao.ATTR_IS_OPEN, 1);
//				}
//			};
//			// try (MockedStatic<ValidateFields> vf =
//			// Mockito.mockStatic(ValidateFields.class)) {
//			// vf.when(() -> ValidateFields.required(anyMap(),
//			// anyString())).thenThrow(MissingFieldsException.class);
//			eR = service.hotelInsert(attrMap);
//						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//			assertEquals(ErrorMessage.CREATION_ERROR + "El campo " + hoteldao.ATTR_NAME + " es nulo",
//					eR.getMessage());
//			// }
//
////			doThrow().when(ValidateFields.required(anyMap(), anyString())).thenThrow(MissingFieldsException.class);
////			when(daoHelper.insert(any(),anyMap())).thenThrow(new MissingFieldsException("El campo " + hoteldao.ATTR_NAME + " es nulo"));
////    		eR = service.hotelInsert(anyMap());
////						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
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
//					put(hoteldao.ATTR_ID, 1);
//				}
//			};
//			Map<String, Object> keyMap = new HashMap<>() {
//				{
//					put(hoteldao.ATTR_ID, 1);
//					put(hoteldao.ATTR_NAME, "Hotel 1 actualizado");
//					put(hoteldao.ATTR_STREET, "Avenida Sin Nombre Nº 1 actualizado");
//					put(hoteldao.ATTR_CITY, "Vigo actualizado");
//					put(hoteldao.ATTR_CP, "36211 actualizado");
//					put(hoteldao.ATTR_STATE, "Galicia");
//					put(hoteldao.ATTR_COUNTRY, "Spain");
//					put(hoteldao.ATTR_PHONE, "+34 986 111 111");
//					put(hoteldao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//					put(hoteldao.ATTR_DESCRIPTION, "Hotel actualizado");
//					put(hoteldao.ATTR_IS_OPEN, 0);
//				}
//			};
//			EntityResult resultado = new EntityResultMapImpl();
//			resultado.addRecord(keyMap);
//			resultado.setCode(EntityResult.OPERATION_SUCCESSFUL);
//			resultado.setMessage("Hotel actualizado");
//
//			when(daoHelper.update(any(), anyMap(), anyMap())).thenReturn(resultado);
//			eR = service.hotelUpdate(attrMap, keyMap);
//						assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());
//			assertEquals(eR.getMessage(), "Hotel actualizado");
//		}
//
//		@Test
//		@DisplayName("Duplicated Key")
//		void when_already_exist() {
//			Map<String, Object> attrMap = new HashMap<>() {
//				{
//					put(hoteldao.ATTR_ID, 2);
//				}
//			};
//			Map<String, Object> keyMap = new HashMap<>() {
//				{
//					put(hoteldao.ATTR_ID, 2);// ???
//					put(hoteldao.ATTR_NAME, "Hotel 1");// Este hotel ya existe
//					put(hoteldao.ATTR_STREET, "Avenida Sin Nombre Nº 1 actualizado");
//					put(hoteldao.ATTR_CITY, "Vigo actualizado");
//					put(hoteldao.ATTR_CP, "36211 actualizado");
//					put(hoteldao.ATTR_STATE, "Galicia");
//					put(hoteldao.ATTR_COUNTRY, "Spain");
//					put(hoteldao.ATTR_PHONE, "+34 986 111 111");
//					put(hoteldao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//					put(hoteldao.ATTR_DESCRIPTION, "Hotel actualizado");
//					put(hoteldao.ATTR_IS_OPEN, 1);
//				}
//			};
//			when(daoHelper.update(any(), anyMap(), anyMap())).thenThrow(DuplicateKeyException.class);
//			eR = service.hotelUpdate(attrMap, keyMap);
//						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//			assertEquals(eR.getMessage(), ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
//		}
}

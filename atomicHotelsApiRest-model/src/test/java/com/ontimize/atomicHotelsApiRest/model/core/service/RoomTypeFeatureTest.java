package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
import static org.mockito.Mockito.reset;
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
import org.mockito.Mockito;
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
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeFeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class RoomTypeFeatureTest {
	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@Spy
	ControlFields cf;
	
	@InjectMocks
	RoomTypeFeatureService service;

	@Autowired
	RoomTypeFeatureDao dao;
	
	EntityResult eR;
	
	@Nested
	@DisplayName("Test for Room type features queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class RoomTypeFeatureQuery {
		
		//RoomTypeFeatureQuery
				@Test
				@DisplayName("ControlFields usar reset()")
				void testRoomTypeFeatureQueryControlFieldsReset() {
					service.roomTypeFeatureQuery(TestingTools.getMapEmpty(), getColumsName());
					verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
				}

				@Test
				@DisplayName("ControlFields usar validate() map y list")
				void testRoomTypeFeatureQueryControlFieldsValidate() {
					service.roomTypeFeatureQuery(TestingTools.getMapEmpty(), getColumsName());
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
				void testRoomTypeFeatureQueryOK() {
					doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList());

					// válido: HashMap vacio (sin filtros)
					eR = service.roomTypeFeatureQuery(TestingTools.getMapEmpty(), getColumsName());
					assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

					// válido: HashMap con filtro que existe (sin filtros)
					eR = service.roomTypeFeatureQuery(getMapId(), getColumsName());
					assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

				}

				@Test
				@DisplayName("Valores de entrada NO válidos")
				void testRoomTypeFeatureQueryKO() {
					try {
						// lanzamos todas las excepciones de Validate para comprobar que están bien recogidas.
						
						doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
						eR = service.roomTypeFeatureQuery(TestingTools.getMapEmpty(), getColumsName());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

						doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
						eR = service.roomTypeFeatureQuery(TestingTools.getMapEmpty(), getColumsName());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

						doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
						eR = service.roomTypeFeatureQuery(TestingTools.getMapEmpty(), getColumsName());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

						doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
						eR = service.roomTypeFeatureQuery(TestingTools.getMapEmpty(), getColumsName());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

						doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
						eR = service.roomTypeFeatureQuery(TestingTools.getMapEmpty(), getColumsName());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

					} catch (Exception e) {
						e.printStackTrace();
						fail("excepción no capturada: " + e.getMessage());
					}

				}
				
			}


	@Nested
	@DisplayName("Test for Room type features inserts")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class RoomTypeFeatureInsert {
		
		@Test
		@DisplayName("ControlFields usar reset()")
		void testRoomTypeFeatureControlFieldsReset() {
			service.roomTypeFeatureInsert(TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}
	
		@Test
		@DisplayName("ControlFields usar validate() map ")
		void testRoomTypeFeatureInsertControlFieldsValidate() {
			service.roomTypeFeatureInsert(TestingTools.getMapEmpty());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
	
			}
		}
	
		@Test
		@DisplayName("Valores de entrada válidos")
		void testRoomTypeFeatureInsertOK() {
			doReturn(new EntityResultMapImpl()).when(daoHelper).insert(any(), anyMap());
	
			// válido: HashMap campos mínimos
			eR = service.roomTypeFeatureInsert(getMapRequiredInsert());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());
	
			// válido: HashMap campos mínimos y mas
			eR = service.roomTypeFeatureInsert(getMapRequiredInsertExtended());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());
	
		}
	
		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testRoomTypeFeatureInsertKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien recogidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.roomTypeFeatureInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
	
				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.roomTypeFeatureInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
	
				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.roomTypeFeatureInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
	
				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.roomTypeFeatureInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
	
				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.roomTypeFeatureInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
	
				reset(cf);
				// extra para controlar required:
				eR = service.roomTypeFeatureInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				System.out.println(eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());
	
				// extra para controlar restricted:
				eR = service.roomTypeFeatureInsert(getMapRequiredInsertExtendedWidthRestricted());
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
@DisplayName("Test for Room type feature deletes")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoomTypeFeatureDeleteDelete {
//TODO PENDIENTE TERMINAR DELETE
	@Test
	@DisplayName("ControlFields usar reset()")
	void testRoomTypeFeatureDeleteControlFieldsReset() {
		service.roomTypeFeatureDelete(TestingTools.getMapEmpty());
		verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
	}

	@Test
	@DisplayName("ControlFields usar validate() map ")
	void testRoomTypeFeatureDeleteControlFieldsValidate() {
		service.roomTypeFeatureDelete(TestingTools.getMapEmpty());
		try {
			verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
		} catch (Exception e) {
			e.printStackTrace();
			fail("excepción no capturada: " + e.getMessage());
		}
	}

	@Test
	@DisplayName("Valores de entrada válidos")
	void testRoomTypeFeatureDeleteOK() {
		
		doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(),anyList());
		doReturn(new EntityResultMapImpl()).when(daoHelper).delete(any(), anyMap());

		// válido: HashMap campo único y exclusivo
		eR = service.roomTypeFeatureDelete(getMapId());
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());
	}
	
	@Test
	@DisplayName("Valores Subcontulta Error")
	void testRoomTypeFeatureDeleteSubQueryKO() {
		doReturn(new EntityResultWrong()).when(daoHelper).query(any(), anyMap(),anyList());
//		doReturn(new EntityResultMapImpl()).when(daoHelper).delete(any(), anyMap());
		
		eR = service.roomTypeFeatureDelete(getMapId());
		assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
		assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
	}
	
	@Test
	@DisplayName("Valores Subconsulta 0 resultados")
	void testRoomTypeFeatureDeleteSubQueryNoResults() {
		doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(),anyList());
//		doReturn(new EntityResultMapImpl()).when(daoHelper).delete(any(), anyMap());
		
		eR = service.roomTypeFeatureDelete(getMapId());
		assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
		assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
	}
	
	@Test
	@DisplayName("Valores de entrada NO válidos")
	void testRoomTypeFeatureDeleteKO() {
		try {
			// lanzamos todas las excepciones de Validate para comprobar que están bien recogidas.
			doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
			eR = service.roomTypeFeatureDelete(TestingTools.getMapEmpty());
			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
			assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
			eR = service.roomTypeFeatureDelete(TestingTools.getMapEmpty());
			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
			assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
			eR = service.roomTypeFeatureDelete(TestingTools.getMapEmpty());
			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
			assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
			eR = service.roomTypeFeatureDelete(TestingTools.getMapEmpty());
			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
			assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
			eR = service.roomTypeFeatureDelete(TestingTools.getMapEmpty());
			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
			assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

/*
			// lanzamos todas las excepciones de SQL para comprobar que están bien recogidas.
			doThrow(DataIntegrityViolationException.class).when(cf).validate(anyMap());
			eR = service.roomTypeFeatureDelete(TestingTools.getMapEmpty());
			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
			assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
*/
			reset(cf); //para quitar doThrow anterior
			// extra para controlar required:
			eR = service.roomTypeFeatureDelete(TestingTools.getMapEmpty());
			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
			assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
			assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

			// extra para controlar restricted:
			eR = service.roomTypeFeatureDelete(getMapRequiredDeletetExtendedWidthRestricted());
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
				put(dao.ATTR_ROOM_ID, 1);
				put(dao.ATTR_FEATURE_ID, 1);
				
			}
		};
	}
	
	Map<String, Object> getMapUpdate() {
		return getMapRequiredInsert();
	}
	
	
	
	Map<String, Object> getMapRequiredInsertExtended() {
	
		return new HashMap<>() {
			{
				put(dao.ATTR_ROOM_ID, 1);
				put(dao.ATTR_FEATURE_ID, 1);
			}
		};
	}
	
	Map<String, Object> getMapRequiredInsertExtendedWidthRestricted() {
	
		return new HashMap<>() {
			{
				put(dao.ATTR_ROOM_ID, "1");
				put(dao.ATTR_FEATURE_ID, "1");
			}
		};
	}
	
	Map<String, Object> getMapRequiredDeletetExtendedWidthRestricted() {
		return getMapRequiredInsertExtendedWidthRestricted();
	}
	
	HashMap<String, Object> getMapId() {
		HashMap<String, Object> filters = new HashMap<>() {
			{
				put(dao.ATTR_ROOM_ID, 1);
				put(dao.ATTR_FEATURE_ID, 1);
			}
		};
		return filters;
	};
	
	List<String> getColumsName() {
		List<String> columns = new ArrayList<>() {
			{
				add(dao.ATTR_FEATURE_ID);
			}
		};
		return columns;
	}
}
		

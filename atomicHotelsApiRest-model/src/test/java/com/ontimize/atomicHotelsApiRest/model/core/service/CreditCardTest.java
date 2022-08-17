package com.ontimize.atomicHotelsApiRest.model.core.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
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
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CreditCardDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;


@ExtendWith(MockitoExtension.class)
class CreditCardTest {

	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@Spy
	ControlFields cf;
	
	@InjectMocks
	CreditCardService service;

	@Autowired
	CreditCardDao dao;

	EntityResult eR;
	
	@Nested
	@DisplayName("Test for creditCard queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class CreditCardQuery { 
		//creditCardQuery
		@Test
		@DisplayName("ControlFields usar reset()")
		void testCreditCardQueryControlFieldsReset() {
			service.creditCardQuery(TestingTools.getMapEmpty(), getColumsName());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testCreditCardQueryControlFieldsValidate() {
			
			try {
				doNothing().when(cf).restricPermissions(anyMap());
				service.creditCardQuery(TestingTools.getMapEmpty(), getColumsName());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyList());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testCreditCardQueryOK() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList());

			// válido: HashMap vacio (sin filtros)
			eR = service.creditCardQuery(TestingTools.getMapEmpty(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

			// válido: HashMap con filtro que existe (sin filtros)
			eR = service.creditCardQuery(getMapId(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testCreditCardKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.creditCardQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.creditCardQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.creditCardQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.creditCardQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.creditCardQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}

		}
		
	}
	
	@Nested
	@DisplayName("Test for Credit Cards inserts")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class CreditCardInsert {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testhotelInsertControlFieldsReset() {
			service.creditCardInsert(TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map ")
		void testCreditCardInsertControlFieldsValidate() {
			service.creditCardInsert(TestingTools.getMapEmpty());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());

			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testCreditCardInsertOK() {
			
			try {
				doNothing().when(cf).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(new EntityResultMapImpl()).when(daoHelper).insert(any(), anyMap());

			// válido: HashMap campos mínimos
			eR = service.creditCardInsert(getMapRequiredInsert());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

			// válido: HashMap campos mínimos y mas
			eR = service.creditCardInsert(getMapRequiredInsertExtended());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testCreditCardInsertKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.creditCardInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.creditCardInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.creditCardInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.creditCardInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.creditCardInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(cf);
				// extra para controlar required:
				eR = service.creditCardInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				System.out.println(eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.creditCardInsert(getMapRequiredInsertExtendedWidthRestricted());
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
	@DisplayName("Test for CreditCard deletes")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class CreditCardDelete {
		@BeforeTestMethod
		public void buildSpy() {
			cf = spy(cf);
		}
		@Test
		@DisplayName("ControlFields usar reset()")
		void testhotelDeleteControlFieldsReset() {
			service.creditCardDelete(TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map ")
		void testHotelDeleteControlFieldsValidate() {
			service.creditCardDelete(TestingTools.getMapEmpty());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());

			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testhotelDeleteOK() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}

			doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(),anyList());
			doReturn(new EntityResultMapImpl()).when(daoHelper).delete(any(), anyMap());

			// válido: HashMap campo único y exclusivo
			eR = service.creditCardDelete(getMapId());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());
 
		}
		
		@Test
		@DisplayName("Valores Subcontulta Error")
		void testhotelDeleteSubQueryKO() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(new EntityResultWrong()).when(daoHelper).query(any(), anyMap(),anyList());
//			doReturn(new EntityResultMapImpl()).when(daoHelper).delete(any(), anyMap());
			
			// 
			eR = service.creditCardDelete(getMapId());
			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
			assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
		}
		
		@Test
		@DisplayName("Valores Subconsultta 0 resultados")
		void testhotelDeleteSubQueryNoResults() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(),anyList());
//			doReturn(new EntityResultMapImpl()).when(daoHelper).delete(any(), anyMap());
			
			// 
			eR = service.creditCardDelete(getMapId());
			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
			assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
		}
		
		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testhotelDeleteKO() {
			try {
				try {
					doNothing().when(cf).restricPermissions(anyMap());
				} catch (Exception e) {
					e.printStackTrace();
					fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
				}
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.creditCardDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.creditCardDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.creditCardDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.creditCardDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.creditCardDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				// lanzamos todas las excepciones de SQL para comprobar que están bien
				// recogidas.
				doThrow(DataIntegrityViolationException.class).when(cf).validate(anyMap());
				eR = service.creditCardDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG , eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(cf); //para quitar doThrow anterior
				// extra para controlar required:
				eR = service.creditCardDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.creditCardDelete(getMapRequiredDeletetExtendedWidthRestricted());
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
				put(dao.ATTR_NUMBER, 12345678901234L);
				put(dao.ATTR_DATE_EXPIRY,"2023-01-04");
			
			}
		};
	}

	Map<String, Object> getMapUpdate() {
		return getMapRequiredInsert();
	}

	

	Map<String, Object> getMapRequiredInsertExtended() {

		return new HashMap<>() {
			{
				put(dao.ATTR_NUMBER, 12345678901234L);
				put(dao.ATTR_DATE_EXPIRY,"2023-01-04");
			}
		};
	}
	
	Map<String, Object> getMapRequiredInsertExtendedWidthRestricted() {

		return new HashMap<>() {
			{
				put(dao.ATTR_ID, "1");
				put(dao.ATTR_NUMBER, "12345678901234");
				put(dao.ATTR_DATE_EXPIRY,"2023-01-04");
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

	List<String> getColumsName() {
		List<String> columns = new ArrayList<>() {
			{
				add(dao.ATTR_NUMBER);
			}
		};
		return columns;
	}
	
	
	
	
}
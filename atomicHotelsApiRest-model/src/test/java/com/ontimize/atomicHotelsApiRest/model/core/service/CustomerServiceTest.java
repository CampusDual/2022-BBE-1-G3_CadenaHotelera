package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
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

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.db.Entity;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@Spy
	ControlFields cf;

	@InjectMocks
	CustomerService service;

	@Autowired
	CustomerDao dao;

	EntityResult eR;

	@Nested
	@DisplayName("Test for Customers queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class CustomerQuery {
		@Test
		@DisplayName("ControlFields usar reset()")
		void testCustomerQueryControlFieldsReset() {
			service.customerQuery(TestingTools.getMapEmpty(), getColumsName());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testCustomerQueryControlFieldsValidate() {
			service.customerQuery(TestingTools.getMapEmpty(), getColumsName());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyList());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testCustomerQueryOK() {
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());

			// válido: HashMap vacio (sin filtros)
			eR = service.customerQuery(TestingTools.getMapEmpty(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

			// válido: HashMap con filtro que existe
			eR = service.customerQuery(getMapId(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testCustomerQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.customerQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.customerQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.customerQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.customerQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.customerQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}

		}

	}

	@Nested
	@DisplayName("Test for Customers evaluation booleans")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class CustomerQueryBooleans {
		@Test
		@DisplayName("ValidBookingHolder - Valores de salida válidos")
		void testIsCustomerValidBookingHolderOK() {
			boolean resultado;

			try {
				doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(), anyList(),
						anyString());
				resultado = service.isCustomerValidBookingHolder(999);
				assertTrue(resultado);

				doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
				resultado = service.isCustomerValidBookingHolder(999);
				assertFalse(resultado);

			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}

		@Test
		@DisplayName("ValidBookingHolder - Valores de salida NO válidos - error consulta")
		void testIsCustomerValidBookingHolderKO() {
			boolean resultado;
			try {
				doReturn(new EntityResultWrong()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
				assertThrows(EntityResultRequiredException.class, () -> service.isCustomerValidBookingHolder(999));

				eR = TestingTools.getEntityOneRecord();
				eR.setCode(EntityResult.OPERATION_WRONG);
				doReturn(eR).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
				assertThrows(EntityResultRequiredException.class, () -> service.isCustomerValidBookingHolder(999));

			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}

		@DisplayName("BlockedQuery - Valores de salida válidos")
		void testIsCustomerBlockedQueryOK() {
			boolean resultado;

			try {
				doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(), anyList(),
						anyString());
				resultado = service.isCustomerBlockeddQuery(999);
				assertFalse(resultado);

				doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
				resultado = service.isCustomerBlockeddQuery(999);
				assertTrue(resultado);

			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}

		@Test
		@DisplayName("BlockedQuery - Valores de salida NO válidos - error consulta")
		void testIsCustomerBlockedQueryKO() {
			boolean resultado;
			try {
				doReturn(new EntityResultWrong()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
				assertThrows(EntityResultRequiredException.class, () -> service.isCustomerBlockeddQuery(999));

				eR = TestingTools.getEntityOneRecord();
				eR.setCode(EntityResult.OPERATION_WRONG);
				doReturn(eR).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
				assertThrows(EntityResultRequiredException.class, () -> service.isCustomerBlockeddQuery(999));

			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}

	}

	@Nested
	@DisplayName("Test for Customer inserts")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class CustomerInsert {

		// BusinessCustomer
		@Test
		@DisplayName("Bussiness - ControlFields usar reset()")
		void testcustomerInsertControlFieldsReset() {
			service.businessCustomerInsert(TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("Bussiness - ControlFields usar validate() map ")
		void testBusinessCustomerInsertControlFieldsValidate() {
			service.businessCustomerInsert(TestingTools.getMapEmpty());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}

		@Test
		@DisplayName("Bussiness - Valores de entrada válidos")
		void testBusinesscustomerInsertOK() {
			doReturn(new EntityResultMapImpl()).when(daoHelper).insert(any(), anyMap());

			// válido: HashMap campos mínimos
			eR = service.businessCustomerInsert(getMapRequiredBusinessInsert());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

			// válido: HashMap campos mínimos y mas
			eR = service.businessCustomerInsert(getMapRequiredBusinessInsertExtended());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());
		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testBusinessCustomerInsertKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.businessCustomerInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.businessCustomerInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.businessCustomerInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.businessCustomerInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.businessCustomerInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(cf);
				// extra para controlar required:
				eR = service.businessCustomerInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				System.out.println(eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.businessCustomerInsert(getMapRequiredBusinessInsertExtendedWidthRestricted());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				System.out.println(eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}

		}
	}
	// datos entrada

	Map<String, Object> getMapRequiredBusinessInsert() {
		return new HashMap<>() {
			{
				put(dao.ATTR_NAME, "Citroen");
				put(dao.ATTR_PHONE, "986123456");
				put(dao.ATTR_COUNTRY, "EU");
				put(dao.ATTR_VAT_NUMBER, "B-14556699");
			}
		};
	}

	Map<String, Object> getMapUpdate() {
		return getMapRequiredBusinessInsert();
	}

	Map<String, Object> getMapRequiredBusinessInsertExtended() {
		Map<String, Object> m = getMapRequiredBusinessInsert();
		m.put(dao.ATTR_CITY, "New York");
		return m;
	}

	Map<String, Object> getMapRequiredBusinessInsertExtendedWidthRestricted() {
		Map<String, Object> m = getMapRequiredBusinessInsertExtended();
		m.put(dao.ATTR_ID, 2);
		m.put(dao.ATTR_SURNAME, "Rodriguez");
		m.put(dao.ATTR_BIRTH_DATE, "1993-01-01");
		m.put(dao.ATTR_IDEN_DOC, "55444333-H");
		return m;
	}

	Map<String, Object> getMapRequiredDeletetExtendedWidthRestricted() {
		return getMapRequiredBusinessInsertExtendedWidthRestricted();
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
				add(dao.ATTR_NAME);
			}
		};
		return columns;
	}
}

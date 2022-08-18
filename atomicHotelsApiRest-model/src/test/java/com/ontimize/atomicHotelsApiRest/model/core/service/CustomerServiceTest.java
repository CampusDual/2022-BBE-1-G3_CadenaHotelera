package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
	
	@BeforeEach
	void passRestrictPermissions() {
		try {
			reset(cf);
			doNothing().when(cf).restricPermissions(anyMap());
		} catch (Exception e) {
			e.printStackTrace();
			fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
		}
	}

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
		//mailAgreementQuery
		@Test
		@DisplayName("ControlFields usar reset()")
		void testmailAgreementQueryControlFieldsReset() {
			service.mailAgreementQuery(TestingTools.getMapEmpty(), getColumsName());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testmailAgreementQueryControlFieldsValidate() {
			service.mailAgreementQuery(TestingTools.getMapEmpty(), getColumsName());
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
		void testmailAgreementQueryOK() {
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());

			// válido: HashMap vacio (sin filtros)
			eR = service.mailAgreementQuery(TestingTools.getMapEmpty(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

			// válido: HashMap con filtro que existe
			eR = service.mailAgreementQuery(getMapId(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testmailAgreementQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.mailAgreementQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.mailAgreementQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.mailAgreementQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.mailAgreementQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.mailAgreementQuery(TestingTools.getMapEmpty(), getColumsName());
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

		@Test
		@DisplayName("BlockedQuery - Valores de salida válidos")
		void testIsCustomerBlockedQueryOK() {
			boolean resultado;

			try {
				doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(), anyList(),
						anyString());
				resultado = service.isCustomerBlockedQuery(999);
				assertTrue(resultado);

				doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
				resultado = service.isCustomerBlockedQuery(999);
				assertFalse(resultado);

			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}

		@Test
		@DisplayName("BlockedQuery - Valores de salida NO válidos - error consulta")
		void testIsCustomerBlockedQueryKO() {
			boolean resultado;
			reset(cf);//para omitir el @beforeAll
			try {
				doReturn(new EntityResultWrong()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
				assertThrows(EntityResultRequiredException.class, () -> service.isCustomerBlockedQuery(999));

				eR = TestingTools.getEntityOneRecord();
				eR.setCode(EntityResult.OPERATION_WRONG);
				doReturn(eR).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
				assertThrows(EntityResultRequiredException.class, () -> service.isCustomerBlockedQuery(999));

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
		void testCustomerBusinessInsertControlFieldsReset() {
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
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
			doReturn(new EntityResultMapImpl()).when(daoHelper).insert(any(), anyMap());
			try {
				doNothing().when(cf).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
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

				doThrow(DuplicateKeyException.class).when(cf).validate(anyMap());
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
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.businessCustomerInsert(getMapRequiredBusinessInsertExtendedWidthRestricted());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar valor existente:
				doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(), anyList(),
						anyString());
//				doReturn(new EntityResultMapImpl()).when(daoHelper).insert(any(), anyMap());
				try {
					doNothing().when(cf).validate(anyMap());
				} catch (Exception e) {
					e.printStackTrace();
					fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
				}
				eR = service.businessCustomerInsert(getMapRequiredBusinessInsert());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}

		}

		// RegularCustomer
		@Test
		@DisplayName("Bussiness - ControlFields usar reset()")
		void testcustomerInsertControlFieldsReset() {
			reset(cf);//para omitir el @beforeAll
			service.regularCustomerInsert(TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("Bussiness - ControlFields usar validate() map ")
		void testRegularCustomerInsertControlFieldsValidate() {
			service.regularCustomerInsert(TestingTools.getMapEmpty());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}

		@Test
		@DisplayName("Bussiness - Valores de entrada válidos")
		void testRegularcustomerInsertOK() {
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
			doReturn(new EntityResultMapImpl()).when(daoHelper).insert(any(), anyMap());
			try {
				doNothing().when(cf).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			// válido: HashMap campos mínimos
			eR = service.regularCustomerInsert(getMapRequiredRegularInsert());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

			// válido: HashMap campos mínimos y mas
			eR = service.regularCustomerInsert(getMapRequiredRegularInsertExtended());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());
		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testRegularCustomerInsertKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.regularCustomerInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.regularCustomerInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.regularCustomerInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.regularCustomerInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(DuplicateKeyException.class).when(cf).validate(anyMap());
				eR = service.regularCustomerInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.regularCustomerInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(cf);
				// extra para controlar required:
				eR = service.regularCustomerInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.regularCustomerInsert(getMapRequiredRegularInsertExtendedWidthRestricted());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar valor existente:
				doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(), anyList(),
						anyString());
//						doReturn(new EntityResultMapImpl()).when(daoHelper).insert(any(), anyMap());
				doNothing().when(cf).validate(anyMap());

				eR = service.regularCustomerInsert(getMapRequiredRegularInsert());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}

		}
	}

	@Nested
	@DisplayName("Test for Customer updates")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class CustomerUpdate {

		//Business customer
		@Test
		@DisplayName("Business - ControlFields usar reset()")
		void testCustomerBusinessUpdateControlFieldsReset() {
			service.customerBusinessUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("Business - ControlFields usar validate() map ")
		void testCustomerBusinessUpdateControlFieldsValidate() {
			service.customerBusinessUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}

		@Test
		@DisplayName("Business - Valores de entrada válidos")
		void testCustomerBusinessUpdateOK() {
			doReturn(getERWithVatNumber()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
			doReturn(new EntityResultMapImpl()).when(daoHelper).update(any(), anyMap(), anyMap());
			try {
				doNothing().when(cf).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}

			eR = service.customerBusinessUpdate(getMapUpdate(), getMapId());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Business - Valores de entrada NO válidos")
		void testCustomerBusinessUpdateKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.customerBusinessUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.customerBusinessUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.customerBusinessUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.customerBusinessUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.customerBusinessUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				// lanzamos todas las excepciones de SQL para comprobar que están bien
				// recojidas.
				doThrow(DuplicateKeyException.class).when(cf).validate(anyMap());
				eR = service.customerBusinessUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(DataIntegrityViolationException.class).when(cf).validate(anyMap());
				eR = service.customerBusinessUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(cf);
				// extra para controlar required:
				eR = service.customerBusinessUpdate(getMapUpdate(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.CREATION_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.customerBusinessUpdate(getMapId(), getMapId());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.CREATION_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar errores internos:
				doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
				doNothing().when(cf).validate(anyMap());
				eR = service.customerBusinessUpdate(getMapUpdate(), getMapId());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
				doNothing().when(cf).validate(anyMap());
				eR = service.customerBusinessUpdate(getMapUpdate(), getMapId());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				
				doReturn(getERWithVatNumber()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
				doReturn(TestingTools.getEntitySuccesfulWithMsg()).when(daoHelper).update(any(), anyMap(), anyMap());
				doNothing().when(cf).validate(anyMap());
				eR = service.customerBusinessUpdate(getMapUpdate(), getMapId());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}

		}
		
		//Regular customer
				@Test
				@DisplayName("Regular - ControlFields usar reset()")
				void testCustomerRegularUpdateControlFieldsReset() {
					service.customerRegularUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
					verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
				}

				@Test
				@DisplayName("Regular - ControlFields usar validate() map ")
				void testCustomerRegularUpdateControlFieldsValidate() {
					reset(cf);//para omitir el @beforeAll
					service.customerRegularUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
					try {
						verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
					} catch (Exception e) {
						e.printStackTrace();
						fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
					}
				}

				@Test
				@DisplayName("Regular - Valores de entrada válidos")
				void testCustomerRegularUpdateOK() {
					doReturn(getERWithIdenDoc()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
					doReturn(new EntityResultMapImpl()).when(daoHelper).update(any(), anyMap(), anyMap());
					try {
						doNothing().when(cf).validate(anyMap());
					} catch (Exception e) {
						e.printStackTrace();
						fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
					}

					eR = service.customerRegularUpdate(getMapUpdate(), getMapId());
					assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

				}

				@Test
				@DisplayName("Regular - Valores de entrada NO válidos")
				void testCustomerRegularUpdateKO() {
					try {
						// lanzamos todas las excepciones de Validate para comprobar que están bien
						// recojidas.
						doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
						eR = service.customerRegularUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

						doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
						eR = service.customerRegularUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

						doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
						eR = service.customerRegularUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

						doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
						eR = service.customerRegularUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

						doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
						eR = service.customerRegularUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

						// lanzamos todas las excepciones de SQL para comprobar que están bien
						// recojidas.
						doThrow(DuplicateKeyException.class).when(cf).validate(anyMap());
						eR = service.customerRegularUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

						doThrow(DataIntegrityViolationException.class).when(cf).validate(anyMap());
						eR = service.customerRegularUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

						reset(cf);
						// extra para controlar required:
						eR = service.customerRegularUpdate(getMapUpdate(), TestingTools.getMapEmpty());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.CREATION_ERROR, eR.getMessage(), eR.getMessage());
						assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

						// extra para controlar restricted:
						eR = service.customerRegularUpdate(getMapId(), getMapId());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.CREATION_ERROR, eR.getMessage(), eR.getMessage());
						assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

						// extra para controlar errores internos:
						doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
						doNothing().when(cf).validate(anyMap());
						eR = service.customerRegularUpdate(getMapUpdate(), getMapId());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

						doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
						doNothing().when(cf).validate(anyMap());
						eR = service.customerRegularUpdate(getMapUpdate(), getMapId());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
						
						doReturn(getERWithIdenDoc()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
						doReturn(TestingTools.getEntitySuccesfulWithMsg()).when(daoHelper).update(any(), anyMap(), anyMap());
						doNothing().when(cf).validate(anyMap());
						eR = service.customerRegularUpdate(getMapUpdate(), getMapId());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

					} catch (Exception e) {
						e.printStackTrace();
						fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
					}

				}
	}
	
	@Nested
	@DisplayName("Test for CustomerCancel updates")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class CustomerCancelUpdate {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testcustomerCancelUpdateControlFieldsReset() {
			service.customerCancelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map ")
		void testCustomerUpdateControlFieldsValidate() {
			reset(cf);//para omitir el @beforeAll
			service.customerCancelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testcustomerCancelUpdateOK() {
			doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(), anyList(), eq("queryBasic"));
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(), eq("queryBloquedCustomer"));
			doReturn(new EntityResultMapImpl()).when(daoHelper).update(any(), anyMap(), anyMap());
			try {
				doNothing().when(cf).validate(anyMap());									
			} catch (Exception e) { 
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			// válido: HashMap campos y filtros
			eR = service.customerCancelUpdate(getMapActionCancel(), getMapId());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testcustomerCancelUpdateKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.customerCancelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.customerCancelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.customerCancelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.customerCancelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.customerCancelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				// lanzamos todas las excepciones de SQL para comprobar que están bien
				// recojidas.
				doThrow(DuplicateKeyException.class).when(cf).validate(anyMap());
				eR = service.customerCancelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(DataIntegrityViolationException.class).when(cf).validate(anyMap());
				eR = service.customerCancelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(cf);
				// extra para controlar required:
				eR = service.customerCancelUpdate(getMapUpdate(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.CREATION_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.customerCancelUpdate(getMapId(), getMapId());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.CREATION_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// errores internos
				/**
					doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(), anyList(), eq("queryBasic"));
					doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(), eq("queryBloquedCustomer"));
					doReturn(new EntityResultMapImpl()).when(daoHelper).update(any(), anyMap(), anyMap());
				
				*/
				doNothing().when(cf).validate(anyMap());
				
				doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(), anyList(), eq("queryBasic"));
				doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(), eq("queryBloquedCustomer"));
				doReturn(TestingTools.getEntitySuccesfulWithMsg()).when(daoHelper).update(any(), anyMap(), anyMap());
				eR = service.customerCancelUpdate(getMapUpdate(), getMapId());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(daoHelper);
				doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(), anyList(), eq("queryBasic"));
				doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(), anyList(), eq("queryBloquedCustomer"));				doNothing().when(cf).validate(anyMap());
				eR = service.customerCancelUpdate(getMapUpdate(), getMapId());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				
				reset(daoHelper);
				doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(), eq("queryBasic"));			
				eR = service.customerCancelUpdate(getMapUpdate(), getMapId());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}

		}
	}
	
	@Nested
	@DisplayName("Test for Customer deletes")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class CustomerDelete {
		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testhotelDeleteOK() {		
			reset(cf);//para omitir el @beforeAll
			eR = service.customerDelete(TestingTools.getMapEmpty());
			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
			assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

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
	Map<String, Object> getMapActionCancel() {
		return new HashMap<>() {
			{
				put(dao.NON_ATTR_ACTION, "CANCEL");				
			}
		};
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

	Map<String, Object> getMapRequiredRegularInsert() {
		return new HashMap<>() {
			{
				put(dao.ATTR_NAME, "Ana");
				put(dao.ATTR_SURNAME, "Rodríguez");
				put(dao.ATTR_PHONE, "986123456");
				put(dao.ATTR_COUNTRY, "ES");
				put(dao.ATTR_IDEN_DOC, "55333222J");
			}
		};
	}

	Map<String, Object> getMapRequiredRegularInsertExtended() {
		Map<String, Object> m = getMapRequiredRegularInsert();
		m.put(dao.ATTR_CITY, "New York");
		return m;
	}

	Map<String, Object> getMapRequiredRegularInsertExtendedWidthRestricted() {
		Map<String, Object> m = getMapRequiredRegularInsertExtended();
		m.put(dao.ATTR_ID, 2);
		m.put(dao.ATTR_VAT_NUMBER, "B36111333");
		return m;
	}

	Map<String, Object> getMapUpdate() {
		return getMapRequiredBusinessInsert();
	}

//	Map<String, Object> getMapRequiredDeletetExtendedWidthRestricted() {
//		return getMapRequiredBusinessInsertExtendedWidthRestricted();
//	}

	Map<String, Object> getMapId() {
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

	EntityResult getERWithVatNumber() {
		EntityResult resultado = new EntityResultMapImpl();
		resultado.addRecord(new HashMap() {
			{
				put(CustomerDao.ATTR_VAT_NUMBER, "AAAAAAAA");
			}
		});
		return resultado;
	}

	EntityResult getERWithIdenDoc() {
		EntityResult resultado = new EntityResultMapImpl();
		resultado.addRecord(new HashMap() {
			{
				put(CustomerDao.ATTR_IDEN_DOC, "AAAAAAAA");
			}
		});
		return resultado;
	}
}

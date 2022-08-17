package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;

import com.ontimize.atomicHotelsApiRest.model.core.dao.BillDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.DepartmentDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class BillServiceTest {

	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@Spy
	ControlFields cf;

	@InjectMocks
	BillService service;

	@Autowired
	BillDao dao;

	@Autowired
	HotelDao hotelDao;

	@Autowired
	DepartmentDao departmentDao;

	@Mock
	HotelService hotelServiceMock;

	@Mock
	DepartmentService departmentServiceMock;

	EntityResult eR;

	@Nested
	@DisplayName("Test for Bill queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class billQuery {

		// billQuery
		@Test
		@DisplayName("ControlFields usar reset()")
		void testBillQueryControlFieldsReset() {
			service.billQuery(TestingTools.getMapEmpty(), getColumsName());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testBillQueryControlFieldsValidate() {

			try {
				doNothing().when(cf).restricPermissions(anyMap());
				service.billQuery(TestingTools.getMapEmpty(), getColumsName());

				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyList());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testBillQueryOK() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList());

			// válido: HashMap vacio (sin filtros)
			eR = service.billQuery(TestingTools.getMapEmpty(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

			// válido: HashMap con filtro que existe (sin filtros)
			eR = service.billQuery(getMapId(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testBillQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recogidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.billQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.billQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.billQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.billQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.billQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}

		}

	}

	@Nested
	@DisplayName("Test for Bill inserts")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class billInsert {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testBillInsertControlFieldsReset() throws OntimizeJEERuntimeException, MissingFieldsException {
			service.billInsert(TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map ")
		void testBillInsertControlFieldsValidate() throws OntimizeJEERuntimeException, MissingFieldsException {
			service.billInsert(TestingTools.getMapEmpty());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());

			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testBillInsertOK() throws OntimizeJEERuntimeException, MissingFieldsException {
			try {
				doNothing().when(cf).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(new EntityResultMapImpl()).when(daoHelper).insert(any(), anyMap());

			// válido: HashMap campos mínimos
			eR = service.billInsert(getMapRequiredInsert());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

			// válido: HashMap campos mínimos y mas
			eR = service.billInsert(getMapRequiredInsertExtended());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testBillInsertKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recogidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.billInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.billInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.billInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.billInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.billInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(cf);
				// extra para controlar required:
				eR = service.billInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				System.out.println(eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.billInsert(getMapRequiredInsertExtendedWidthRestricted());
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

	@Nested
	@DisplayName("Test for Bill updates")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class billUpdate {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testBillUpdateControlFieldsReset() {
			service.billUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map ")
		void testBillUpdateControlFieldsValidate() {
			service.billUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testBillUpdateOK() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
				doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(), anyList());
				doReturn(new EntityResultMapImpl()).when(daoHelper).update(any(), anyMap(), anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			// válido: HashMap campos y filtros
			eR = service.billUpdate(getMapUpdate(), getMapId());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testBillServiceExtraUpdateKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recogidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.billUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.billUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.billUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.billUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.billUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				// lanzamos todas las excepciones de SQL para comprobar que están bien
				// recogidas.
				doThrow(DuplicateKeyException.class).when(cf).validate(anyMap());
				eR = service.billUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(DataIntegrityViolationException.class).when(cf).validate(anyMap());
				eR = service.billUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(cf);
				// extra para controlar required:
				eR = service.billUpdate(getMapUpdate(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.CREATION_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.billUpdate(getMapId(), getMapId());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.CREATION_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}

		}
	}

	@Nested
	@DisplayName("Test for Bill deletes")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class billDelete {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testBillDeleteControlFieldsReset() {
			service.billDelete(TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map ")
		void testBillDeleteControlFieldsValidate() {
			service.billDelete(TestingTools.getMapEmpty());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());

			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testBillDeleteOK() {

			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(), anyList());
			doReturn(new EntityResultMapImpl()).when(daoHelper).delete(any(), anyMap());

			// válido: HashMap campo único y exclusivo
			eR = service.billDelete(getMapId());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores Subcontulta Error")
		void testBillDeleteSubQueryKO() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(new EntityResultWrong()).when(daoHelper).query(any(), anyMap(), anyList());
//			doReturn(new EntityResultMapImpl()).when(daoHelper).delete(any(), anyMap());

			//
			eR = service.billDelete(getMapId());
			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
			assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
		}

		@Test
		@DisplayName("Valores Subconsultta 0 resultados")
		void testBillDeleteSubQueryNoResults() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList());
//			doReturn(new EntityResultMapImpl()).when(daoHelper).delete(any(), anyMap());

			//
			eR = service.billDelete(getMapId());
			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
			assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testBillDeleteKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recogidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.billDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.billDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.billDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.billDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.billDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				// lanzamos todas las excepciones de SQL para comprobar que están bien
				// recogidas.
				doThrow(DataIntegrityViolationException.class).when(cf).validate(anyMap());
				eR = service.billDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(cf); // para quitar doThrow anterior
				// extra para controlar required:
				eR = service.billDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.billDelete(getMapRequiredDeletetExtendedWidthRestricted());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}
	}

	@Nested
	@DisplayName("Test for bills By Hotel Department query")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class billsByHotelDepartmentQuery {

		// billsByHotelDepartmentQuery
		@Test
		@DisplayName("ControlFields usar reset()")
		void testBillsByHotelDepartmentQueryControlFieldsReset() {
			service.billsByHotelDepartmentQuery(TestingTools.getMapEmpty(), getColumsName());
			verify(cf, description("No se ha utilizado el método reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testBillsByHotelDepartmentQueryControlFieldsValidate() {
			service.billsByHotelDepartmentQuery(TestingTools.getMapEmpty(), getColumsName2());
			try {
				verify(cf, description("No se ha utilizado el método validate de ControlFields")).validate(anyMap());

				// por no required
				// verify(cf, description("No se ha utilizado el método validate de
				// ControlFields")).validate(anyList());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testBillsByHotelDepartmentQueryOK() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());

			// válido: HashMap vacio (sin filtros)
			eR = service.billsByHotelDepartmentQuery(TestingTools.getMapEmpty(), getColumsName2());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

			// válido: HashMap con filtro que existe (sin filtros)
			eR = service.billsByHotelDepartmentQuery(getMapId2(), getColumsName2());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testBillQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recogidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.billsByHotelDepartmentQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.billsByHotelDepartmentQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.billsByHotelDepartmentQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.billsByHotelDepartmentQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.billsByHotelDepartmentQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}
	}

	@Nested
	@DisplayName("Test for gastos Departamento Query")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class GastosDepartamentoQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testGastosDepartamentoQueryControlFieldsReset() {
			service.gastosDepartamentoQuery(TestingTools.getMapEmpty(), getColumsName());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map ")
		void testGastosDepartamentoQueryControlFieldsValidate() {
			service.gastosDepartamentoQuery(TestingTools.getMapEmpty(), getColumsName2());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testGastosDepartamentoQueryOK() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());

			// when(hotelServiceMock.getBookingStatus(any())).thenReturn(BookingDao.Status.CONFIRMED);
			when(departmentServiceMock.departmentQuery(anyMap(), anyList())).thenReturn(getEntityResultDepartamentos());// entityRsultextratools.entityresultempty(hashmap
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
			// when(this.bookingSlotsInfoQuery(anyMap(),
			// anyList())).thenReturn(getTotalSlots());

			// válido: HashMap campos y filtros
			eR = service.gastosDepartamentoQuery(getMapId3(), getColumsName3());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());
		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testGastosDepartamentoQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.gastosDepartamentoQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.gastosDepartamentoQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.gastosDepartamentoQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.gastosDepartamentoQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.gastosDepartamentoQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(cf);
				// extra para controlar required:
				eR = service.gastosDepartamentoQuery(getMapUpdate(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.CREATION_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.gastosDepartamentoQuery(getMapId(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.CREATION_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}

		}
	}

	// datos entrada

	Map<String, Object> getMapRequiredInsert() {
		return new HashMap<>() {
			{
				put(dao.ATTR_ID_HTL, 2);
				put(dao.ATTR_ID_DPT, 3);
				put(dao.ATTR_CONCEPT, "boda");
				put(dao.ATTR_DATE, "2018-12-12");
				put(dao.ATTR_AMOUNT, 3000.75);
			}
		};
	}

	Map<String, Object> getMapUpdate() {
		return getMapRequiredInsert();
	}

	Map<String, Object> getMapUpdate2() {
		return new HashMap<>() {
			{
				put(DepartmentDao.ATTR_ID, 2);
				put(HotelDao.ATTR_ID, 2);
			}
		};
	}

	Map<String, Object> getMapUpdate3() {
		return new HashMap<>() {
			{
				put(DepartmentDao.ATTR_ID, 2);
				put(HotelDao.ATTR_ID, 2);
				put(dao.ATTR_ID, 2);
			}
		};
	}

	Map<String, Object> getMapRequiredInsertExtended() {
		return new HashMap<>() {
			{
				put(dao.ATTR_ID_HTL, 2);
				put(dao.ATTR_ID_DPT, 3);
				put(dao.ATTR_CONCEPT, "boda");
				put(dao.ATTR_DATE, "2018-12-12");
				put(dao.ATTR_AMOUNT, 3000.75);
			}
		};
	}

	Map<String, Object> getMapRequiredInsertExtendedWidthRestricted() {
		return new HashMap<>() {
			{
				put(dao.ATTR_ID, 1);
				put(dao.ATTR_ID_HTL, 2);
				put(dao.ATTR_ID_DPT, 3);
				put(dao.ATTR_CONCEPT, "boda");
				put(dao.ATTR_DATE, "2018-12-12");
				put(dao.ATTR_AMOUNT, 3000.75);
			}
		};
	}

	Map<String, Object> getMapRequiredDeletetExtendedWidthRestricted() {
		return getMapRequiredInsertExtendedWidthRestricted();
	}

	HashMap<String, Object> getMapId() {
		HashMap<String, Object> filters = new HashMap<>() {
			{
				put(dao.ATTR_ID, 2);
			}
		};
		return filters;
	};

	HashMap<String, Object> getMapId2() {
		HashMap<String, Object> filters = new HashMap<>() {
			{
				put(dao.ATTR_ID_HTL, 2);
			}
		};
		return filters;
	};

	HashMap<String, Object> getMapId3() {
		HashMap<String, Object> filters = new HashMap<>() {
			{
				put(hotelDao.ATTR_ID, 2);
				put(departmentDao.ATTR_ID, 2);

			}
		};
		return filters;
	};

//		HashMap<String, Object> getMapIdWrongValue() {
//			HashMap<String, Object> filters = new HashMap<>() {
//				{
//					put(billdao.ATTR_ID, "albaricoque");
//				}
//			};
//			return filters;
//		};

	List<String> getColumsName2() {
		List<String> columns = new ArrayList<>() {
			{
				add(dao.ATTR_CONCEPT);
				add(hotelDao.ATTR_NAME);
				add(HotelDao.ATTR_CITY);
				add(dao.ATTR_ID);
				add(dao.ATTR_AMOUNT);
				add(dao.ATTR_DATE);
				add(DepartmentDao.ATTR_NAME);
			}
		};
		return columns;
	}

	List<String> getColumsName() {
		List<String> columns = new ArrayList<>() {
			{
				add(dao.ATTR_CONCEPT);

			}
		};
		return columns;
	}

	List<String> getColumsName3() {
		List<String> columns = new ArrayList<>() {
			{
				// add(BillDao.ATTR_ID);
				// add(BillDao.ATTR_CONCEPT);
				// add(BillDao.ATTR_DATE);
				// add(BillDao.ATTR_AMOUNT);
				add(DepartmentDao.ATTR_NAME);
				add(DepartmentDao.ATTR_DESCRIPTION);
				// add(HotelDao.ATTR_NAME);
			}
		};
		return columns;
	}

	EntityResult getEntityResultDepartamentos() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(DepartmentDao.ATTR_NAME, 1);// da igual lo q se devuelva
				put(DepartmentDao.ATTR_DESCRIPTION, "a");

			}
		});
		return er;
	}

	// fin datos entrada

}

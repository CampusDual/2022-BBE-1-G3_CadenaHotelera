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
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlPermissions;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.EmployeeDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@Spy
	ControlFields cf;

	@InjectMocks
	EmployeeService service;

	@Autowired
	EmployeeDao dao;

	EntityResult eR;

	@Nested
	@DisplayName("Test for Employee queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class EmployeeQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testEmployeeQueryControlFieldsReset() {
			service.employeeQuery(TestingTools.getMapEmpty(), getColumsName());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testEmployeeQueryControlFieldsValidate() {
			try {
                doNothing().when(cf).restricPermissions(anyMap());
            } catch (Exception e) {
                e.printStackTrace();
                fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
                }
			service.employeeQuery(TestingTools.getMapEmpty(), getColumsName());
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
		void testEmployeeQueryOK() {
			try {
                doNothing().when(cf).restricPermissions(anyMap());
            } catch (Exception e) {
                e.printStackTrace();
                fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
                }
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList());

			// válido: HashMap vacio (sin filtros)
			eR = service.employeeQuery(TestingTools.getMapEmpty(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

			// válido: HashMap con filtro que existe
			eR = service.employeeQuery(getMapId(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testEmployeeQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.employeeQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.employeeQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.employeeQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.employeeQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.employeeQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}

		}

	}

	@Nested
	@DisplayName("Test for Employee inserts")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class EmployeeInsert {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testEmployeeInsertControlFieldsReset() {
			service.employeeInsert(TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map ")
		void testDepartmentInsertControlFieldsValidate() {
			service.employeeInsert(TestingTools.getMapEmpty());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());

			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testEmployeeInsertOK() {
			try {
				doNothing().when(cf).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(TestingTools.getEntityEmpty()).when(daoHelper).query(any(), anyMap(), anyList());
			doReturn(new EntityResultMapImpl()).when(daoHelper).insert(any(), anyMap());

			// válido: HashMap campos mínimos
			eR = service.employeeInsert(getMapRequiredInsert());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

			// válido: HashMap campos mínimos y mas
			eR = service.employeeInsert(getMapRequiredInsertExtended());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		void testEmployeeInsertFired() {
			try {
				doNothing().when(cf).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(getMapRequiredInsertExtended()).when(daoHelper).query(any(), anyMap(), anyList());
			doReturn(new EntityResultMapImpl()).when(daoHelper).insert(any(), anyMap());

			// válido: HashMap campos mínimos
			eR = service.employeeInsert(getMapRequiredInsert());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

			// válido: HashMap campos mínimos y mas
			eR = service.employeeInsert(getMapRequiredInsertExtended());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testDepartmentInsertKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.employeeInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.employeeInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.employeeInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.employeeInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.employeeInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(DuplicateKeyException.class).when(cf).validate(anyMap());
				eR = service.employeeInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(cf);
				// extra para controlar required:
				eR = service.employeeInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				System.out.println(eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.employeeInsert(getMapRequiredInsertExtendedWidthRestricted());
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
	@DisplayName("Test for Employee updates")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class DepartmentUpdate {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testDepartmentUpdateControlFieldsReset() {
			service.employeeUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map ")
		void testEmployeeUpdateControlFieldsValidate() {
			service.employeeUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testEmployeeUpdateOK() {
			doReturn(isNotFired()).when(daoHelper).query(any(), anyMap(), anyList());
			doReturn(new EntityResultMapImpl()).when(daoHelper).update(any(), anyMap(), anyMap());
			try {
 
				doNothing().when(cf).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			// válido: HashMap campos y filtros
			eR = service.employeeUpdate(getMapUpdate(), getMapId());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testEmployeeUpdateKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.employeeUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.employeeUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.employeeUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.employeeUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.employeeUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				// lanzamos todas las excepciones de SQL para comprobar que están bien
				// recojidas.
				doThrow(DuplicateKeyException.class).when(cf).validate(anyMap());
				eR = service.employeeUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(DataIntegrityViolationException.class).when(cf).validate(anyMap());
				eR = service.employeeUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(cf);
				// extra para controlar required:
				eR = service.employeeUpdate(getMapUpdate(), TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.CREATION_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.employeeUpdate(getMapId(), getMapId());
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
				put(dao.ATTR_NAME, "cesar");
				put(dao.ATTR_IDEN_DOC, "34896415x");
				put(dao.ATTR_SOCIAL_DOC, "2256489");
				put(dao.ATTR_SALARY, 1800);
				put(dao.ATTR_PHONE, 667880938);
				put(dao.ATTR_ACCOUNT, "210020255");
				put(dao.ATTR_DEPARTMENT, 1);
				put(dao.ATTR_HOTEL, 1);
				put(dao.ATTR_HIRING, new Date());
				put(dao.ATTR_COUNTRY, "ES");
			}
		};
	}

	Map<String, Object> getMapUpdate() {
		return new HashMap<>() {
			{

				put(dao.ATTR_NAME, "Pedro");

			}
		};
	}

	EntityResult isNotFired() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
//				
				put(dao.ATTR_FIRED, null);

			}
		});
		return er;
	}

	Map<String, Object> getMapRequiredInsertExtended() {

		return new HashMap<>() {
			{
				put(dao.ATTR_PHONE, "667880938");
				put(dao.ATTR_EMAIL, "cesarbouzaspaw@gmail.com");
				put(dao.ATTR_FIRED, "2023-06-15");
			}
		};
	}

	Map<String, Object> getMapRequiredInsertExtendedWidthRestricted() {

		return new HashMap<>() {
			{
				put(dao.ATTR_ID, "1");
				put(dao.ATTR_PHONE, "667880938");
				put(dao.ATTR_EMAIL, "cesarbouzaspaw@gmail.com");
			}
		};
	}

	Map<String, Object> getMapRequiredDeletetExtendedWidthRestricted() {
		return getMapRequiredInsertExtendedWidthRestricted();
	}

	HashMap<String, Object> getMapId() {
		HashMap<String, Object> filters = new HashMap<>() {
			{
				put(dao.ATTR_IDEN_DOC, "34896415x");
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
				add(dao.ATTR_NAME);
			}
		};
		return columns;
	}

}

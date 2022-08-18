package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingGuestDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.DepartmentDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServicesXtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlPermissions;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.jee.server.dao.IOntimizeDaoSupport;
import com.ontimize.jee.server.dao.ISQLQueryAdapter;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {

	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@Spy
	ControlFields cf;

	@InjectMocks
	StatisticsService service;
	
	@Mock
	HotelService hotelServiceMock;

	@Autowired
	HotelDao hotelDao;

	@Autowired
	ISQLQueryAdapter adapter;

	EntityResult eR;

	@Nested
	@DisplayName("Test for HotelMaximumCapacity queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class HotelMaximumCapacityQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testHotelMaximumCapacityQueryControlFieldsReset() {
			service.hotelMaximumCapacityQuery(TestingTools.getMapEmpty(), getColumsNameMaximumCapacity());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testHotelMaximumCapacityQueryControlFieldsValidateList() {

			try {
				doNothing().when(cf).restricPermissions(anyMap());
				service.hotelMaximumCapacityQuery(getHotelId(), getColumsNameMaximumCapacity());

				verify(cf, description("No se ha utilizado el metodo validate de ControlFields map"))
						.validate(anyMap());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields list"))
						.validate(anyList());
			} catch (Exception e) {
//				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testGuestCountQueryOK() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
//				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(getEntityResultHotelMaximumCapacity()).when(daoHelper).query(any(), anyMap(), anyList(),
					anyString());
			eR = service.hotelMaximumCapacityQuery(getHotelId(), new ArrayList());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testGuestCountQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelMaximumCapacityQuery(TestingTools.getMapEmpty(), getColumsNameMaximumCapacity());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.hotelMaximumCapacityQuery(TestingTools.getMapEmpty(), getColumsNameMaximumCapacity());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelMaximumCapacityQuery(TestingTools.getMapEmpty(), getColumsNameMaximumCapacity());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.hotelMaximumCapacityQuery(TestingTools.getMapEmpty(), getColumsNameMaximumCapacity());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.hotelMaximumCapacityQuery(TestingTools.getMapEmpty(), getColumsNameMaximumCapacity());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
//				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}

	}

	@Nested
	@DisplayName("Test for HotelOccupancyPercentage queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class HotelOccupancyPercentageQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testHotelOccupancyPercentageQueryControlFieldsReset() {
			service.hotelOccupancyPercentageQuery(TestingTools.getMapEmpty(), getColumsNameOccupancyPercentage());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testHotelOccupancyPercentageQueryControlFieldsValidateList() {

			try {
				doNothing().when(cf).restricPermissions(anyMap());
				service.hotelOccupancyPercentageQuery(getFromTo(), getColumsNameOccupancyPercentage());

				verify(cf, description("No se ha utilizado el metodo validate de ControlFields map"))
						.validate(anyMap());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields list"))
						.validate(anyList());
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testHotelOccupancyPercentageQueryOK() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(getEntityResultHotelOccupancyPercentage()).when(daoHelper).query(any(), anyMap(), anyList(),
					anyString(), any(ISQLQueryAdapter.class));
			eR = service.hotelOccupancyPercentageQuery(getFromTo(), new ArrayList());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testHotelOccupancyPercentageQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyPercentageQuery(TestingTools.getMapEmpty(),
						getColumsNameOccupancyPercentage());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyPercentageQuery(TestingTools.getMapEmpty(),
						getColumsNameOccupancyPercentage());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyPercentageQuery(TestingTools.getMapEmpty(),
						getColumsNameOccupancyPercentage());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyPercentageQuery(TestingTools.getMapEmpty(),
						getColumsNameOccupancyPercentage());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyPercentageQuery(TestingTools.getMapEmpty(),
						getColumsNameOccupancyPercentage());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}

	}

	@Nested
	@DisplayName("Test for HotelCapacityInDateRange queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class HotelCapacityInDateRangeQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testHotelCapacityInDateRangeQueryControlFieldsReset() {
			service.hotelCapacityInDateRangeQuery(TestingTools.getMapEmpty(), getColumsNameCapacityInDateRange());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testHotelCapacityInDateRangeQueryControlFieldsValidateList() {

			try {
				doNothing().when(cf).restricPermissions(anyMap());
				service.hotelCapacityInDateRangeQuery(getFromTo(), getColumsNameCapacityInDateRange());

				verify(cf, description("No se ha utilizado el metodo validate de ControlFields map"))
						.validate(anyMap());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields list"))
						.validate(anyList());
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testHotelCapacityInDateRangeQueryOK() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(getEntityResultHotelCapacityInDateRange()).when(daoHelper).query(any(), anyMap(), anyList(),
					anyString(), any(ISQLQueryAdapter.class));
			eR = service.hotelCapacityInDateRangeQuery(getFromTo(), new ArrayList());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testHotelCapacityInDateRangeQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelCapacityInDateRangeQuery(TestingTools.getMapEmpty(),
						getColumsNameCapacityInDateRange());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.hotelCapacityInDateRangeQuery(TestingTools.getMapEmpty(),
						getColumsNameCapacityInDateRange());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelCapacityInDateRangeQuery(TestingTools.getMapEmpty(),
						getColumsNameCapacityInDateRange());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.hotelCapacityInDateRangeQuery(TestingTools.getMapEmpty(),
						getColumsNameCapacityInDateRange());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.hotelCapacityInDateRangeQuery(TestingTools.getMapEmpty(),
						getColumsNameCapacityInDateRange());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}

	}

	@Nested
	@DisplayName("Test for HotelOccupancyByNationality queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class HotelOccupancyByNationalityQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testHotelOccupancyByNationalityQueryControlFieldsReset() {
			service.hotelOccupancyByNationalityQuery(TestingTools.getMapEmpty(), getColumsNameOccupancyByNationality());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testHotelOccupancyByNationalityQueryControlFieldsValidateList() {

			try {
				doNothing().when(cf).restricPermissions(anyMap());
				service.hotelOccupancyByNationalityQuery(getFromToHotelId(), getColumsNameOccupancyByNationality());

				verify(cf, description("No se ha utilizado el metodo validate de ControlFields map"))
						.validate(anyMap());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields list"))
						.validate(anyList());
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testHotelOccupancyByNationalityQueryOK() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(getEntityResultHotelOccupancyByNationality()).when(daoHelper).query(any(), anyMap(), anyList(),
					anyString(), any(ISQLQueryAdapter.class));
			eR = service.hotelOccupancyByNationalityQuery(getFromToHotelId(), new ArrayList());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testHotelOccupancyByNationalityQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyByNationalityQuery(TestingTools.getMapEmpty(),
						getColumsNameOccupancyByNationality());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyByNationalityQuery(TestingTools.getMapEmpty(),
						getColumsNameOccupancyByNationality());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyByNationalityQuery(TestingTools.getMapEmpty(),
						getColumsNameOccupancyByNationality());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyByNationalityQuery(TestingTools.getMapEmpty(),
						getColumsNameOccupancyByNationality());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyByNationalityQuery(TestingTools.getMapEmpty(),
						getColumsNameOccupancyByNationality());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}

	}

	@Nested
	@DisplayName("Test for HotelOccupancyByNationalityPercentage queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class HotelOccupancyByNationalityPercentageQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testHotelOccupancyByNationalityQueryControlFieldsReset() {
			service.hotelOccupancyByNationalityPercentageQuery(TestingTools.getMapEmpty(),
					getColumsNameOccupancyByNationalityPercentage());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testHotelOccupancyByNationalityPercentageQueryControlFieldsValidateList() {

			try {
				doNothing().when(cf).restricPermissions(anyMap());
				service.hotelOccupancyByNationalityPercentageQuery(getFromToHotelId(),
						getColumsNameOccupancyByNationalityPercentage());

				verify(cf, description("No se ha utilizado el metodo validate de ControlFields map"))
						.validate(anyMap());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields list"))
						.validate(anyList());
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testHotelOccupancyByNationalityPercentageQueryOK() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			
			doReturn(getEntityResultHotelCapacityInDateRange()).when(daoHelper).query(any(), anyMap(), anyList(),eq("queryCapacityInRange"),any(ISQLQueryAdapter.class));
			doReturn(getEntityResultHotelOccupancyByNationality()).when(daoHelper).query(any(), anyMap(), anyList(),eq("queryOccupancyByNationality"),any(ISQLQueryAdapter.class));

			eR = service.hotelOccupancyByNationalityPercentageQuery(getFromToHotelId(), new ArrayList());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testHotelOccupancyByNationalityPercentageQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyByNationalityPercentageQuery(TestingTools.getMapEmpty(),
						getColumsNameOccupancyByNationalityPercentage());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyByNationalityPercentageQuery(TestingTools.getMapEmpty(),
						getColumsNameOccupancyByNationalityPercentage());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyByNationalityPercentageQuery(TestingTools.getMapEmpty(),
						getColumsNameOccupancyByNationalityPercentage());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyByNationalityPercentageQuery(TestingTools.getMapEmpty(),
						getColumsNameOccupancyByNationalityPercentage());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyByNationalityPercentageQuery(TestingTools.getMapEmpty(),
						getColumsNameOccupancyByNationalityPercentage());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}

	}
	
	@Nested
	@DisplayName("Test for DepartmentExpensesByHotel queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class DepartmentExpensesByHotelQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testDepartmentExpensesByHotelQueryControlFieldsReset() {
			service.departmentExpensesByHotelQuery(TestingTools.getMapEmpty(), getColumsNameDepartmentExpensesByHotel());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testDepartmentExpensesByHotelQueryControlFieldsValidateList() {

			try {
				doNothing().when(cf).restricPermissions(anyMap());
				service.departmentExpensesByHotelQuery(getFromToHotelId(), getColumsNameDepartmentExpensesByHotel());

				verify(cf, description("No se ha utilizado el metodo validate de ControlFields map"))
						.validate(anyMap());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields list"))
						.validate(anyList());
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testDepartmentExpensesByHotelQueryOK() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(getEntityResultDepartmentExpensesByHotel()).when(daoHelper).query(any(), anyMap(), anyList(),
					anyString(), any(ISQLQueryAdapter.class));
			eR = service.departmentExpensesByHotelQuery(getFromToHotelId(), new ArrayList());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testDepartmentExpensesByHotelQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.departmentExpensesByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameDepartmentExpensesByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.departmentExpensesByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameDepartmentExpensesByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.departmentExpensesByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameDepartmentExpensesByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.departmentExpensesByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameDepartmentExpensesByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.departmentExpensesByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameDepartmentExpensesByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}

	}
	
	
	@Nested
	@DisplayName("Test for RoomsIncomeByHotel queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class RoomsIncomeByHotelQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testroomsIncomeByHotelQueryControlFieldsReset() {
			service.roomsIncomeByHotelQuery(TestingTools.getMapEmpty(), getColumsNameRoomsIncomeByHotel());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testRoomsIncomeByHotelQueryControlFieldsValidateList() {

			try {
				doNothing().when(cf).restricPermissions(anyMap());
				service.roomsIncomeByHotelQuery(getFromToHotelId(), getColumsNameRoomsIncomeByHotel());

				verify(cf, description("No se ha utilizado el metodo validate de ControlFields map"))
						.validate(anyMap());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields list"))
						.validate(anyList());
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testRoomsIncomeByHotelQueryOK() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(getEntityResulteRoomsIncomeByHotel()).when(daoHelper).query(any(), anyMap(), anyList(),
					anyString(), any(ISQLQueryAdapter.class));
			eR = service.roomsIncomeByHotelQuery(getFromToHotelId(), new ArrayList());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testRoomsIncomeByHotelQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.roomsIncomeByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameRoomsIncomeByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.roomsIncomeByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameRoomsIncomeByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.roomsIncomeByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameRoomsIncomeByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.roomsIncomeByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameRoomsIncomeByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.roomsIncomeByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameRoomsIncomeByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}

	}
	
	@Nested
	@DisplayName("Test for ServicesExtraIncomeByHotel queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class ServicesExtraIncomeByHotelQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testServicesExtraIncomeByHotelQueryControlFieldsReset() {
			service.servicesExtraIncomeByHotelQuery(TestingTools.getMapEmpty(), getColumsNameServicesExtraIncomeByHotel());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testServicesExtraIncomeByHotelQueryControlFieldsValidateList() {

			try {
				doNothing().when(cf).restricPermissions(anyMap());
				service.servicesExtraIncomeByHotelQuery(getFromToHotelId(), getColumsNameServicesExtraIncomeByHotel());

				verify(cf, description("No se ha utilizado el metodo validate de ControlFields map"))
						.validate(anyMap());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields list"))
						.validate(anyList());
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testServicesExtraIncomeByHotelQueryOK() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(getEntityResulteServicesExtraIncomeByHotel()).when(daoHelper).query(any(), anyMap(), anyList(),
					anyString());
			eR = service.servicesExtraIncomeByHotelQuery(getFromTo(), new ArrayList());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testServicesExtraIncomeByHotelQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.servicesExtraIncomeByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameServicesExtraIncomeByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.servicesExtraIncomeByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameServicesExtraIncomeByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.servicesExtraIncomeByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameServicesExtraIncomeByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.servicesExtraIncomeByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameServicesExtraIncomeByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.servicesExtraIncomeByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameServicesExtraIncomeByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}

	}
	
	
	@Nested
	@DisplayName("Test for IncomeVsExpensesByHotel queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class IncomeVsExpensesByHotelQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testIncomeVsExpensesByHotelQueryControlFieldsReset() {
			service.incomeVsExpensesByHotelQuery(TestingTools.getMapEmpty(), getColumsNameIncomeVsExpensesByHotel());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testIncomeVsExpensesByHotelQueryControlFieldsValidateList() {

			try {
				doNothing().when(cf).restricPermissions(anyMap());
				service.incomeVsExpensesByHotelQuery(getFromToHotelId(), getColumsNameIncomeVsExpensesByHotel());

				verify(cf, description("No se ha utilizado el metodo validate de ControlFields map"))
						.validate(anyMap());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields list"))
						.validate(anyList());
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testIncomeVsExpensesByHotelQueryOK() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			
			when(hotelServiceMock.hotelQuery(anyMap(), anyList())).thenReturn(getEntityResulteRoomsIncomeByHotel());
			doReturn(getEntityResultDepartmentExpensesByHotel()).when(daoHelper).query(any(), anyMap(), anyList(),eq("queryTotalDepartmentExpensesByHotel"),any(ISQLQueryAdapter.class));
			doReturn(getEntityResulteRoomsIncomeByHotel()).when(daoHelper).query(any(), anyMap(), anyList(),eq("queryBookingsIncomeByHotel"),any(ISQLQueryAdapter.class));
			doReturn(getEntityResulteServicesExtraIncomeByHotel()).when(daoHelper).query(any(), anyMap(), anyList(),eq("queryServicesExtrasIncomeByHotel"));
//			doReturn(getEntityResulteServicesExtraIncomeByHotel()).when(daoHelper).query(any(), anyMap(), anyList(),
//					anyString());
			eR = service.incomeVsExpensesByHotelQuery(getFromTo(), new ArrayList());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testIncomeVsExpensesByHotelQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.incomeVsExpensesByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameIncomeVsExpensesByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.incomeVsExpensesByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameIncomeVsExpensesByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.incomeVsExpensesByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameIncomeVsExpensesByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.incomeVsExpensesByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameIncomeVsExpensesByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.incomeVsExpensesByHotelQuery(TestingTools.getMapEmpty(),
						getColumsNameIncomeVsExpensesByHotel());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}

	}

	List<String> getColumsNameMaximumCapacity() {
		List<String> columns = new ArrayList<>() {
			{
				add(HotelDao.ATTR_ID);
				add(HotelDao.ATTR_NAME);
				add(HotelDao.ATTR_CITY);
				add(HotelDao.ATTR_MAXIMUN_CAPACITY);
			}
		};
		return columns;
	}

	HashMap<String, Object> getHotelId() {
		HashMap<String, Object> filters = new HashMap<>() {
			{
				put(HotelDao.ATTR_ID, 1);
			}
		};
		return filters;
	};

	HashMap<String, Object> getFromTo() {
		HashMap<String, Object> filters = new HashMap<>() {
			{
				put(HotelDao.ATTR_FROM, "2022-12-12");
				put(HotelDao.ATTR_TO, "2022-12-13");
			}
		};
		return filters;
	};

	HashMap<String, Object> getFromToHotelId() {
		HashMap<String, Object> filters = new HashMap<>() {
			{
				put(HotelDao.ATTR_FROM, "2022-12-12");
				put(HotelDao.ATTR_TO, "2022-12-13");
				put(HotelDao.ATTR_ID, 1);
			}
		};
		return filters;
	};

	EntityResult getEntityResultHotelMaximumCapacity() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(HotelDao.ATTR_ID, 1);
				put(HotelDao.ATTR_NAME, "Hotel 1");
				put(HotelDao.ATTR_CITY, "Ciudad");
				put(HotelDao.ATTR_MAXIMUN_CAPACITY, new BigDecimal(100));

			}

		});
		return er;
	}

	List<String> getColumsNameOccupancyPercentage() {
		List<String> columns = new ArrayList<>() {
			{
				add(HotelDao.ATTR_ID);
				add(HotelDao.ATTR_NAME);
				add(HotelDao.ATTR_CITY);
				add("occupancy_percentage_in_date_range");
				add("capacity_in_date_range");
				add("occupancy_in_date_range");
			}
		};
		return columns;
	}

	List<String> getColumsNameCapacityInDateRange() {
		List<String> columns = new ArrayList<>() {
			{

				add(HotelDao.ATTR_ID);
				add(HotelDao.ATTR_NAME);
				add(HotelDao.ATTR_CITY);
				add(HotelDao.ATTR_CAPACITY_IN_DATE_RANGE);

			}
		};
		return columns;
	}

	List<String> getColumsNameOccupancyByNationality() {
		List<String> columns = new ArrayList<>() {
			{
				add(CustomerDao.ATTR_COUNTRY);
				add(HotelDao.ATTR_OCCUPANCY_IN_DATE_RANGE);

			}
		};
		return columns;
	}
	

	List<String> getColumsNameDepartmentExpensesByHotel() {
		List<String> columns = new ArrayList<>() {
			{
				add(DepartmentDao.ATTR_ID);
				add("total_expenses");

			}
		};
		return columns;
	}

	List<String> getColumsNameOccupancyByNationalityPercentage() {
		List<String> columns = new ArrayList<>() {
			{

				add(CustomerDao.ATTR_COUNTRY);
				add(HotelDao.ATTR_CAPACITY_IN_DATE_RANGE);
				add(HotelDao.ATTR_OCCUPANCY_IN_DATE_RANGE);
				add(HotelDao.ATTR_OCCUPANCY_PERCENTAGE_IN_DATE_RANGE);

			}
		};
		return columns;
	}
	
	List<String> getColumsNameRoomsIncomeByHotel() {
		List<String> columns = new ArrayList<>() {
			{
				add("rooms_income");
				add(HotelDao.ATTR_ID);
				add(HotelDao.ATTR_NAME);
				add(HotelDao.ATTR_CITY);

			}
		};
		return columns;
	}
	
	List<String> getColumsNameServicesExtraIncomeByHotel() {
		List<String> columns = new ArrayList<>() {
			{
				add(DepartmentDao.ATTR_ID);
				add("total_income");

			}
		};
		return columns;
	}
	
	List<String> getColumsNameIncomeVsExpensesByHotel() {
		List<String> columns = new ArrayList<>() {
			{
				add(HotelDao.ATTR_ID);
				add(HotelDao.ATTR_NAME);
				add(HotelDao.ATTR_CITY);
				add("benefits");
				add("total_income");
				add("total_expenses");

			}
		};
		return columns;
	}
	
	EntityResult getEntityResulteServicesExtraIncomeByHotel() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
			
				put(DepartmentDao.ATTR_ID,1);
				put("total_income", new BigDecimal(200));

			}

		});
		return er;
	}
	
	EntityResult getEntityResulteRoomsIncomeByHotel() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
			
				put("rooms_income",new BigDecimal(600));
				put(HotelDao.ATTR_ID,1);
				put(HotelDao.ATTR_NAME,"Hotel 1");
				put(HotelDao.ATTR_CITY,"Ciudad");

			}

		});
		return er;
	}

	EntityResult getEntityResultHotelOccupancyByNationalityPercentage() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(CustomerDao.ATTR_COUNTRY, "ES");
				put(HotelDao.ATTR_CAPACITY_IN_DATE_RANGE, new BigDecimal(100));
				put(HotelDao.ATTR_OCCUPANCY_IN_DATE_RANGE, new BigDecimal(12));
				put(HotelDao.ATTR_OCCUPANCY_PERCENTAGE_IN_DATE_RANGE, new BigDecimal(12));

			}

		});
		return er;
	}
	
	EntityResult getEntityResultHotelOccupancyByNationality() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(CustomerDao.ATTR_COUNTRY, "ES");
				put(HotelDao.ATTR_OCCUPANCY_IN_DATE_RANGE, new BigDecimal(12));

			}

		});
		return er;
	}

	EntityResult getEntityResultDepartmentExpensesByHotel() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(DepartmentDao.ATTR_ID,1);
				put("total_expenses",new BigDecimal(600));

			}

		});
		return er;
	}

	EntityResult getEntityResultHotelCapacityInDateRange() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(HotelDao.ATTR_ID,1);
				put(HotelDao.ATTR_NAME, "Hotel 1");
				put(HotelDao.ATTR_CITY, "Ciudad");
				put(HotelDao.ATTR_CAPACITY_IN_DATE_RANGE, new BigDecimal(100));

			}

		});
		return er;
	}

	EntityResult getEntityResultHotelOccupancyPercentage() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(HotelDao.ATTR_ID, 1);
				put(HotelDao.ATTR_NAME, "Hotel 1");
				put(HotelDao.ATTR_CITY, "Ciudad");
				put("occupancy_percentage_in_date_range", new BigDecimal(12));
				put("capacity_in_date_range", 100);
				put("occupancy_in_date_range", 12);

			}

		});
		return er;
	}
}
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ReceiptDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServicesXtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

import kotlin.collections.EmptyList;

@ExtendWith(MockitoExtension.class)
class BookingServiceExtraServiceTest {

	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@Mock
	BookingService bookingServiceMock;

	@Mock
	HotelServiceExtraService hotelServiceExtraServiceMock;

	@Spy
	ControlFields cf;

	@InjectMocks
	BookingServiceExtraService service;

	@Autowired
	BookingServiceExtraDao bookingServiceExtraDao;

	EntityResult eR;

	@Nested
	@DisplayName("Test for BookingServiceExtra queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class BookingServiceExtraQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testBookingServiceExtraQueryControlFieldsReset() {
			service.bookingServiceExtraQuery(TestingTools.getMapEmpty(), getColumsName());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testBookingServiceExtraQueryControlFieldsValidate() {
			service.bookingServiceExtraQuery(TestingTools.getMapEmpty(), getColumsName());
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
		void testBookingServiceExtraQueryOK() {
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList());

			// válido: HashMap vacio (sin filtros)
			eR = service.bookingServiceExtraQuery(TestingTools.getMapEmpty(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

			// válido: HashMap con filtro que existe (sin filtros)
			eR = service.bookingServiceExtraQuery(getMapId(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testBookingServiceExtraQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.bookingServiceExtraQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.bookingServiceExtraQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.bookingServiceExtraQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.bookingServiceExtraQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.bookingServiceExtraQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}

	}

	@Nested
	@DisplayName("Test for BookingServiceExtra inserts")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class HotelInsert {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testbookingServiceExtraInsertControlFieldsReset() {
			service.bookingServiceExtraInsert(TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map ")
		void testBookingServiceExtraInsertControlFieldsValidate() {
			service.bookingServiceExtraInsert(TestingTools.getMapEmpty());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());

			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testbookingServiceExtraInsertOK() {
			doReturn(new EntityResultMapImpl()).when(daoHelper).insert(any(), anyMap());
			try {
				when(bookingServiceMock.getBookingStatus(any())).thenReturn(BookingDao.Status.IN_PROGRESS);
				when(bookingServiceMock.bookingsHotelsQuery(anyMap(), anyList())).thenReturn(getBookingHotel());
				when(hotelServiceExtraServiceMock.hotelServiceExtraQuery(anyMap(), anyList())).thenReturn(getPrice());
			} catch (EntityResultRequiredException e) {
				fail("Err");
				e.printStackTrace();
			}
			// válido: HashMap campos mínimos
			eR = service.bookingServiceExtraInsert(getMapRequiredInsert());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testhotelInsertKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.bookingServiceExtraInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.bookingServiceExtraInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.bookingServiceExtraInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.bookingServiceExtraInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.bookingServiceExtraInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(cf);
				// extra para controlar required:
				eR = service.bookingServiceExtraInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				System.out.println(eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.bookingServiceExtraInsert(getMapRequiredInsertExtendedWidthRestricted());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				System.out.println(eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				reset(cf);
				doThrow(EntityResultRequiredException.class).when(bookingServiceMock).getBookingStatus(any());
				eR = service.bookingServiceExtraInsert(getMapRequiredInsert());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.RESULT_REQUIRED + " " + ErrorMessage.NO_BOOKING_ID, eR.getMessage(),
						eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}
	}

	@Nested
	@DisplayName("Test for BookingServiceExtra deletes")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class HotelDelete {
		@Test
		@DisplayName("ControlFields usar reset()")
		void testbookingServiceExtraDeleteControlFieldsReset() {
			service.bookingServiceExtraDelete(TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map ")
		void testBookingServiceExtraDeleteControlFieldsValidate() {
			service.bookingServiceExtraDelete(TestingTools.getMapEmpty());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());

			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testbookingServiceExtraDeleteOK() {

			doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(), anyList());
			doReturn(new EntityResultMapImpl()).when(daoHelper).delete(any(), anyMap());
			try {
				when(bookingServiceMock.getBookingStatus(any())).thenReturn(BookingDao.Status.IN_PROGRESS);
			} catch (EntityResultRequiredException e) {
				fail("Err");
				e.printStackTrace();
			}
			// válido: HashMap campo único y exclusivo
			eR = service.bookingServiceExtraDelete(getMapId());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores Subcontulta Error")
		void testbookingServiceExtraDeleteSubQueryKO() {
			doReturn(new EntityResultWrong()).when(daoHelper).query(any(), anyMap(), anyList());
			eR = service.bookingServiceExtraDelete(getMapId());
			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
			assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
		}

		@Test
		@DisplayName("Valores Subconsultta 0 resultados")
		void testbookingServiceExtraDeleteSubQueryNoResults() {
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList());
			eR = service.bookingServiceExtraDelete(getMapId());
			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
			assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testbookingServiceExtraDeleteKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recogidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.bookingServiceExtraDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.bookingServiceExtraDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.bookingServiceExtraDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.bookingServiceExtraDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.bookingServiceExtraDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				// lanzamos todas las excepciones de SQL para comprobar que están bien
				// recogidas.
				doThrow(DataIntegrityViolationException.class).when(cf).validate(anyMap());
				eR = service.bookingServiceExtraDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(cf); // para quitar doThrow anterior
				// extra para controlar required:
				eR = service.bookingServiceExtraDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.bookingServiceExtraDelete(getMapRequiredDeletetExtendedWidthRestricted());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				reset(cf);
				when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getBookingServiceExtra());
				doThrow(new EntityResultRequiredException("Error al consultar estado de la reserva"))
						.when(bookingServiceMock).getBookingStatus(any());
				eR = service.bookingServiceExtraDelete(getMapId());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals("Error al consultar estado de la reserva", eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}
	}

	@Nested
	@DisplayName("Test for BookingExtraServicePriceUnitsTotal queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class BookingExtraServicePriceUnitsTotalQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testBookingExtraServicePriceUnitsTotalQueryControlFieldsReset() {
			service.bookingExtraServicePriceUnitsTotalQuery(TestingTools.getMapEmpty(), getColumsName());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		
		@Test
		@DisplayName("ControlFields usar validate() map y list") 
		void testBookingExtraServicePriceUnitsTotalQueryControlFieldsValidateList() {
			service.bookingExtraServicePriceUnitsTotalQuery(getBookingId(), getColumsName());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields map")).validate(anyMap());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields list")).validate(anyList());	
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testBookingExtraServicePriceUnitsTotalQueryOK() {
			doReturn(getEntityResultPrecioServcioUnidadesTotal()).when(daoHelper).query(any(), anyMap(), anyList(),
					anyString());
			eR = service.bookingExtraServicePriceUnitsTotalQuery(getBookingId(),new ArrayList());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testBookingExtraServicePriceUnitsTotalQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.bookingExtraServicePriceUnitsTotalQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.bookingExtraServicePriceUnitsTotalQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.bookingExtraServicePriceUnitsTotalQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.bookingExtraServicePriceUnitsTotalQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.bookingExtraServicePriceUnitsTotalQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}

	}

	@Nested
	@DisplayName("Test for ExtraServicesNameDescriptionUnitsPriceDate queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class ExtraServicesNameDescriptionUnitsPriceDateQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testExtraServicesNameDescriptionUnitsPriceDateQueryControlFieldsReset() {
			service.extraServicesNameDescriptionUnitsPriceDateQuery(TestingTools.getMapEmpty(), getColumsName());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list") 
		void testBookingExtraServicePriceUnitsTotalQueryControlFieldsValidateList() {
			service.extraServicesNameDescriptionUnitsPriceDateQuery(getBookingId(), getColumsName());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields map")).validate(anyMap());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields list")).validate(anyList());	
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testExtraServicesNameDescriptionUnitsPriceDateQueryOK() {
			doReturn(getEntityResultListaServciosExtra()).when(daoHelper).query(any(), anyMap(), anyList(),
					anyString());
			eR = service.extraServicesNameDescriptionUnitsPriceDateQuery(getBookingId(),
					new ArrayList());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testExtraServicesNameDescriptionUnitsPriceDateQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.extraServicesNameDescriptionUnitsPriceDateQuery(TestingTools.getMapEmpty(),
						getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.extraServicesNameDescriptionUnitsPriceDateQuery(TestingTools.getMapEmpty(),
						getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.extraServicesNameDescriptionUnitsPriceDateQuery(TestingTools.getMapEmpty(),
						getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.extraServicesNameDescriptionUnitsPriceDateQuery(TestingTools.getMapEmpty(),
						getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.extraServicesNameDescriptionUnitsPriceDateQuery(TestingTools.getMapEmpty(),
						getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

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
				put(BookingServiceExtraDao.ATTR_ID_BKG, 1);
				put(BookingServiceExtraDao.ATTR_ID_SXT, 1);
				put(BookingServiceExtraDao.ATTR_ID_UNITS, 1);
			}
		};
	}

	HashMap<String, Object> getMapId() {
		HashMap<String, Object> filters = new HashMap<>() {
			{
				put(BookingServiceExtraDao.ATTR_ID, 1);
			}
		};
		return filters;
	};

	HashMap<String, Object> getBookingId() {
		HashMap<String, Object> filters = new HashMap<>() {
			{
				put(BookingServiceExtraDao.ATTR_ID_BKG, 1);
			}
		};
		return filters;
	};

	Map<String, Object> getMapRequiredInsertExtendedWidthRestricted() {

		return new HashMap<>() {
			{
				put(BookingServiceExtraDao.ATTR_ID, 1);
				put(BookingServiceExtraDao.ATTR_ID_BKG, 1);
				put(BookingServiceExtraDao.ATTR_ID_SXT, 1);
				put(BookingServiceExtraDao.ATTR_ID_UNITS, 1);
				put(BookingServiceExtraDao.ATTR_PRECIO, 12);
			}
		};
	}

	Map<String, Object> getMapRequiredDeletetExtendedWidthRestricted() {
		return getMapRequiredInsertExtendedWidthRestricted();
	}

	EntityResult getBookingHotel() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(HotelDao.ATTR_ID, 1);
			}
		});
		return er;
	}

	EntityResult getBookingServiceExtra() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(BookingServiceExtraDao.ATTR_ID, 1);
			}
		});
		return er;
	}
	

	EntityResult getPrice() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(HotelServiceExtraDao.ATTR_ID_SXT, 1);
				put(HotelServiceExtraDao.ATTR_PRECIO, 22);

			}
		});
		return er;
	}

	List<String> getColumsName() {
		List<String> columns = new ArrayList<>() {
			{
				add(BookingServiceExtraDao.ATTR_ID_BKG);
			}
		};
		return columns;
	}

	EntityResult getEntityResultPrecioServcioUnidadesTotal() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(BookingServiceExtraDao.ATTR_ID_BKG, 1);
				put(BookingServiceExtraDao.ATTR_ID_UNITS, 1);
				put(BookingServiceExtraDao.ATTR_PRECIO, new BigDecimal(22));
				put("total", new BigDecimal(22));
			}

		});
		return er;
	}

	List<String> getPrecioServcioUnidadesTotal() {
		List<String> columns = new ArrayList<>() {
			{
				add(BookingServiceExtraDao.ATTR_ID_BKG);
				add(BookingServiceExtraDao.ATTR_ID_UNITS);
				add(BookingServiceExtraDao.ATTR_PRECIO);
				add("total");
			}
		};
		return columns;
	}
	
	EntityResult getEntityResultListaServciosExtra() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(ServicesXtraDao.ATTR_NAME,"Servicio Extra 1");
				put(ServicesXtraDao.ATTR_DESCRIPTION,"Hace las cosas del servcio extra 1");
				put(BookingServiceExtraDao.ATTR_ID_UNITS,1);
				put(BookingServiceExtraDao.ATTR_PRECIO,new BigDecimal(22));
				put(BookingServiceExtraDao.ATTR_DATE,TestingTools.getNowString());

			}

		});
		return er;
	}

	List<String> getListaServciosExtra() {
		List<String> listaServiciosExtra = new ArrayList<String>() {
			{
				add(ServicesXtraDao.ATTR_NAME);
				add(ServicesXtraDao.ATTR_DESCRIPTION);
				add(BookingServiceExtraDao.ATTR_ID_UNITS);
				add(BookingServiceExtraDao.ATTR_PRECIO);
				add(BookingServiceExtraDao.ATTR_DATE);
			}
		};
		return listaServiciosExtra;
	}

	// fin datos entrada

}

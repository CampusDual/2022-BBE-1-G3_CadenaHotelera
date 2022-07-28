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
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

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
				when(bookingServiceMock.bookingsHotelsQuery(anyMap(), anyList()))
						.thenReturn(getBookingHotel());
				when(hotelServiceExtraServiceMock.hotelServiceExtraQuery(anyMap(), anyList()))
						.thenReturn(getPrice());
			} catch (EntityResultRequiredException e) {
				fail("Err");
				e.printStackTrace();
			}
			// válido: HashMap campos mínimos
			eR = service.bookingServiceExtraInsert(getMapRequiredInsert());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

//			// válido: HashMap campos mínimos y mas
//			eR = service.bookingServiceExtraInsert(getMapRequiredInsertExtended());
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}
//
//		@Test
//		@DisplayName("Valores de entrada NO válidos")
//		void testhotelInsertKO() {
//			try {
//				// lanzamos todas las excepciones de Validate para comprobar que están bien
//				// recojidas.
//				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
//				eR = service.hotelInsert(TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
//				eR = service.hotelInsert(TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
//				eR = service.hotelInsert(TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
//				eR = service.hotelInsert(TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
//				eR = service.hotelInsert(TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				reset(cf);
//				// extra para controlar required:
//				eR = service.hotelInsert(TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//				System.out.println(eR.getMessage());
//				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());
//
//				// extra para controlar restricted:
//				eR = service.hotelInsert(getMapRequiredInsertExtendedWidthRestricted());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//				System.out.println(eR.getMessage());
//				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				fail("excepción no capturada: " + e.getMessage());
//			}
//
//		}
	}
//
//	@Nested
//	@DisplayName("Test for Hotel updates")
//	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//	public class HotelUpdate {
//
//		@Test
//		@DisplayName("ControlFields usar reset()")
//		void testhotelUpdateControlFieldsReset() {
//			service.hotelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
//			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
//		}
//
//		@Test
//		@DisplayName("ControlFields usar validate() map ")
//		void testHotelUpdateControlFieldsValidate() {
//			service.hotelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
//			try {
//				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
//			} catch (Exception e) {
//				e.printStackTrace();
//				fail("excepción no capturada: " + e.getMessage());
//			}
//		}
//
//		@Test
//		@DisplayName("Valores de entrada válidos")
//		void testhotelUpdateOK() {
//			doReturn(new EntityResultMapImpl()).when(daoHelper).update(any(), anyMap(), anyMap());
//
//			// válido: HashMap campos y filtros
//			eR = service.hotelUpdate(getMapUpdate(), getMapId());
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());
//
//		}
//
//		@Test
//		@DisplayName("Valores de entrada NO válidos")
//		void testhotelUpdateKO() {
//			try {
//				// lanzamos todas las excepciones de Validate para comprobar que están bien
//				// recojidas.
//				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
//				eR = service.hotelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
//				eR = service.hotelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
//				eR = service.hotelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
//				eR = service.hotelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
//				eR = service.hotelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				// lanzamos todas las excepciones de SQL para comprobar que están bien
//				// recojidas.
//				doThrow(DuplicateKeyException.class).when(cf).validate(anyMap());
//				eR = service.hotelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				doThrow(DataIntegrityViolationException.class).when(cf).validate(anyMap());
//				eR = service.hotelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				reset(cf);
//				// extra para controlar required:
//				eR = service.hotelUpdate(getMapUpdate(), TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.CREATION_ERROR, eR.getMessage(), eR.getMessage());
//				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());
//
//				// extra para controlar restricted:
//				eR = service.hotelUpdate(getMapId(), getMapId());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.CREATION_ERROR, eR.getMessage(), eR.getMessage());
//				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				fail("excepción no capturada: " + e.getMessage());
//			}
//
//		}
//	}
//
//	@Nested
//	@DisplayName("Test for Hotel deletes")
//	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//	public class HotelDelete {
////TODO PENDIENTE TERMINAR DELETE
//		@Test
//		@DisplayName("ControlFields usar reset()")
//		void testhotelDeleteControlFieldsReset() {
//			service.hotelDelete(TestingTools.getMapEmpty());
//			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
//		}
//
//		@Test
//		@DisplayName("ControlFields usar validate() map ")
//		void testHotelDeleteControlFieldsValidate() {
//			service.hotelDelete(TestingTools.getMapEmpty());
//			try {
//				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
//			} catch (Exception e) {
//				e.printStackTrace();
//				fail("excepción no capturada: " + e.getMessage());
//
//			}
//		}
//
//		@Test
//		@DisplayName("Valores de entrada válidos")
//		void testhotelDeleteOK() {
//			
//			doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(),anyList());
//			doReturn(new EntityResultMapImpl()).when(daoHelper).delete(any(), anyMap());
//
//			// válido: HashMap campos mínimos
//			eR = service.hotelDelete(getMapId());
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());
// 
//		}
//		
//		@Test
//		@DisplayName("Valores Subcontulta Error")
//		void testhotelDeleteSubQueryKO() {
//			doReturn(new EntityResultWrong()).when(daoHelper).query(any(), anyMap(),anyList());
//			doReturn(new EntityResultMapImpl()).when(daoHelper).delete(any(), anyMap());
//			
//			// 
//			eR = service.hotelDelete(getMapId());
//			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//			assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//		}
//
//		@Test
//		@DisplayName("Valores de entrada NO válidos")
//		void testhotelDeleteKO() {
//			try {
//				// lanzamos todas las excepciones de Validate para comprobar que están bien
//				// recojidas.
//				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
//				eR = service.hotelDelete(TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
//				eR = service.hotelDelete(TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
//				eR = service.hotelDelete(TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
//				eR = service.hotelDelete(TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
//				eR = service.hotelDelete(TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				// lanzamos todas las excepciones de SQL para comprobar que están bien
//				// recojidas.
//				doThrow(DataIntegrityViolationException.class).when(cf).validate(anyMap());
//				eR = service.hotelUpdate(TestingTools.getMapEmpty(), TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				reset(cf);
//				// extra para controlar required:
//				eR = service.hotelDelete(TestingTools.getMapEmpty());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//				System.out.println(eR.getMessage());
//				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());
//
//				// extra para controlar restricted:
//				eR = service.hotelDelete(getMapRequiredDeletetExtendedWidthRestricted());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//				System.out.println(eR.getMessage());
//				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				fail("excepción no capturada: " + e.getMessage());
//			}
//
//		}
//	}
//
//	// datos entrada

	Map<String, Object> getMapRequiredInsert() {
		return new HashMap<>() {
			{
				put(BookingServiceExtraDao.ATTR_ID_SXT, 1);
				put(BookingServiceExtraDao.ATTR_ID_BKG, 2);
				put(BookingServiceExtraDao.ATTR_ID_UNITS,3);
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
//
//	Map<String, Object> getMapUpdate() {
//		return getMapRequiredInsert();
//	}

	

//	Map<String, Object> getMapRequiredInsertExtended() {
//
//		return new HashMap<>() {
//			{
//				put(HotelDao.ATTR_NAME, "Hotel 23");
//				put(HotelDao.ATTR_STREET, "Avenida Sin Nombre Nº 1");
//				put(HotelDao.ATTR_CITY, "Vigo");
//				put(HotelDao.ATTR_CP, "36211");
//				put(HotelDao.ATTR_STATE, "Galicia");
//				put(HotelDao.ATTR_COUNTRY, "ES");
//				put(HotelDao.ATTR_PHONE, "+34 986 111 111");
//				put(HotelDao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//				put(HotelDao.ATTR_DESCRIPTION, "Faltan campos no nullables");
//				put(HotelDao.ATTR_IS_OPEN, 1);
//			}
//		};
//	}
//
//	Map<String, Object> getMapRequiredInsertExtendedWidthRestricted() {
//
//		return new HashMap<>() {
//			{
//				put(HotelDao.ATTR_ID, "1");
//				put(HotelDao.ATTR_NAME, "Hotel 23");
//				put(HotelDao.ATTR_STREET, "Avenida Sin Nombre Nº 1");
//				put(HotelDao.ATTR_CITY, "Vigo");
//				put(HotelDao.ATTR_CP, "36211");
//				put(HotelDao.ATTR_STATE, "Galicia");
//				put(HotelDao.ATTR_COUNTRY, "ES");
//				put(HotelDao.ATTR_PHONE, "+34 986 111 111");
//				put(HotelDao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//				put(HotelDao.ATTR_DESCRIPTION, "Faltan campos no nullables");
//				put(HotelDao.ATTR_IS_OPEN, 1);
//			}
//		};
//	}

//	Map<String, Object> getMapRequiredDeletetExtendedWidthRestricted() {
//		return getMapRequiredInsertExtendedWidthRestricted();
//	}
	
	EntityResult getBookingHotel() {
		List<String> columnList = Arrays.asList(HotelDao.ATTR_ID);
		EntityResult er = new EntityResultMapImpl(columnList);
		er.addRecord(new HashMap<String, Object>() {
			{
				put(HotelDao.ATTR_ID, 1);
			}
		});
		return er;
	}
	
	EntityResult getPrice() {
		List<String> columnList = Arrays.asList(HotelServiceExtraDao.ATTR_PRECIO);
		EntityResult er = new EntityResultMapImpl(columnList);
		er.addRecord(new HashMap<String, Object>() {
			{
				put(HotelServiceExtraDao.ATTR_PRECIO, 22);
				put(HotelServiceExtraDao.ATTR_ID_SXT,1);
			}
		});
		return er;
	}

	//
//		HashMap<String, Object> getMapIdWrongValue() {
//			HashMap<String, Object> filters = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, "albaricoque");
//				}
//			};
//			return filters;
//		};

	List<String> getColumsName() {
		List<String> columns = new ArrayList<>() {
			{
				add(BookingServiceExtraDao.ATTR_ID_BKG);
			}
		};
		return columns;
	}
//	// fin datos entrada











}


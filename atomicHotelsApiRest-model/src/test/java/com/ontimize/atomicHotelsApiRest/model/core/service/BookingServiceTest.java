package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.dao.DuplicateKeyException;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingGuestDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ReceiptDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@Mock
	BookingService bookingServiceMock;

	@Spy
	ControlFields cf;

	@InjectMocks
	BookingService service;

	@Autowired
	BookingDao dao;

	EntityResult eR;

	@Autowired
	BookingDao bookingDao;
	/*
	 * @Spy BookingService spyService;
	 */

	@Autowired
	ValidateFields vf;

//	@BeforeEach
//	void setUp(){
//		this.service = new BookingService();
//		
//	}
//	@Test
//	void testBookingQuery() {
//		//
//	}

//	@Test
//	void testBookingInfoQuery() {
//		fail("Not yet implemented");
//	}

//	@Test
//	void testBookingInsert() {
//	
//	}

//	

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
	@DisplayName("Test for actions")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class Actions {
		
		@Test
		@DisplayName("Valores No Válidos")
		void testBookingActionUpdateKO() {
			EntityResult entityResult;

			entityResult = service.bookingActionUpdate(getKeyMapOk(), getAttrMapActionCheckIn()); // invertidos
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

			entityResult = service.bookingActionUpdate(emptyMap(), getKeyMapOk());
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

			entityResult = service.bookingActionUpdate(getAttrMapActionCheckIn(), emptyMap());
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

			entityResult = service.bookingActionUpdate(emptyMap(), emptyMap());
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

			entityResult = service.bookingActionUpdate(getAttrMapActionCancelKO(), getKeyMapOk());
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

		}
		@Test
		@DisplayName("ACTION CANCEL")
		void testBookingActionUpdateCancel() {
			EntityResult entityResult;

			doReturn(confirmedER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
			doReturn(getOkER()).when(daoHelper).update(any(), anyMap(), anyMap());
			entityResult = service.bookingActionUpdate(getAttrMapActionCancel(), getKeyMapOk()); // Action
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode(), entityResult.getMessage());

			doReturn(inProgressER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
			entityResult = service.bookingActionUpdate(getAttrMapActionCancel(), getKeyMapOk());
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

			doReturn(completedER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
			entityResult = service.bookingActionUpdate(getAttrMapActionCancel(), getKeyMapOk());
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

			doReturn(canceledER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
			entityResult = service.bookingActionUpdate(getAttrMapActionCancel(), getKeyMapOk());
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());
		}

		@Test
		@DisplayName("ACTION CHECKIN")
		void testBookingActionUpdateCheckIn() {
			EntityResult entityResult;

			doReturn(confirmedER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
			doReturn(getOkER()).when(daoHelper).update(any(), anyMap(), anyMap());
			entityResult = service.bookingActionUpdate(getAttrMapActionCheckIn(), getKeyMapOk()); // Action
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode(), entityResult.getMessage());

			doReturn(inProgressER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
			entityResult = service.bookingActionUpdate(getAttrMapActionCheckIn(), getKeyMapOk());
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

			doReturn(completedER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
			entityResult = service.bookingActionUpdate(getAttrMapActionCheckIn(), getKeyMapOk());
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

			doReturn(canceledER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
			entityResult = service.bookingActionUpdate(getAttrMapActionCheckIn(), getKeyMapOk());
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());
		}

		@Test
		@DisplayName("ACTION CHECKOUT")
		void testBookingActionUpdateCheckout() {
			EntityResult entityResult;

			doReturn(confirmedER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
			doReturn(getOkER()).when(daoHelper).update(any(), anyMap(), anyMap());
			entityResult = service.bookingActionUpdate(getAttrMapActionCheckout(), getKeyMapOk()); // Action
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

			doReturn(inProgressER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
			entityResult = service.bookingActionUpdate(getAttrMapActionCheckout(), getKeyMapOk());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode(), entityResult.getMessage());

			doReturn(completedER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
			entityResult = service.bookingActionUpdate(getAttrMapActionCheckout(), getKeyMapOk());
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

			doReturn(canceledER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
			entityResult = service.bookingActionUpdate(getAttrMapActionCheckout(), getKeyMapOk());
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

		}
	}

//	@Test
//	@DisplayName("Maps de entrada no válidos")
//	void testBookingActionUpdateKO() {
//		// when(daoHelper.update(any(), anyMap(),
//		// anyMap())).thenThrow(MissingFieldsException.class);
//		EntityResult entityResult = service.bookingActionUpdate(new HashMap<String, Object>(),
//				new HashMap<String, Object>());
//		assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());
//
////		entityResult = service.bookingActionUpdate(null, null);
////		assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());
//
//		entityResult = service.bookingActionUpdate(new HashMap<String, Object>(), getKeyMapOk());
//		assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());
//
//		entityResult = service.bookingActionUpdate(getAttrMapOK(), new HashMap<String, Object>());
//		assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());
//	}

//	@Test
//	void testBookingDelete() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testBookingsInRangeQuery() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testBookingsInRangeInfoQuery() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testBookingsInRangeBuilder() {
//		fail("Not yet implemented");
//	}
//

	@Nested
	@DisplayName("Test for status")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class Status {

//		@Test
//		@DisplayName("Estado cancelado OK")
//		void testGetBookingStatusCanceledOK() {
//			try {
//				doReturn(canceledER()).when(daoHelper).query(any(), anyMap(), anyList());
//				BookingDao.Status resultado = service.getBookingStatus(15);
//				assertEquals(BookingDao.Status.CANCELED, resultado);
//			} catch (EntityResultRequiredException e) {
//				fail(e);
//			}
//
//		}
//
//		@Test
//		@DisplayName("Estado completado OK")
//		void testGetBookingStatusCompletedOK() {
//			try {
//				doReturn(completedER()).when(daoHelper).query(any(), anyMap(), anyList());
//				BookingDao.Status resultado = service.getBookingStatus(15);
//				assertEquals(BookingDao.Status.COMPLETED, resultado);
//			} catch (EntityResultRequiredException e) {
//				fail(e);
//			}
//
//		}
//
//		@Test
//		@DisplayName("Estado en progreso OK")
//		void testGetBookingStatusInProgressOK() {
//			try {
//				doReturn(inProgressER()).when(daoHelper).query(any(), anyMap(), anyList());
//				BookingDao.Status resultado = service.getBookingStatus(15);
//				assertEquals(BookingDao.Status.IN_PROGRESS, resultado);
//			} catch (EntityResultRequiredException e) {
//				fail(e);
//			}
//
//		}
//
//		@Test
//		@DisplayName("Estado confirmado OK")
//		void testGetBookingStatusConfirmadedOK() {
//			try {
//				doReturn(confirmedER()).when(daoHelper).query(any(), anyMap(), anyList());
//				BookingDao.Status resultado = service.getBookingStatus(15);
//				assertEquals(BookingDao.Status.CONFIRMED, resultado);
//			} catch (EntityResultRequiredException e) {
//				fail(e);
//			}
//
//		}

		@Test
		@DisplayName("Estádo Canceled")
		void testGetBookingStatusCanceled() {
			try {
				BookingDao.Status expected = BookingDao.Status.CANCELED;

				doReturn(canceledER()).when(daoHelper).query(any(), anyMap(), anyList());
				BookingDao.Status resultado = service.getBookingStatus(15);
				assertEquals(expected, resultado);
				
				doReturn(completedER()).when(daoHelper).query(any(), anyMap(), anyList());
				resultado = service.getBookingStatus(15);
				assertNotEquals(expected, resultado);

				doReturn(inProgressER()).when(daoHelper).query(any(), anyMap(), anyList());
				resultado = service.getBookingStatus(15);
				assertNotEquals(expected, resultado);

				doReturn(confirmedER()).when(daoHelper).query(any(), anyMap(), anyList());
				resultado = service.getBookingStatus(15);
				assertNotEquals(expected, resultado);

			} catch (EntityResultRequiredException e) {
				fail(e);
			}
		}

		@Test
		@DisplayName("Estádo Completed")
		void testGetBookingStatusCompleted() {
			try {
				BookingDao.Status expected = BookingDao.Status.COMPLETED;

				doReturn(canceledER()).when(daoHelper).query(any(), anyMap(), anyList());
				BookingDao.Status resultado = service.getBookingStatus(15);
				assertNotEquals(expected, resultado);

				doReturn(completedER()).when(daoHelper).query(any(), anyMap(), anyList());
				resultado = service.getBookingStatus(15);
				assertEquals(expected, resultado);

				doReturn(inProgressER()).when(daoHelper).query(any(), anyMap(), anyList());
				resultado = service.getBookingStatus(15);
				assertNotEquals(expected, resultado);

				doReturn(confirmedER()).when(daoHelper).query(any(), anyMap(), anyList());
				resultado = service.getBookingStatus(15);
				assertNotEquals(expected, resultado);

			} catch (EntityResultRequiredException e) {
				fail(e);
			}
		}

		@Test
		@DisplayName("Estádo InProgress")
		void testGetBookingStatusInProgress() {
			try {
				reset(cf);

				BookingDao.Status expected = BookingDao.Status.IN_PROGRESS;

				doReturn(canceledER()).when(daoHelper).query(any(), anyMap(), anyList());
				BookingDao.Status resultado = service.getBookingStatus(15);
				assertNotEquals(expected, resultado);

				doReturn(completedER()).when(daoHelper).query(any(), anyMap(), anyList());
				resultado = service.getBookingStatus(15);
				assertNotEquals(expected, resultado);

				doReturn(inProgressER()).when(daoHelper).query(any(), anyMap(), anyList());
				resultado = service.getBookingStatus(15);
				assertEquals(expected, resultado);

				doReturn(confirmedER()).when(daoHelper).query(any(), anyMap(), anyList());
				resultado = service.getBookingStatus(15);
				assertNotEquals(expected, resultado);

			} catch (EntityResultRequiredException e) {
				fail(e);
			}
		}

		@Test
		@DisplayName("Estádo Confirmed")
		void testGetBookingStatusConfirmed() {
			try {
				BookingDao.Status expected = BookingDao.Status.CONFIRMED;

				doReturn(canceledER()).when(daoHelper).query(any(), anyMap(), anyList());
				BookingDao.Status resultado = service.getBookingStatus(15);
				assertNotEquals(expected, resultado);

				doReturn(completedER()).when(daoHelper).query(any(), anyMap(), anyList());
				resultado = service.getBookingStatus(15);
				assertNotEquals(expected, resultado);

				doReturn(inProgressER()).when(daoHelper).query(any(), anyMap(), anyList());
				resultado = service.getBookingStatus(15);
				assertNotEquals(expected, resultado);

				doReturn(confirmedER()).when(daoHelper).query(any(), anyMap(), anyList());
				resultado = service.getBookingStatus(15);
				assertEquals(expected, resultado);

			} catch (EntityResultRequiredException e) {
				fail(e);
			}
		}

	}

	// ER

	List<String> getStatusColumnList() {
		return Arrays.asList(BookingDao.ATTR_CHECKIN, BookingDao.ATTR_CHECKOUT, BookingDao.ATTR_CANCELED);
	}

	EntityResult canceledER() {
		EntityResult er = new EntityResultMapImpl(getStatusColumnList());
		er.addRecord(new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_CHECKIN, null);
				put(BookingDao.ATTR_CHECKOUT, null);
				put(BookingDao.ATTR_CANCELED, TestingTools.getYesterdayString());
			}
		});
		return er;
	}

	EntityResult completedER() {
		EntityResult er = new EntityResultMapImpl(getStatusColumnList());
		er.addRecord(new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_CHECKIN, TestingTools.getYesterdayString());
				put(BookingDao.ATTR_CHECKOUT, TestingTools.getTomorrowString());
				put(BookingDao.ATTR_CANCELED, null);
			}
		});
		return er;
	}

	EntityResult inProgressER() {
		EntityResult er = new EntityResultMapImpl(getStatusColumnList());
		er.addRecord(new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_CHECKIN, TestingTools.getYesterdayString());
				put(BookingDao.ATTR_CHECKOUT, null);
				put(BookingDao.ATTR_CANCELED, null);
			}
		});
		return er;
	}

	EntityResult confirmedER() {
		EntityResult er = new EntityResultMapImpl(getStatusColumnList());
		er.addRecord(new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_CHECKIN, null);
				put(BookingDao.ATTR_CHECKOUT, null);
				put(BookingDao.ATTR_CANCELED, null);
			}
		});
		return er;
	}

	Map<String, Object> getKeyMapOk() {
		return new HashMap<>() {
			{
				put(BookingDao.ATTR_ID, 2);
			}
		};
	}

	Map<String, Object> getAttrMapActionCancel() {
		return new HashMap<>() {
			{
				put(BookingDao.NON_ATTR_ACTION, "CANCEL");
			}
		};
	}

	Map<String, Object> getAttrMapActionCancelKO() {
		return new HashMap<>() {
			{
				put(BookingDao.NON_ATTR_ACTION, "CANCELKO");
			}
		};
	}

	Map<String, Object> getAttrMapActionCheckIn() {
		return new HashMap<>() {
			{
				put(BookingDao.NON_ATTR_ACTION, "CHECKIN");
			}
		};
	}

	Map<String, Object> getAttrMapActionCheckout() {
		return new HashMap<>() {
			{
				put(BookingDao.NON_ATTR_ACTION, "CHECKOUT");
			}
		};
	}

	public EntityResult getOkER() {
		EntityResult resultado = new EntityResultMapImpl();
		resultado.setCode(EntityResult.OPERATION_SUCCESSFUL);
		return resultado;
	}

	Map<String, Object> emptyMap() {
		return new HashMap<>();
	}
//
//	@Test
//	void testBookingDaysUnitaryRoomPriceQuery() {
//		fail("Not yet implemented");
//	}

	@Nested
	@DisplayName("Test for booking queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class BookingQuery {
		// bookingQuery
		@Test
		@DisplayName("ControlFields usar reset()")
		void testBookingQueryControlFieldsReset() {
			service.bookingQuery(TestingTools.getMapEmpty(), getColumsName());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testBookingQueryControlFieldsValidate() {
			service.bookingQuery(TestingTools.getMapEmpty(), getColumsName());
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
		void testBookingQueryOK() {
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList());

			// válido: HashMap vacio (sin filtros)
			eR = service.bookingQuery(TestingTools.getMapEmpty(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

			// válido: HashMap con filtro que existe (sin filtros)
			eR = service.bookingQuery(getMapId(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testBookingQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recogidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.bookingQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.bookingQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.bookingQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.bookingQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.bookingQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}
		// bookingInfoQuery
				@Test
				@DisplayName("ControlFields usar reset()")
				void testBookingInfoQueryControlFieldsReset() {
					service.bookingInfoQuery(TestingTools.getMapEmpty(), getColumsName());
					verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
				}

				@Test
				@DisplayName("ControlFields usar validate() map y list")
				void testBookingInfoQueryControlFieldsValidate() {
					service.bookingInfoQuery(TestingTools.getMapEmpty(), getColumsName());
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
				void testBookingInfoQueryOK() {
					doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList());

					// válido: HashMap vacio (sin filtros)
					eR = service.bookingInfoQuery(TestingTools.getMapEmpty(), getColumsName());
					assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

					// válido: HashMap con filtro que existe (sin filtros)
					eR = service.bookingInfoQuery(getMapId(), getColumsName());
					assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

				}

				@Test
				@DisplayName("Valores de entrada NO válidos")
				void testBookingInfoQueryKO() {
					try {
						// lanzamos todas las excepciones de Validate para comprobar que están bien
						// recogidas.
						doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
						eR = service.bookingInfoQuery(TestingTools.getMapEmpty(), getColumsName());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

						doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
						eR = service.bookingInfoQuery(TestingTools.getMapEmpty(), getColumsName());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

						doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
						eR = service.bookingInfoQuery(TestingTools.getMapEmpty(), getColumsName());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

						doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
						eR = service.bookingInfoQuery(TestingTools.getMapEmpty(), getColumsName());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

						doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
						eR = service.bookingInfoQuery(TestingTools.getMapEmpty(), getColumsName());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
						assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

					} catch (Exception e) {
						e.printStackTrace();
						fail("excepción no capturada: " + e.getMessage());
					}

				}

	}

	@Nested
	@DisplayName("Test for bookingSlotsInfo queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class BookingSlotsInfoQuery {
		@Test
		@DisplayName("ControlFields usar reset()")
		void testBookingSlotsInfoQueryControlFieldsReset() {
			service.bookingSlotsInfoQuery(TestingTools.getMapEmpty(), getbookingSlotsInfoColums());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testBookingSlotsInfoQueryControlFieldsValidateList() {
			service.bookingSlotsInfoQuery(getMapRequiredBookingSlotsQuery(), getbookingSlotsInfoColums());
			try {
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
		void testBookingSlotsInfoQueryOK() {
			doReturn(getEntityResultbookingSlotsInfo()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
			eR = service.bookingSlotsInfoQuery(getMapRequiredBookingSlotsQuery(), new ArrayList());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testBookingSlotsInfoQueryQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.bookingSlotsInfoQuery(TestingTools.getMapEmpty(), getbookingSlotsInfoColums());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.bookingSlotsInfoQuery(TestingTools.getMapEmpty(), getbookingSlotsInfoColums());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.bookingSlotsInfoQuery(TestingTools.getMapEmpty(), getbookingSlotsInfoColums());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.bookingSlotsInfoQuery(TestingTools.getMapEmpty(), getbookingSlotsInfoColums());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.bookingSlotsInfoQuery(TestingTools.getMapEmpty(), getbookingSlotsInfoColums());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}

	}

	// datos bookingSlotsInfoQuery

	List<String> getbookingSlotsInfoColums() {
		List<String> columns = new ArrayList<>() {
			{
				add(BookingGuestDao.ATTR_TOTAL_SLOTS);
			}
		};
		return columns;
	}

	Map<String, Object> getMapRequiredBookingSlotsQuery() {
		return new HashMap<>() {
			{
				put(BookingDao.ATTR_ID, 1);

			}
		};
	}

	EntityResult getEntityResultbookingSlotsInfo() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(BookingGuestDao.ATTR_TOTAL_SLOTS, 1L);
			}
		});
		return er;
	}

	@Nested
	@DisplayName("Test for bookingDaysUnitaryRoomPrice queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class BookingDaysUnitaryRoomPriceQuery {
		@Test
		@DisplayName("ControlFields usar reset()")
		void testBookingDaysUnitaryRoomPriceQueryControlFieldsReset() {
			service.bookingDaysUnitaryRoomPriceQuery(TestingTools.getMapEmpty(),
					getBookingDaysUnitaryRoomPriceColums());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testBookingDaysUnitaryRoomPriceQueryControlFieldsValidateList() {
			service.bookingSlotsInfoQuery(getMapRequiredBookingDaysUnitaryRoomPriceQuery(),
					getBookingDaysUnitaryRoomPriceColums());
			try {
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
		void testBookingSlotsInfoQueryOK() {
			doReturn(getEntityResultbookingSlotsInfo()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());
			eR = service.bookingSlotsInfoQuery(getMapRequiredBookingDaysUnitaryRoomPriceQuery(), new ArrayList());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testBookingDaysUnitaryRoomPriceQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.bookingDaysUnitaryRoomPriceQuery(TestingTools.getMapEmpty(),
						getBookingDaysUnitaryRoomPriceColums());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.bookingDaysUnitaryRoomPriceQuery(TestingTools.getMapEmpty(),
						getBookingDaysUnitaryRoomPriceColums());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.bookingDaysUnitaryRoomPriceQuery(TestingTools.getMapEmpty(),
						getBookingDaysUnitaryRoomPriceColums());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.bookingDaysUnitaryRoomPriceQuery(TestingTools.getMapEmpty(),
						getBookingDaysUnitaryRoomPriceColums());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.bookingDaysUnitaryRoomPriceQuery(TestingTools.getMapEmpty(),
						getBookingDaysUnitaryRoomPriceColums());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}

	}

	// datos bookingDaysUnitaryRoomPriceQuery

	List<String> getBookingDaysUnitaryRoomPriceColums() {
		List<String> columns = new ArrayList<>() {
			{
				add(BookingDao.ATTR_ID);
				add(RoomTypeDao.ATTR_PRICE);
				add(ReceiptDao.ATTR_DIAS);
			}
		};
		return columns;
	}

	Map<String, Object> getMapRequiredBookingDaysUnitaryRoomPriceQuery() {
		return new HashMap<>() {
			{
				put(BookingDao.ATTR_ID, 1);

			}
		};
	}

	EntityResult getEntityResultBookingDaysUnitaryRoomPrice() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_ID, 1);
				put(RoomTypeDao.ATTR_PRICE, new BigDecimal(23));
				put(ReceiptDao.ATTR_DIAS, 1);

			}
		});
		return er;
	}

	@Nested
	@DisplayName("Test for booking_now_by_room_number queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class booking_now_by_room_numberQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testBooking_now_by_room_numberQueryControlFieldsReset() {
			service.booking_now_by_room_numberQuery(TestingTools.getMapEmpty(), getColumsName());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testBooking_now_by_room_numberQueryControlFieldsValidate() {
			service.booking_now_by_room_numberQuery(getMapRoomId(), getColumsName());
			try {
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
		void testBooking_now_by_room_numberQueryOK() {
			/*
			 * doReturn(getMapRequiredInsertExtendedWidthRestricted()).when(daoHelper).query
			 * (any(), anyMap(), anyList(), anyString()); eR =
			 * service.booking_now_by_room_numberQuery(getMapId(),new ArrayList());
			 * assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(),
			 * eR.getMessage());
			 */

//		doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(),anyList());
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(), anyString());

			// válido: HashMap campos mínimos
			eR = service.booking_now_by_room_numberQuery(getMapRoomId(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testBookingExtraServicePriceUnitsTotalQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.booking_now_by_room_numberQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.booking_now_by_room_numberQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.booking_now_by_room_numberQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.booking_now_by_room_numberQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.booking_now_by_room_numberQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}
		}
	}
	/*
	 * @Test
	 * 
	 * @DisplayName("Valores de entrada válidos") void
	 * testBooking_now_by_room_numberQueryOK() {
	 * 
	 * doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(),
	 * anyMap(),anyList()); doReturn(new
	 * EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(),
	 * anyString());
	 * 
	 * // válido: HashMap campos mínimos eR =
	 * service.booking_now_by_room_numberQuery(getMapRequiredInsert(),getColumsName(
	 * )); assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(),
	 * eR.getMessage());
	 * 
	 * // válido: HashMap campos mínimos y más eR =
	 * service.booking_now_by_room_numberQuery(getMapRequiredInsertExtended(),
	 * getColumsName()); assertEquals(EntityResult.OPERATION_SUCCESSFUL,
	 * eR.getCode(), eR.getMessage());
	 * 
	 * 
	 * doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(),
	 * anyMap(),anyList()); doReturn(new
	 * EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList(),
	 * anyString());
	 * 
	 * // válido: HashMap campos mínimos eR =
	 * service.booking_now_by_room_numberQuery(getMapRequiredInsert(),
	 * getColumsName()); assertEquals(EntityResult.OPERATION_SUCCESSFUL,
	 * eR.getCode(), eR.getMessage());
	 * 
	 * /* doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(),
	 * anyList(), anyString());
	 * 
	 * // válido: HashMap con filtro que existe (sin filtros) eR =
	 * service.booking_now_by_room_numberQuery(getMapId(), getColumsName());
	 * assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(),
	 * eR.getMessage());
	 * 
	 * }
	 * 
	 * @Test
	 * 
	 * @DisplayName("Valores de entrada NO válidos") void
	 * testbooking_now_by_room_numberQueryKO() { try { // lanzamos todas las
	 * excepciones de Validate para comprobar que están bien recogidas.
	 * doThrow(MissingFieldsException.class).when(cf).validate(anyMap()); eR =
	 * service.booking_now_by_room_numberQuery(TestingTools.getMapEmpty(),
	 * getColumsName()); assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(),
	 * eR.getMessage()); assertNotEquals(ErrorMessage.UNKNOWN_ERROR,
	 * eR.getMessage(), eR.getMessage());
	 * 
	 * doThrow(RestrictedFieldException.class).when(cf).validate(anyMap()); eR =
	 * service.booking_now_by_room_numberQuery(TestingTools.getMapEmpty(),
	 * getColumsName()); assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(),
	 * eR.getMessage()); assertNotEquals(ErrorMessage.UNKNOWN_ERROR,
	 * eR.getMessage(), eR.getMessage());
	 * 
	 * doThrow(InvalidFieldsException.class).when(cf).validate(anyMap()); eR =
	 * service.booking_now_by_room_numberQuery(TestingTools.getMapEmpty(),
	 * getColumsName()); assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(),
	 * eR.getMessage()); assertNotEquals(ErrorMessage.UNKNOWN_ERROR,
	 * eR.getMessage(), eR.getMessage());
	 * 
	 * doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap()); eR =
	 * service.booking_now_by_room_numberQuery(TestingTools.getMapEmpty(),
	 * getColumsName()); assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(),
	 * eR.getMessage()); assertNotEquals(ErrorMessage.UNKNOWN_ERROR,
	 * eR.getMessage(), eR.getMessage());
	 * 
	 * doThrow(LiadaPardaException.class).when(cf).validate(anyMap()); eR =
	 * service.booking_now_by_room_numberQuery(TestingTools.getMapEmpty(),
	 * getColumsName()); assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(),
	 * eR.getMessage()); assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(),
	 * eR.getMessage());
	 * 
	 * } catch (Exception e) { e.printStackTrace(); fail("excepción no capturada: "
	 * + e.getMessage()); }
	 * 
	 * }
	 * 
	 * }
	 */
	// datos entrada

	Map<String, Object> getMapRequiredInsert() {
		return new HashMap<>() {
			{
				put(dao.ATTR_ID, 1);

			}
		};
	}

	Map<String, Object> getMapUpdate() {
		return getMapRequiredInsert();
	}

	Map<String, Object> getMapRequiredInsertExtended() {

		return new HashMap<>() {
			{
				put(dao.ATTR_CUSTOMER_ID, 1);
				put(dao.ATTR_ROOM_ID, 1);
				put(dao.ATTR_START, "2022-07-25");
				put(dao.ATTR_END, "2022-08-12");
				put(dao.ATTR_CREATED, "2022-07-20 10:40:23.225");
				put(dao.ATTR_OBSERVATIONS, "tudo bom");
				put(dao.ATTR_CHECKIN, "2022-07-26 09:02:02.748");
				put(dao.ATTR_CHECKOUT, "2022-08-12 09:02:02.748");
				put(dao.ATTR_CANCELED, "2022-08-12 09:02:02.748");
			}
		};
	}

	Map<String, Object> getMapRequiredInsertExtendedWidthRestricted() {

		return new HashMap<>() {
			{
				put(dao.ATTR_ID, "1");
				put(dao.ATTR_CUSTOMER_ID, 1);
				put(dao.ATTR_ROOM_ID, 1);
				put(dao.ATTR_START, "2022-07-25");
				put(dao.ATTR_END, "2022-08-12");
				put(dao.ATTR_CREATED, "2022-07-20 10:40:23.225");
				put(dao.ATTR_OBSERVATIONS, "tudo bom");
				put(dao.ATTR_CHECKIN, "2022-07-26 09:02:02.748");
				// put(dao.ATTR_CHECKOUT, "2022-08-12 09:02:02.748");
				// put(dao.ATTR_CANCELED, null);
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
				/*
				 * put(dao.ATTR_CUSTOMER_ID, 1); put(dao.ATTR_ROOM_ID, 1); put(dao.ATTR_START,
				 * "2022-07-25"); put(dao.ATTR_END, "2022-08-12");
				 * put(dao.ATTR_CREATED,"2022-07-20 10:40:23.225"); put(dao.ATTR_OBSERVATIONS,
				 * "tudo bom"); put(dao.ATTR_CHECKIN, "2022-07-26 09:02:02.748");
				 */

			}
		};
		return filters;
	};

	HashMap<String, Object> getMapRoomId() {
		HashMap<String, Object> filters = new HashMap<>() {
			{
				put(dao.ATTR_ROOM_ID, 1);
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
				add(dao.ATTR_OBSERVATIONS);
			}
		};
		return columns;
	}

	EntityResult getReservasFechaActiva() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_ID, 1);
				put(RoomTypeDao.ATTR_PRICE, new BigDecimal(12));
				put(ReceiptDao.ATTR_DIAS, 2);
			}
		});
		return er;
	}

	Map<String, Object> getMapVueltaValores() {

		return new HashMap<>() {
			{
				put(dao.ATTR_ROOM_ID, 1);
				put(dao.ATTR_START, "2022-07-25");
				put(dao.ATTR_END, "2022-08-12");
				put(dao.ATTR_CREATED, "2022-07-20 10:40:23.225");
				put(dao.ATTR_OBSERVATIONS, "tudo bom");
				put(dao.ATTR_CHECKIN, "2022-07-26 09:02:02.748");
				put(dao.ATTR_CHECKOUT, "2022-08-12 09:02:02.748");
				put(dao.ATTR_CANCELED, "2022-08-12 09:02:02.748");
			}
		};
	}
	// fin datos entrada

}

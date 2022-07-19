package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

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
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.Pruebas;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@Spy
	BookingService spyService;

	@InjectMocks
	BookingService service;

	@Autowired
	BookingDao bookingDao;

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
	
	
	@Test
	@DisplayName("Valores No Válidos")
	void testBookingActionUpdateKO() {
		EntityResult entityResult;
		
		entityResult = service.bookingActionUpdate(null, getKeyMapOk()); // Action		
		assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());
	}
	

	@Test
	@DisplayName("ACTION CANCEL")
	void testBookingActionUpdateCancel() {
		EntityResult entityResult;
		Map<String, Object> attrMap  = getAttrMapActionCancel(); // ACTION
		
		doReturn(confirmedER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
		doReturn(getOkER()).when(daoHelper).update(any(), anyMap(), anyMap());
		entityResult = service.bookingActionUpdate(getAttrMapActionCancel(), getKeyMapOk()); // Action
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode(), entityResult.getMessage());
		
		doReturn(inProgressER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
		doReturn(getOkER()).when(daoHelper).update(any(), anyMap(), anyMap());
		entityResult = service.bookingActionUpdate(attrMap, getKeyMapOk()); 
		assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

		doReturn(completedER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
		doReturn(getOkER()).when(daoHelper).update(any(), anyMap(), anyMap());
		entityResult = service.bookingActionUpdate(attrMap, getKeyMapOk()); 
		assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

		doReturn(confirmedER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
		doReturn(getOkER()).when(daoHelper).update(any(), anyMap(), anyMap());
		entityResult = service.bookingActionUpdate(attrMap, getKeyMapOk()); 
		assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());			

	}

	@Test
	@DisplayName("ACTION CHECKIN")
	void testBookingActionUpdateCheckIn() {
		EntityResult entityResult;
		Map<String, Object> attrMap  = getAttrMapActionCheckIn(); // ACTION
		
		doReturn(confirmedER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
		doReturn(getOkER()).when(daoHelper).update(any(), anyMap(), anyMap());
		entityResult = service.bookingActionUpdate(getAttrMapActionCancel(), getKeyMapOk()); // Action
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode(), entityResult.getMessage());
		
		doReturn(inProgressER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
		doReturn(getOkER()).when(daoHelper).update(any(), anyMap(), anyMap());
		entityResult = service.bookingActionUpdate(attrMap, getKeyMapOk()); 
		assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());
		
		doReturn(completedER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
		doReturn(getOkER()).when(daoHelper).update(any(), anyMap(), anyMap());
		entityResult = service.bookingActionUpdate(attrMap, getKeyMapOk()); 
		assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());
		
		doReturn(confirmedER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
		doReturn(getOkER()).when(daoHelper).update(any(), anyMap(), anyMap());
		entityResult = service.bookingActionUpdate(attrMap, getKeyMapOk()); 
		assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());			
		
	}

	@Test
	@DisplayName("ACTION CHECKOUT")
	void testBookingActionUpdateCheckout() {
		EntityResult entityResult;
		Map<String, Object> attrMap  = getAttrMapActionCheckout(); // ACTION
		
		doReturn(confirmedER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
		doReturn(getOkER()).when(daoHelper).update(any(), anyMap(), anyMap());
		entityResult = service.bookingActionUpdate(getAttrMapActionCancel(), getKeyMapOk()); // Action
		assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());
		
		doReturn(inProgressER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
		doReturn(getOkER()).when(daoHelper).update(any(), anyMap(), anyMap());
		entityResult = service.bookingActionUpdate(attrMap, getKeyMapOk()); 
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode(), entityResult.getMessage());
		
		doReturn(completedER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
		doReturn(getOkER()).when(daoHelper).update(any(), anyMap(), anyMap());
		entityResult = service.bookingActionUpdate(attrMap, getKeyMapOk()); 
		assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());
		
		doReturn(confirmedER()).when(daoHelper).query(any(), anyMap(), anyList());// estado del registro
		doReturn(getOkER()).when(daoHelper).update(any(), anyMap(), anyMap());
		entityResult = service.bookingActionUpdate(attrMap, getKeyMapOk()); 
		assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());			
		
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
	public class Status {

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
				put(BookingDao.ATTR_CANCELED, Tools.getYesterdayString());
			}
		});
		return er;
	}

	EntityResult completedER() {
		EntityResult er = new EntityResultMapImpl(getStatusColumnList());
		er.addRecord(new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_CHECKIN, Tools.getYesterdayString());
				put(BookingDao.ATTR_CHECKOUT, Tools.getTomorrowString());
				put(BookingDao.ATTR_CANCELED, null);
			}
		});
		return er;
	}

	EntityResult inProgressER() {
		EntityResult er = new EntityResultMapImpl(getStatusColumnList());
		er.addRecord(new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_CHECKIN, Tools.getYesterdayString());
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

//
//	@Test
//	void testBookingDaysUnitaryRoomPriceQuery() {
//		fail("Not yet implemented");
//	}

}

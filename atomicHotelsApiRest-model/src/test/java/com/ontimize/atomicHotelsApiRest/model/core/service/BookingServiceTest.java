package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
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
	BookingService serviceMock;

	@InjectMocks
	BookingService service;

	@Autowired
	BookingDao bookingDao;

	@Autowired
	ValidateFields vf;

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
	Map<String, Object> getKeyMapOk() {
		return new HashMap<>() {
			{
				put(BookingDao.ATTR_ID, 2);
			}
		};
	}

	Map<String, Object> getAttrMapOK() {
		return new HashMap<>() {
			{
				put(BookingDao.NON_ATTR_ACTION, "CANCEL");
			}
		};
	}

	@Test
	@DisplayName("Maps de entrada válidos")
	void testBookingActionUpdateOK() {		

		EntityResult entityResult = service.bookingActionUpdate(getAttrMapOK(), getKeyMapOk());
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode(),entityResult.getMessage());				
			try {
				doReturn(BookingDao.Status.CANCELED).when(serviceMock.getBookingStatus(getKeyMapOk().get(BookingDao.ATTR_ID)));
			} catch (EntityResultRequiredException e) {
				fail(e.toString());
			}
			
//			when(serviceMock.getBookingStatus(getKeyMapOk().get(BookingDao.ATTR_ID))).thenReturn(BookingDao.Status.CANCELED);
//			when(service.getBookingStatus(getKeyMapOk().get(BookingDao.ATTR_ID))).thenThrow(MissingFieldsException.class);
		
	}

	@Test
	@DisplayName("Maps de entrada no válidos")
	void testBookingActionUpdateKO() {
		// when(daoHelper.update(any(), anyMap(),
		// anyMap())).thenThrow(MissingFieldsException.class);
		EntityResult entityResult = service.bookingActionUpdate(new HashMap<String, Object>(),
				new HashMap<String, Object>());
		assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

//		entityResult = service.bookingActionUpdate(null, null);
//		assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

		entityResult = service.bookingActionUpdate(new HashMap<String, Object>(), getKeyMapOk());
		assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());

		entityResult = service.bookingActionUpdate(getAttrMapOK(), new HashMap<String, Object>());
		assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());
	}

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

	
	@Test
	void testGetBookingStatus() {
		List<String> columnList = Arrays.asList(BookingDao.ATTR_START,BookingDao.ATTR_END,BookingDao.ATTR_CHECKIN,BookingDao.ATTR_CHECKOUT,BookingDao.ATTR_CANCELED,BookingDao.ATTR_CREATED);
		EntityResult erCancel = new EntityResultMapImpl(columnList);
		erCancel.addRecord(new HashMap<String,Object>(){{
			 put(BookingDao.ATTR_START, tools.getYesterdayString());		 
		 }});
				
		
		fail("Not yet implemented");
	}
//
//	@Test
//	void testBookingDaysUnitaryRoomPriceQuery() {
//		fail("Not yet implemented");
//	}

}

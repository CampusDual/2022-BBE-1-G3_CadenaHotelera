    package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import static org.mockito.Mockito.verify;

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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingGuestDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlPermissions;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {
	
	@Mock
	DefaultOntimizeDaoHelper daoHelper;

//	@Mock
//	BookingService bookingServiceMock;
//
//	@Mock
//	CustomerService customerServiceMock;

	@Spy
	ControlFields cf;
	
//	@Spy
//	ControlPermissions permissions;

	@InjectMocks
	StatisticsService service;

	@Autowired
	HotelDao hotelDao;

	EntityResult eR;

	@Nested
	@DisplayName("Test for hotelMaximumCapacity queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class GuestCountQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testHotelMaximumCapacityQueryControlFieldsReset() {
			service.hotelMaximumCapacityQuery(TestingTools.getMapEmpty(), getColumsName());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		
		@Test
		@DisplayName("ControlFields usar validate() map y list") 
		void testHotelMaximumCapacityQueryControlFieldsValidateList() {
			service.hotelMaximumCapacityQuery(getHotelId(), getColumsName());
			try {
//				doNothing().when(cf).reset();
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields map")).validate(anyMap());
				//verify(cf, description("No se ha utilizado el metodo validate de ControlFields list")).validate(anyList());	
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}
		}
//
//		@Test
//		@DisplayName("Valores de entrada válidos")
//		void testGuestCountQueryOK() {
//			doReturn(getEntityResultPrecioServcioUnidadesTotal()).when(daoHelper).query(any(), anyMap(), anyList(),
//					anyString());
//			eR = service.guestCountQuery(getBookingId(),new ArrayList());
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());
//
//		}
////
//		@Test
//		@DisplayName("Valores de entrada NO válidos")
//		void testGuestCountQueryKO() {
//			try {
//				// lanzamos todas las excepciones de Validate para comprobar que están bien
//				// recojidas.
//				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
//				eR = service.guestCountQuery(TestingTools.getMapEmpty(), getColumsName());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
//				eR = service.guestCountQuery(TestingTools.getMapEmpty(), getColumsName());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
//				eR = service.guestCountQuery(TestingTools.getMapEmpty(), getColumsName());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
//				eR = service.guestCountQuery(TestingTools.getMapEmpty(), getColumsName());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
//				eR = service.guestCountQuery(TestingTools.getMapEmpty(), getColumsName());
//				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
//				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				fail("excepción no capturada: " + e.getMessage());
//			}
//
//		}

	}
	
	List<String> getColumsName() {
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
}
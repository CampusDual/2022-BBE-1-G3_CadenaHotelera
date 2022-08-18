    package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

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

	@Autowired
	HotelDao hotelDao;
	
	@Autowired
	ISQLQueryAdapter adapter;

	EntityResult eR;

	@Nested
	@DisplayName("Test for hotelMaximumCapacity queries")
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
		void testHotelMaximumCapacityQueryControlFieldsValidateList()  {
			
			try {
				 doNothing().when(cf).restricPermissions(anyMap());
				service.hotelMaximumCapacityQuery(getHotelId(), getColumsNameMaximumCapacity());
					
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields map")).validate(anyMap());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields list")).validate(anyList());	
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
			eR = service.hotelMaximumCapacityQuery(getHotelId(),new ArrayList());
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
	@DisplayName("Test for hotelOccupancyPercentage queries")
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
		void testHotelOccupancyPercentageQueryControlFieldsValidateList()  {
			
			try {
				 doNothing().when(cf).restricPermissions(anyMap());
				service.hotelOccupancyPercentageQuery(getFromTo(), getColumsNameOccupancyPercentage());
					
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields map")).validate(anyMap());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields list")).validate(anyList());	
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}
		} 


		@Test
		@DisplayName("Valores de entrada válidos")
		void testGuestCountQueryOK() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
			doReturn(getEntityResultHotelOccupancyPercentage()).when(daoHelper).query(any(), anyMap(), anyList(),
					anyString(),any(ISQLQueryAdapter.class));
			eR = service.hotelOccupancyPercentageQuery(getFromTo(),new ArrayList());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}
//		

		
		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testGuestCountQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyPercentageQuery(TestingTools.getMapEmpty(), getColumsNameOccupancyPercentage());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyPercentageQuery(TestingTools.getMapEmpty(), getColumsNameOccupancyPercentage());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyPercentageQuery(TestingTools.getMapEmpty(), getColumsNameOccupancyPercentage());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyPercentageQuery(TestingTools.getMapEmpty(), getColumsNameOccupancyPercentage());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.hotelOccupancyPercentageQuery(TestingTools.getMapEmpty(), getColumsNameOccupancyPercentage());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}
//
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
	
	EntityResult getEntityResultHotelMaximumCapacity() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(HotelDao.ATTR_ID,1);
				put(HotelDao.ATTR_NAME,"Hotel 1");
				put(HotelDao.ATTR_CITY,"Ciudad");
				put(HotelDao.ATTR_MAXIMUN_CAPACITY,new BigDecimal(100));

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
	
	EntityResult getEntityResultHotelOccupancyPercentage() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(HotelDao.ATTR_ID,1);
				put(HotelDao.ATTR_NAME,"Hotel 1");
				put(HotelDao.ATTR_CITY,"Ciudad");
				put("occupancy_percentage_in_date_range",new BigDecimal (12));
				put("capacity_in_date_range",100);
				put("occupancy_in_date_range",12);

			}

		});
		return er;
	}
}
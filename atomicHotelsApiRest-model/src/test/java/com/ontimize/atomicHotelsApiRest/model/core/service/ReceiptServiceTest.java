package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ReceiptDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {

	@Mock
	DefaultOntimizeDaoHelper daoHelper;
	
	@Mock
	BookingService bookingServiceMock;
	
	@Mock
	BookingServiceExtraService bookingServiceExtraServiceMock;

	@InjectMocks
	ReceiptService service;

	@Autowired
	ReceiptDao receiptDao;
	
	@Autowired
	BookingDao bookingDao;
	
	@Autowired
	ValidateFields vf;

	// @Mock/@Autowired/@InjectMocks
	MissingFieldsException e;

//	@Nested
//	@DisplayName("Test for Receipt queries")
//	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//	public class ReceiptQuery {
//
//		@Test
//		@DisplayName("Obtain all data from Receipt table")
//		void when_queryOnlyWithAllColumns_return_allHotelsData() {
//			doReturn(getAllHotelsData()).when(daoHelper).query(any(), anyMap(), anyList());
//			EntityResult entityResult = service.hotelQuery(new HashMap<>(), new ArrayList<>());
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
//			assertEquals(3, entityResult.calculateRecordNumber());
//		}
//
//		@Test
//		@DisplayName("Obtain all data columns from hotels table when htl_id is -> 2")
//		void when_queryAllColumns_return_specificData() {
//			HashMap<String, Object> keyMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, 2);
//				}
//			};
//			List<String> attrList = Arrays.asList(HotelDao.ATTR_ID, HotelDao.ATTR_NAME, HotelDao.ATTR_STREET,
//					HotelDao.ATTR_CITY, HotelDao.ATTR_CP, HotelDao.ATTR_STATE, HotelDao.ATTR_COUNTRY,
//					HotelDao.ATTR_PHONE, HotelDao.ATTR_PHONE, HotelDao.ATTR_EMAIL, HotelDao.ATTR_DESCRIPTION,
//					HotelDao.ATTR_IS_OPEN);
//			doReturn(getSpecificHotelData(keyMap, attrList)).when(daoHelper).query(any(), anyMap(), anyList());
//			EntityResult entityResult = service.hotelQuery(new HashMap<>(), new ArrayList<>());
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
//			assertEquals(1, entityResult.calculateRecordNumber());
//			assertEquals(2, entityResult.getRecordValues(0).get(HotelDao.ATTR_ID));
//		}
//
//		@Test
//		@DisplayName("Obtain all data from Hotel table using a personalized query")
//		void when_queryOnlyWithAllColumns_return_allHotelsData_fromPersonalizedQuery() {
//			HashMap<String, Object> keyMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, 2);
//				}
//			};
//			List<String> attrList = Arrays.asList(HotelDao.ATTR_ID, HotelDao.ATTR_NAME, HotelDao.ATTR_STREET,
//					HotelDao.ATTR_CITY, HotelDao.ATTR_CP, HotelDao.ATTR_STATE, HotelDao.ATTR_COUNTRY,
//					HotelDao.ATTR_PHONE, HotelDao.ATTR_PHONE, HotelDao.ATTR_EMAIL, HotelDao.ATTR_DESCRIPTION,
//					HotelDao.ATTR_IS_OPEN);
//			
//			doReturn(getSpecificHotelData(keyMap, attrList)).when(daoHelper).query(any(), anyMap(), anyList(),anyString());
//			EntityResult entityResult = service.hotelDataQuery(new HashMap<>(), new ArrayList<>());
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
//			assertEquals(1, entityResult.calculateRecordNumber());
//			assertEquals(2, entityResult.getRecordValues(0).get(HotelDao.ATTR_ID));
//		}
//
//		@Test
//		@DisplayName("Obtain all data columns from hotels table when htl_id not exist")
//		void when_queryAllColumnsNotExisting_return_empty() {
//			HashMap<String, Object> keyMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, 5);
//				}
//			};
//			List<String> attrList = Arrays.asList(HotelDao.ATTR_ID, HotelDao.ATTR_NAME, HotelDao.ATTR_STREET,
//					HotelDao.ATTR_CITY, HotelDao.ATTR_CP, HotelDao.ATTR_STATE, HotelDao.ATTR_COUNTRY,
//					HotelDao.ATTR_PHONE, HotelDao.ATTR_PHONE, HotelDao.ATTR_EMAIL, HotelDao.ATTR_DESCRIPTION,
//					HotelDao.ATTR_IS_OPEN);
//			when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificHotelData(keyMap, attrList));
//			EntityResult entityResult = service.hotelQuery(new HashMap<>(), new ArrayList<>());
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
//			assertEquals(0, entityResult.calculateRecordNumber());
//		}
//
//		@ParameterizedTest(name = "Obtain data with htl_id -> {1}")
//		@MethodSource("randomIDGenerator")
//		@DisplayName("Obtain all data columns from hotels table when htl_id is random")
//		void when_queryAllColumnsWithRandomValue_return_specificData(int random) {
//			HashMap<String, Object> keyMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, random);
//				}
//			};
//			List<String> attrList = Arrays.asList(HotelDao.ATTR_ID, HotelDao.ATTR_NAME, HotelDao.ATTR_STREET,
//					HotelDao.ATTR_CITY, HotelDao.ATTR_CP, HotelDao.ATTR_STATE, HotelDao.ATTR_COUNTRY,
//					HotelDao.ATTR_PHONE, HotelDao.ATTR_PHONE, HotelDao.ATTR_EMAIL, HotelDao.ATTR_DESCRIPTION,
//					HotelDao.ATTR_IS_OPEN);
//			when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificHotelData(keyMap, attrList));
//			EntityResult entityResult = service.hotelQuery(new HashMap<>(), new ArrayList<>());
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
//			assertEquals(1, entityResult.calculateRecordNumber());
//			assertEquals(random, entityResult.getRecordValues(0).get(HotelDao.ATTR_ID));
//		}
//
//		public EntityResult getAllReceiptsData() {
//			List<String> columnList = Arrays.asList(ReceiptDao.ATTR_ID, ReceiptDao.ATTR_BOOKING_ID,ReceiptDao.ATTR_DATE,ReceiptDao.ATTR_DIAS,ReceiptDao.ATTR_TOTAL_ROOM,ReceiptDao.ATTR_TOTAL_SERVICES,ReceiptDao.ATTR_TOTAL);
//			EntityResult er = new EntityResultMapImpl(columnList);
//			er.addRecord(new HashMap<String, Object>() {
//				{
//					put(ReceiptDao.ATTR_ID, 1);
//					put(ReceiptDao.ATTR_BOOKING_ID, 2);
//					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
//					put(ReceiptDao.ATTR_DIAS, 3);
//					put(ReceiptDao.ATTR_TOTAL_ROOM, 200.15);
//					put(ReceiptDao.ATTR_TOTAL_SERVICES, 300.15);
//					put(ReceiptDao.ATTR_TOTAL, 500.30);
//				}
//			});
//			er.addRecord(new HashMap<String, Object>() {
//				{
//					put(ReceiptDao.ATTR_ID, 2);
//					put(ReceiptDao.ATTR_BOOKING_ID, 4);
//					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
//					put(ReceiptDao.ATTR_DIAS, 3);
//					put(ReceiptDao.ATTR_TOTAL_ROOM, 200.15);
//					put(ReceiptDao.ATTR_TOTAL_SERVICES, 300.15);
//					put(ReceiptDao.ATTR_TOTAL, 500.30);
//				}
//			});
//			er.addRecord(new HashMap<String, Object>() {
//				{
//					put(ReceiptDao.ATTR_ID, 3);
//					put(ReceiptDao.ATTR_BOOKING_ID, );
//					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
//					put(ReceiptDao.ATTR_DIAS, 3);
//					put(ReceiptDao.ATTR_TOTAL_ROOM, 200.15);
//					put(ReceiptDao.ATTR_TOTAL_SERVICES, 300.15);
//					put(ReceiptDao.ATTR_TOTAL, 500.30);
//				}
//			});
//			er.setCode(EntityResult.OPERATION_SUCCESSFUL);
//			er.setColumnSQLTypes(new HashMap<String, Number>() {
//				{
//					put(ReceiptDao.ATTR_ID, Types.INTEGER);
//					put(ReceiptDao.ATTR_BOOKING_ID, Types.INTEGER);
//					put(ReceiptDao.ATTR_DATE, Types.TIMESTAMP);
//					put(ReceiptDao.ATTR_DIAS, Types.INTEGER);
//					put(ReceiptDao.ATTR_TOTAL_ROOM, Types.NUMERIC);
//					put(ReceiptDao.ATTR_TOTAL_SERVICES, Types.NUMERIC);
//					put(ReceiptDao.ATTR_TOTAL, Types.NUMERIC);
//				}
//			});
//			return er;
//		}
//
//		public EntityResult getSpecificHotelData(Map<String, Object> keyValues, List<String> attributes) {
//			EntityResult allData = this.getAllReceiptsData();
//			int recordIndex = allData.getRecordIndex(keyValues);
//			HashMap<String, Object> recordValues = (HashMap) allData.getRecordValues(recordIndex);
//			List<String> columnList = Arrays.asList(ReceiptDao.ATTR_ID, ReceiptDao.ATTR_BOOKING_ID,ReceiptDao.ATTR_DATE,ReceiptDao.ATTR_DIAS,ReceiptDao.ATTR_TOTAL_ROOM,ReceiptDao.ATTR_TOTAL_SERVICES,ReceiptDao.ATTR_TOTAL);
//			EntityResult er = new EntityResultMapImpl(columnList);
//			if (recordValues != null) {
//				er.addRecord(recordValues);
//			}
//			er.setCode(EntityResult.OPERATION_SUCCESSFUL);
//			er.setColumnSQLTypes(new HashMap<String, Number>() {
//				{
//					put(ReceiptDao.ATTR_ID, Types.INTEGER);
//					put(ReceiptDao.ATTR_BOOKING_ID, Types.INTEGER);
//					put(ReceiptDao.ATTR_DATE, Types.TIMESTAMP);
//					put(ReceiptDao.ATTR_DIAS, Types.INTEGER);
//					put(ReceiptDao.ATTR_TOTAL_ROOM, Types.NUMERIC);
//					put(ReceiptDao.ATTR_TOTAL_SERVICES, Types.NUMERIC);
//					put(ReceiptDao.ATTR_TOTAL, Types.NUMERIC);
//				}
//			});
//			return er;
//		}
//
//		List<Integer> randomIDGenerator() {
//			List<Integer> list = new ArrayList<>();
//			for (int i = 0; i < 5; i++) {
//				list.add(ThreadLocalRandom.current().nextInt(1, 4));
//			}
//			return list;
//		}
//
//	}

	@Nested
	@DisplayName("Test for Receipt inserts")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class InsertQuery {

		@Test
		@DisplayName("Insert Receipt")
		void when_hotel_insert_is_succsessfull() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ReceiptDao.ATTR_ID, 1);
					put(ReceiptDao.ATTR_BOOKING_ID, 2);
					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
					put(ReceiptDao.ATTR_DIAS, 3);
					put(ReceiptDao.ATTR_TOTAL_ROOM, 200.15);
					put(ReceiptDao.ATTR_TOTAL_SERVICES, 300.15);
					put(ReceiptDao.ATTR_TOTAL, 500.30);
				}
			};
			EntityResult resultado = new EntityResultMapImpl();
			resultado.addRecord(attrMap);
			resultado.setCode(EntityResult.OPERATION_SUCCESSFUL);
			resultado.setMessage("Receipt registrada");
			when(daoHelper.insert(any(), anyMap())).thenReturn(resultado);		
			try {				
				when(bookingServiceMock.getBookingStatus(any())).thenReturn(BookingDao.Status.COMPLETED);
				when(bookingServiceMock.bookingDaysUnitaryRoomPriceQuery(anyMap(),anyList())).thenReturn(getAllBookingsRoomPriceDaysData());
				when(bookingServiceExtraServiceMock.bookingExtraServicePriceUnitsTotalQuery(anyMap(),anyList())).thenReturn(getAllBookingsRoomPriceDaysData());
			} catch (EntityResultRequiredException e) {
				fail("Err");
				e.printStackTrace();
			}
			EntityResult entityResult = service.receiptInsert(attrMap);
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals("Receipt registrada",entityResult.getMessage());
		}

		@Test
		@DisplayName("Duplicated Key")
		void when_already_exist() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
//					put(ReceiptDao.ATTR_ID, 1);
					put(ReceiptDao.ATTR_BOOKING_ID, 2);
//					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
//					put(ReceiptDao.ATTR_DIAS, 3);
//					put(ReceiptDao.ATTR_TOTAL_ROOM, 200.15);
//					put(ReceiptDao.ATTR_TOTAL_SERVICES, 300.15);
//					put(ReceiptDao.ATTR_TOTAL, 500.30);
				}
			};
			when(daoHelper.insert(any(), anyMap())).thenThrow(DuplicateKeyException.class);
			EntityResult entityResult = service.receiptInsert(attrMap);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(entityResult.getMessage(), ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}

//		@Test
//		@DisplayName("Missing Fields")
//		void when_unable_insert() {
//			Map<String, Object> attrMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, 1);
//					put(HotelDao.ATTR_NAME, null);
//					put(HotelDao.ATTR_STREET, "Avenida Sin Nombre Nº 1");
//					put(HotelDao.ATTR_CITY, "Vigo");
//					put(HotelDao.ATTR_CP, "36211");
//					put(HotelDao.ATTR_STATE, "Galicia");
//					put(HotelDao.ATTR_COUNTRY, "Spain");
//					put(HotelDao.ATTR_PHONE, "+34 986 111 111");
//					put(HotelDao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//					put(HotelDao.ATTR_DESCRIPTION, "Faltan campos no nullables");
//					put(HotelDao.ATTR_IS_OPEN, 1);
//				}
//			};
//			//try (MockedStatic<ValidateFields> vf = Mockito.mockStatic(ValidateFields.class)) {
//				//vf.when(() -> ValidateFields.required(anyMap(), anyString())).thenThrow(MissingFieldsException.class);
//				EntityResult entityResult = service.hotelInsert(attrMap);
//				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//				assertEquals(ErrorMessage.CREATION_ERROR + "El campo " + HotelDao.ATTR_NAME + " es nulo",entityResult.getMessage());
//			//}
//
////			doThrow().when(ValidateFields.required(anyMap(), anyString())).thenThrow(MissingFieldsException.class);
////			when(daoHelper.insert(any(),anyMap())).thenThrow(new MissingFieldsException("El campo " + HotelDao.ATTR_NAME + " es nulo"));
////    		entityResult = service.hotelInsert(anyMap());
////			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//
//		}
		
		public EntityResult getSpecificBookingRoomPriceDaysData(Map<String, Object> keyValues, List<String> attributes) {
		EntityResult allData = this.getAllBookingsRoomPriceDaysData();
		int recordIndex = allData.getRecordIndex(keyValues);
		HashMap<String, Object> recordValues = (HashMap) allData.getRecordValues(recordIndex);
		List<String> columnList = Arrays.asList(BookingDao.ATTR_ID, RoomTypeDao.ATTR_PRICE,ReceiptDao.ATTR_DIAS);
		EntityResult er = new EntityResultMapImpl(columnList);
		if (recordValues != null) {
			er.addRecord(recordValues);
		}
		er.setCode(EntityResult.OPERATION_SUCCESSFUL);
		er.setColumnSQLTypes(new HashMap<String, Number>() {
			{
				put(BookingDao.ATTR_ID, Types.INTEGER);
				put(RoomTypeDao.ATTR_PRICE, Types.NUMERIC);
				put(ReceiptDao.ATTR_DIAS, Types.INTEGER);
			}
		});
		return er;
	}
		
		public EntityResult getAllBookingsRoomPriceDaysData() {
		List<String> columnList = Arrays.asList(BookingDao.ATTR_ID, RoomTypeDao.ATTR_PRICE,ReceiptDao.ATTR_DIAS);
		EntityResult er = new EntityResultMapImpl(columnList);
		er.addRecord(new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_ID, 1);
				put(RoomTypeDao.ATTR_PRICE, 20.21);
				put(ReceiptDao.ATTR_DIAS, 3);
			}
		});
		
		er.setCode(EntityResult.OPERATION_SUCCESSFUL);
		er.setColumnSQLTypes(new HashMap<String, Number>() {
			{
				put(ReceiptDao.ATTR_ID, Types.INTEGER);
				put(RoomTypeDao.ATTR_PRICE, Types.NUMERIC);
			}
		});
		return er;
	}

//
//	@Nested
//	@DisplayName("Test for Hotel updates")
//	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//	public class UpdateQuery {
//
//		@Test
//		@DisplayName("Update Hotel")
//		void when_hotel_insert_is_succsessfull() {
//			Map<String, Object> attrMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, 1);
//				}
//			};
//			Map<String, Object> keyMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, 1);
//					put(HotelDao.ATTR_NAME, "Hotel 1 actualizado");
//					put(HotelDao.ATTR_STREET, "Avenida Sin Nombre Nº 1 actualizado");
//					put(HotelDao.ATTR_CITY, "Vigo actualizado");
//					put(HotelDao.ATTR_CP, "36211 actualizado");
//					put(HotelDao.ATTR_STATE, "Galicia");
//					put(HotelDao.ATTR_COUNTRY, "Spain");
//					put(HotelDao.ATTR_PHONE, "+34 986 111 111");
//					put(HotelDao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//					put(HotelDao.ATTR_DESCRIPTION, "Hotel actualizado");
//					put(HotelDao.ATTR_IS_OPEN, 0);
//				}
//			};
//			EntityResult resultado = new EntityResultMapImpl();
//			resultado.addRecord(keyMap);
//			resultado.setCode(EntityResult.OPERATION_SUCCESSFUL);
//			resultado.setMessage("Hotel actualizado");
//
//			when(daoHelper.update(any(), anyMap(), anyMap())).thenReturn(resultado);
//			EntityResult entityResult = service.hotelUpdate(attrMap, keyMap);
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
//			assertEquals(entityResult.getMessage(), "Hotel actualizado");
//		}
//
//		@Test
//		@DisplayName("Duplicated Key")
//		void when_already_exist() {
//			Map<String, Object> attrMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, 2);
//				}
//			};
//			Map<String, Object> keyMap = new HashMap<>() {
//				{
//					put(HotelDao.ATTR_ID, 2);// ???
//					put(HotelDao.ATTR_NAME, "Hotel 1");// Este hotel ya existe
//					put(HotelDao.ATTR_STREET, "Avenida Sin Nombre Nº 1 actualizado");
//					put(HotelDao.ATTR_CITY, "Vigo actualizado");
//					put(HotelDao.ATTR_CP, "36211 actualizado");
//					put(HotelDao.ATTR_STATE, "Galicia");
//					put(HotelDao.ATTR_COUNTRY, "Spain");
//					put(HotelDao.ATTR_PHONE, "+34 986 111 111");
//					put(HotelDao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//					put(HotelDao.ATTR_DESCRIPTION, "Hotel actualizado");
//					put(HotelDao.ATTR_IS_OPEN, 1);
//				}
//			};
//			when(daoHelper.update(any(), anyMap(), anyMap())).thenThrow(DuplicateKeyException.class);
//			EntityResult entityResult = service.hotelUpdate(attrMap, keyMap);
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//			assertEquals(entityResult.getMessage(), ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
//		}

//    	@Test
//		@DisplayName("Missing Fields")
//		void when_unable_insert() {
//			when(daoHelper.insert(any(),anyMap())).thenThrow(MissingFieldsException.class);
//			Map<String, Object> attrMap = new HashMap<>() {{
//				put(HotelDao.ATTR_ID, 1);
////                put(HotelDao.ATTR_NAME, "Hotel 23");
////                put(HotelDao.ATTR_STREET, "Avenida Sin Nombre Nº 1");
//                put(HotelDao.ATTR_CITY, "Vigo");
//                put(HotelDao.ATTR_CP, "36211");
//                put(HotelDao.ATTR_STATE, "Galicia");
//                put(HotelDao.ATTR_COUNTRY, "Spain");
//                put(HotelDao.ATTR_PHONE, "+34 986 111 111");
//                put(HotelDao.ATTR_EMAIL, "hotel1@atomicHotels.com");
//                put(HotelDao.ATTR_DESCRIPTION, "Faltan campos no nullables");
//                put(HotelDao.ATTR_IS_OPEN, 1);	
//			}};			
//			EntityResult entityResult = service.hotelInsert(attrMap);
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//    		assertEquals(entityResult.getMessage(), ErrorMessage.CREATION_ERROR+e.getMessage());
//    	}
	}

}

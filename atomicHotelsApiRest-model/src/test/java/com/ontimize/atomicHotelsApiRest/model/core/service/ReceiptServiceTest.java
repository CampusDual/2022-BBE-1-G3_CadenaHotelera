package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingServiceExtraDao;
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

	@Nested
	@DisplayName("Test for Receipt queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class ReceiptQuery {


		@Test
		@DisplayName("Obtain all data columns from receipts table when rcp_id is -> 2")
		void when_queryAllColumns_return_specificData() {
			Map<String, Object> keyMap = new HashMap() {
				{
					put(ReceiptDao.ATTR_ID, 2);
				}
			};
			List<String> attrList = Arrays.asList(ReceiptDao.ATTR_ID, ReceiptDao.ATTR_BOOKING_ID,ReceiptDao.ATTR_DATE,ReceiptDao.ATTR_DIAS,ReceiptDao.ATTR_TOTAL_ROOM,ReceiptDao.ATTR_TOTAL_SERVICES,ReceiptDao.ATTR_TOTAL);
			when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificReceiptData(keyMap, attrList));
			EntityResult entityResult = service.receiptQuery(keyMap,attrList);
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(1, entityResult.calculateRecordNumber());
			assertEquals(2, entityResult.getRecordValues(0).get(ReceiptDao.ATTR_ID));
		}

		@Test
		@DisplayName("Obtain all data columns from receipts table when rcp_id not exist")
		void when_queryAllColumnsNotExisting_return_empty() {
			HashMap<String, Object> keyMap = new HashMap<>() {
				{
					put(ReceiptDao.ATTR_ID, 5);
				}
			};
			List<String> attrList = Arrays.asList(ReceiptDao.ATTR_ID, ReceiptDao.ATTR_BOOKING_ID,ReceiptDao.ATTR_DATE,ReceiptDao.ATTR_DIAS,ReceiptDao.ATTR_TOTAL_ROOM,ReceiptDao.ATTR_TOTAL_SERVICES,ReceiptDao.ATTR_TOTAL);
			when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificReceiptData(keyMap, attrList));
			EntityResult entityResult = service.receiptQuery(keyMap, attrList);
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(0, entityResult.calculateRecordNumber());
		}

		@ParameterizedTest(name = "Obtain data with rcp_id -> {1}")
		@MethodSource("randomIDGenerator")
		@DisplayName("Obtain all data columns from receipt table when rcp_id is random")
		void when_queryAllColumnsWithRandomValue_return_specificData(int random) {
			HashMap<String, Object> keyMap = new HashMap<>() {
				{
					put(ReceiptDao.ATTR_ID, random);
				}
			};
			List<String> attrList = Arrays.asList(ReceiptDao.ATTR_ID, ReceiptDao.ATTR_BOOKING_ID,ReceiptDao.ATTR_DATE,ReceiptDao.ATTR_DIAS,ReceiptDao.ATTR_TOTAL_ROOM,ReceiptDao.ATTR_TOTAL_SERVICES,ReceiptDao.ATTR_TOTAL);
			when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificReceiptData(keyMap, attrList));
			EntityResult entityResult = service.receiptQuery(keyMap, attrList);
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(1, entityResult.calculateRecordNumber());
			assertEquals(random, entityResult.getRecordValues(0).get(ReceiptDao.ATTR_ID));
		}
		
		@Test
		@DisplayName("Missing Fields")
		void when_Missings_Fields() {
			List<String> attrList = Arrays.asList(ReceiptDao.ATTR_ID, ReceiptDao.ATTR_BOOKING_ID,ReceiptDao.ATTR_DATE,ReceiptDao.ATTR_DIAS,ReceiptDao.ATTR_TOTAL_ROOM,ReceiptDao.ATTR_TOTAL_SERVICES,ReceiptDao.ATTR_TOTAL);
			EntityResult entityResult = service.receiptQuery(new HashMap<>(), attrList);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(ErrorMessage.REQUIRED_FIELDS, entityResult.getMessage());
			assertEquals(0, entityResult.calculateRecordNumber());
		}
		
		@Test
		@DisplayName("Missing Columns")
		void when_missing_columns() {
			HashMap<String, Object> keyMap = new HashMap<>() {
				{
					put(ReceiptDao.ATTR_ID, 1);
				}
			};
			EntityResult entityResult = service.receiptQuery(keyMap, new ArrayList<>());
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(ErrorMessage.REQUIRED_COLUMNS, entityResult.getMessage());
			assertEquals(0, entityResult.calculateRecordNumber());
		}
		

		@Test
		@DisplayName("Invalid Fields Value")
		void when_invalid_fields_value() {
			HashMap<String, Object> keyMap = new HashMap<>() {
				{
					put(ReceiptDao.ATTR_ID, "1");
				}
			};
			List<String> attrList = Arrays.asList(ReceiptDao.ATTR_ID, ReceiptDao.ATTR_BOOKING_ID,ReceiptDao.ATTR_DATE,ReceiptDao.ATTR_DIAS,ReceiptDao.ATTR_TOTAL_ROOM,ReceiptDao.ATTR_TOTAL_SERVICES,ReceiptDao.ATTR_TOTAL);
			EntityResult entityResult = service.receiptQuery(keyMap, attrList);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(ErrorMessage.WRONG_TYPE + " - "+ReceiptDao.ATTR_ID, entityResult.getMessage());
			assertEquals(0, entityResult.calculateRecordNumber());
		}

		public EntityResult getAllReceiptsData() {
			List<String> columnList = Arrays.asList(ReceiptDao.ATTR_ID, ReceiptDao.ATTR_BOOKING_ID,
					ReceiptDao.ATTR_DATE, ReceiptDao.ATTR_DIAS, ReceiptDao.ATTR_TOTAL_ROOM,
					ReceiptDao.ATTR_TOTAL_SERVICES, ReceiptDao.ATTR_TOTAL);
			EntityResult er = new EntityResultMapImpl(columnList);
			er.addRecord(new HashMap<String, Object>() {
				{
					put(ReceiptDao.ATTR_ID, 1);
					put(ReceiptDao.ATTR_BOOKING_ID, 2);
					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
					put(ReceiptDao.ATTR_DIAS, 3);
					put(ReceiptDao.ATTR_TOTAL_ROOM, new BigDecimal(200.15));
					put(ReceiptDao.ATTR_TOTAL_SERVICES, new BigDecimal(300.15));
					put(ReceiptDao.ATTR_TOTAL, new BigDecimal(500.30));
				}
			});
			er.addRecord(new HashMap<String, Object>() {
				{
					put(ReceiptDao.ATTR_ID, 2);
					put(ReceiptDao.ATTR_BOOKING_ID, 4);
					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
					put(ReceiptDao.ATTR_DIAS, 3);
					put(ReceiptDao.ATTR_TOTAL_ROOM, new BigDecimal(200.15));
					put(ReceiptDao.ATTR_TOTAL_SERVICES, new BigDecimal(300.15));
					put(ReceiptDao.ATTR_TOTAL, new BigDecimal(500.30));
				}
			});
			er.addRecord(new HashMap<String, Object>() {
				{
					put(ReceiptDao.ATTR_ID, 3);
					put(ReceiptDao.ATTR_BOOKING_ID, 5);
					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
					put(ReceiptDao.ATTR_DIAS, 3);
					put(ReceiptDao.ATTR_TOTAL_ROOM, new BigDecimal(200.15));
					put(ReceiptDao.ATTR_TOTAL_SERVICES, new BigDecimal(300.15));
					put(ReceiptDao.ATTR_TOTAL, new BigDecimal(500.30));
				}
			});
			er.setCode(EntityResult.OPERATION_SUCCESSFUL);
			er.setColumnSQLTypes(new HashMap<String, Number>() {
				{
					put(ReceiptDao.ATTR_ID, Types.INTEGER);
					put(ReceiptDao.ATTR_BOOKING_ID, Types.INTEGER);
					put(ReceiptDao.ATTR_DATE, Types.TIMESTAMP);
					put(ReceiptDao.ATTR_DIAS, Types.INTEGER);
					put(ReceiptDao.ATTR_TOTAL_ROOM, Types.NUMERIC);
					put(ReceiptDao.ATTR_TOTAL_SERVICES, Types.NUMERIC);
					put(ReceiptDao.ATTR_TOTAL, Types.NUMERIC);
				}
			});
			return er;
		}

		public EntityResult getSpecificReceiptData(Map<String, Object> keyValues, List<String> attributes) {
			EntityResult allData = this.getAllReceiptsData();
			int recordIndex = allData.getRecordIndex(keyValues);
			HashMap<String, Object> recordValues = (HashMap) allData.getRecordValues(recordIndex);
			List<String> columnList = Arrays.asList(ReceiptDao.ATTR_ID, ReceiptDao.ATTR_BOOKING_ID,
					ReceiptDao.ATTR_DATE, ReceiptDao.ATTR_DIAS, ReceiptDao.ATTR_TOTAL_ROOM,
					ReceiptDao.ATTR_TOTAL_SERVICES, ReceiptDao.ATTR_TOTAL);
			EntityResult er = new EntityResultMapImpl(columnList);
			if (recordValues != null) {
				er.addRecord(recordValues);
			}
			er.setCode(EntityResult.OPERATION_SUCCESSFUL);
			er.setColumnSQLTypes(new HashMap<String, Number>() {
				{
					put(ReceiptDao.ATTR_ID, Types.INTEGER);
					put(ReceiptDao.ATTR_BOOKING_ID, Types.INTEGER);
					put(ReceiptDao.ATTR_DATE, Types.TIMESTAMP);
					put(ReceiptDao.ATTR_DIAS, Types.INTEGER);
					put(ReceiptDao.ATTR_TOTAL_ROOM, Types.NUMERIC);
					put(ReceiptDao.ATTR_TOTAL_SERVICES, Types.NUMERIC);
					put(ReceiptDao.ATTR_TOTAL, Types.NUMERIC);
				}
			});
			return er;
		}

		List<Integer> randomIDGenerator() {
			List<Integer> list = new ArrayList<>();
			for (int i = 0; i < 5; i++) {
				list.add(ThreadLocalRandom.current().nextInt(1, 4));
			}
			return list;
		}

	}

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
					put(ReceiptDao.ATTR_TOTAL_ROOM, new BigDecimal(200.15));
					put(ReceiptDao.ATTR_TOTAL_SERVICES, new BigDecimal(300.15));
					put(ReceiptDao.ATTR_TOTAL, new BigDecimal(500.30));
				}
			};
			EntityResult resultado = new EntityResultMapImpl();
			resultado.addRecord(attrMap);
			resultado.setCode(EntityResult.OPERATION_SUCCESSFUL);
			resultado.setMessage("Receipt registrada");
			when(daoHelper.insert(any(), anyMap())).thenReturn(resultado);
			try {
				when(bookingServiceMock.getBookingStatus(any())).thenReturn(BookingDao.Status.COMPLETED);
				when(bookingServiceMock.bookingDaysUnitaryRoomPriceQuery(anyMap(), anyList()))
						.thenReturn(getBookingDaysRoomPrice());
				when(bookingServiceExtraServiceMock.bookingExtraServicePriceUnitsTotalQuery(anyMap(), anyList()))
						.thenReturn(getBookingExtraServiceUnitsPriceTotal());
			} catch (EntityResultRequiredException e) {
				fail("Err");
				e.printStackTrace();
			}
			EntityResult entityResult = service.receiptInsert(attrMap);
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals("Receipt registrada", entityResult.getMessage());
		}
		

		@Test
		@DisplayName("Duplicated Key")
		void when_already_exist() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ReceiptDao.ATTR_ID, 1); // Si ya está guardado en la base datos
					put(ReceiptDao.ATTR_BOOKING_ID, 2);
					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
					put(ReceiptDao.ATTR_DIAS, 3);
					put(ReceiptDao.ATTR_TOTAL_ROOM, new BigDecimal(200.15));
					put(ReceiptDao.ATTR_TOTAL_SERVICES, new BigDecimal(300.15));
					put(ReceiptDao.ATTR_TOTAL, new BigDecimal(500.30));
				}
			};

			try {
				when(bookingServiceMock.getBookingStatus(any())).thenReturn(BookingDao.Status.COMPLETED);
				when(bookingServiceMock.bookingDaysUnitaryRoomPriceQuery(anyMap(), anyList()))
						.thenReturn(getBookingDaysRoomPrice());
				when(bookingServiceExtraServiceMock.bookingExtraServicePriceUnitsTotalQuery(anyMap(), anyList()))
						.thenReturn(getBookingExtraServiceUnitsPriceTotal());
			} catch (EntityResultRequiredException e) {
				fail("Err");
				e.printStackTrace();
			}
			when(daoHelper.insert(any(), anyMap())).thenThrow(DuplicateKeyException.class);
			EntityResult entityResult = service.receiptInsert(attrMap);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD, entityResult.getMessage());
		}

		@Test
		@DisplayName("Missing Fields - Null")
		void when_missing_fields_null() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ReceiptDao.ATTR_ID, 1);
					put(ReceiptDao.ATTR_BOOKING_ID, null);
					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
					put(ReceiptDao.ATTR_DIAS, 3);
					put(ReceiptDao.ATTR_TOTAL_ROOM, new BigDecimal(200.15));
					put(ReceiptDao.ATTR_TOTAL_SERVICES, new BigDecimal(300.15));
					put(ReceiptDao.ATTR_TOTAL, new BigDecimal(500.30));
				}
			};

			EntityResult entityResult = service.receiptInsert(attrMap);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(ErrorMessage.CREATION_ERROR + "-El campo " + ReceiptDao.ATTR_BOOKING_ID + " es nulo",
					entityResult.getMessage());

		}

		@Test
		@DisplayName("Missing Fields - No field")
		void when_missing_fields() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ReceiptDao.ATTR_ID, 1);
//					put(ReceiptDao.ATTR_BOOKING_ID, 3); //No se inserta un campo obligatorio
					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
					put(ReceiptDao.ATTR_DIAS, 3);
					put(ReceiptDao.ATTR_TOTAL_ROOM, new BigDecimal(200.15));
					put(ReceiptDao.ATTR_TOTAL_SERVICES, new BigDecimal(300.15));
					put(ReceiptDao.ATTR_TOTAL, new BigDecimal(500.30));
				}
			};

			EntityResult entityResult = service.receiptInsert(attrMap);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(ErrorMessage.CREATION_ERROR + "-Falta el campo " + ReceiptDao.ATTR_BOOKING_ID,
					entityResult.getMessage());

		}

		@Test
		@DisplayName("Missing Foreing Key")
		void when_missing_foreing_key() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ReceiptDao.ATTR_ID, 1);
					put(ReceiptDao.ATTR_BOOKING_ID, 100);// Si no existe esta reserva en la tabla bookings
					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
					put(ReceiptDao.ATTR_DIAS, 3);
					put(ReceiptDao.ATTR_TOTAL_ROOM, new BigDecimal(200.15));
					put(ReceiptDao.ATTR_TOTAL_SERVICES, new BigDecimal(300.15));
					put(ReceiptDao.ATTR_TOTAL, new BigDecimal(500.30));
				}
			};

			try {
				when(bookingServiceMock.getBookingStatus(any())).thenReturn(BookingDao.Status.COMPLETED);
				when(bookingServiceMock.bookingDaysUnitaryRoomPriceQuery(anyMap(), anyList()))
						.thenReturn(getBookingDaysRoomPrice());
				when(bookingServiceExtraServiceMock.bookingExtraServicePriceUnitsTotalQuery(anyMap(), anyList()))
						.thenReturn(getBookingExtraServiceUnitsPriceTotal());
			} catch (EntityResultRequiredException e) {
				fail("Err");
				e.printStackTrace();
			}
			when(daoHelper.insert(any(), anyMap())).thenThrow(DataIntegrityViolationException.class);
			EntityResult entityResult = service.receiptInsert(attrMap);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(ErrorMessage.CREATION_ERROR_MISSING_FK, entityResult.getMessage());
		}

		EntityResult getBookingDaysRoomPrice() {
			List<String> columnList = Arrays.asList(BookingDao.ATTR_ID, RoomTypeDao.ATTR_PRICE, ReceiptDao.ATTR_DIAS);
			EntityResult er = new EntityResultMapImpl(columnList);
			er.addRecord(new HashMap<String, Object>() {
				{
					put(BookingDao.ATTR_ID, 1);
					put(RoomTypeDao.ATTR_PRICE, new BigDecimal(20.21));
					put(ReceiptDao.ATTR_DIAS, 3);
				}
			});
			return er;
		}

		@Test
		@DisplayName("Invalid Fields")
		void when_invalid_fields() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ReceiptDao.ATTR_ID, 1);
					put(ReceiptDao.ATTR_BOOKING_ID, "Esto debería ser un int");
					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
					put(ReceiptDao.ATTR_DIAS, 3);
					put(ReceiptDao.ATTR_TOTAL_ROOM, new BigDecimal(200.15));
					put(ReceiptDao.ATTR_TOTAL_SERVICES, new BigDecimal(300.15));
					put(ReceiptDao.ATTR_TOTAL, new BigDecimal(500.30));
				}
			};

			EntityResult entityResult = service.receiptInsert(attrMap);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(
					ErrorMessage.CREATION_ERROR + "-" + ErrorMessage.WRONG_TYPE + " - " + ReceiptDao.ATTR_BOOKING_ID,
					entityResult.getMessage());

		}

		EntityResult getBookingExtraServiceUnitsPriceTotal() {
			List<String> columnList = Arrays.asList(BookingServiceExtraDao.ATTR_ID_BKG,
					BookingServiceExtraDao.ATTR_ID_UNITS, BookingServiceExtraDao.ATTR_PRECIO, "total");
			EntityResult er = new EntityResultMapImpl(columnList);
			er.addRecord(new HashMap<String, Object>() {
				{
					put(BookingServiceExtraDao.ATTR_ID_BKG, 1);
					put(BookingServiceExtraDao.ATTR_ID_UNITS, 2);
					put(BookingServiceExtraDao.ATTR_PRECIO, new BigDecimal(22.20));
					put("total", 44.40);
				}
			});
			return er;
		}

	}
	
}

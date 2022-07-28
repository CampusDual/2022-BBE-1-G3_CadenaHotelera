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
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ReceiptDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServicesXtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
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
	BookingServiceExtraService bookingServiceExtraServiceMock;
	
	@Mock
	BookingService bookingServiceMock;

	@Spy
	ControlFields cf;

	@InjectMocks
	ReceiptService service;

	@Autowired
	ReceiptDao receiptDao;
	
	@Autowired
	BookingDao bookingDao;

	EntityResult eR;

	@Nested
	@DisplayName("Test for Receipt queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class ReceiptQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testReceiptQueryControlFieldsReset() {
			service.receiptQuery(TestingTools.getMapEmpty(), getColumsName());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testReceiptQueryControlFieldsValidate() {
			service.receiptQuery(TestingTools.getMapEmpty(), getColumsName());
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
		void testReceiptQueryOK() {
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList());

			// válido: HashMap vacio (sin filtros)
			eR = service.receiptQuery(TestingTools.getMapEmpty(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

			// válido: HashMap con filtro que existe (sin filtros)
			eR = service.receiptQuery(getMapId(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testReceiptQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.receiptQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.receiptQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.receiptQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.receiptQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.receiptQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}

	}
	
	
	@Nested
	@DisplayName("Test for Complete Receipt queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class CompleteReceiptQuery {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testCompleteReceiptQueryControlFieldsReset() {
			service.completeReceiptQuery(TestingTools.getMapEmpty(), getColumsName());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map") //No se usa validate list
		void testCompleteReceiptQueryControlFieldsValidate() {
			service.completeReceiptQuery(TestingTools.getMapEmpty(), getColumsName());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testComleteReceiptQueryOK() {
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList());


			// válido: HashMap con filtro que existe (sin filtros)
			eR = service.receiptQuery(getMapId(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testCompleteReceiptQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.completeReceiptQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.completeReceiptQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.completeReceiptQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.completeReceiptQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.completeReceiptQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}

	}

	@Nested
	@DisplayName("Test for Receipt inserts")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class ReceiptInsert {

		@Test
		@DisplayName("ControlFields usar reset()")
		void testReceiptInsertControlFieldsReset() {
			service.receiptInsert(TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map ")
		void testReceiptInsertControlFieldsValidate() {
			service.receiptInsert(TestingTools.getMapEmpty());
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
				when(bookingServiceMock.getBookingStatus(any())).thenReturn(BookingDao.Status.COMPLETED);
				when(bookingServiceMock.bookingDaysUnitaryRoomPriceQuery(anyMap(), anyList()))
						.thenReturn(getPrecioDiasHabitacion());
				when(bookingServiceExtraServiceMock.bookingExtraServicePriceUnitsTotalQuery(anyMap(), anyList()))
						.thenReturn(getPrecioServcioUnidadesTotal());
			} catch (EntityResultRequiredException e) {
				fail("Err");
				e.printStackTrace();
			}
			// válido: HashMap campos mínimos
			eR = service.receiptInsert(getMapRequiredInsert());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testReceiptInsertKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.receiptInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.receiptInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.receiptInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.receiptInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.receiptInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(cf);
				// extra para controlar required:
				eR = service.receiptInsert(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				System.out.println(eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.receiptInsert(getMapRequiredInsertExtendedWidthRestricted());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				System.out.println(eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());
				
				
				reset(cf);
				doThrow(new EntityResultRequiredException(ErrorMessage.RESULT_REQUIRED + " " + ErrorMessage.NO_BOOKING_ID)).when(bookingServiceMock).getBookingStatus(any());	
				eR = service.receiptInsert(getMapRequiredInsert());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.RESULT_REQUIRED + " " + ErrorMessage.NO_BOOKING_ID, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());
			}

		}
	}
	@Nested
	@DisplayName("Test for Receipt deletes")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class ReceiptDelete {
		@Test
		@DisplayName("ControlFields usar reset()")
		void testReceiptDeleteControlFieldsReset() {
			service.receiptDelete(TestingTools.getMapEmpty());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map ")
		void testReceiptDeleteControlFieldsValidate() {
			service.receiptDelete(TestingTools.getMapEmpty());
			try {
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail("excepción no capturada: " + e.getMessage());

			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testReceiptDeleteOK() {
			
			doReturn(TestingTools.getEntityOneRecord()).when(daoHelper).query(any(), anyMap(),anyList());
			doReturn(new EntityResultMapImpl()).when(daoHelper).delete(any(), anyMap());
			// válido: HashMap campo único y exclusivo
			eR = service.receiptDelete(getMapId());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());
 
		}
		
		@Test
		@DisplayName("Valores Subcontulta Error")
		void testReceiptDeleteSubQueryKO() {
			doReturn(new EntityResultWrong()).when(daoHelper).query(any(), anyMap(),anyList());
//			doReturn(new EntityResultMapImpl()).when(daoHelper).delete(any(), anyMap());
			
			// 
			eR = service.receiptDelete(getMapId());
			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
			assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
		}
		
		@Test
		@DisplayName("Valores Subconsultta 0 resultados")
		void testReceiptDeleteSubQueryNoResults() {
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(),anyList());
//			doReturn(new EntityResultMapImpl()).when(daoHelper).delete(any(), anyMap());
			
			// 
			eR = service.receiptDelete(getMapId());
			assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
			assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
		}
		
		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testReceiptDeleteKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recogidas.
				doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
				eR = service.receiptDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.receiptDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.receiptDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.receiptDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.receiptDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				// lanzamos todas las excepciones de SQL para comprobar que están bien
				// recogidas.
				doThrow(DataIntegrityViolationException.class).when(cf).validate(anyMap());
				eR = service.receiptDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				reset(cf); //para quitar doThrow anterior
				// extra para controlar required:
				eR = service.receiptDelete(TestingTools.getMapEmpty());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());

				// extra para controlar restricted:
				eR = service.receiptDelete(getMapRequiredDeletetExtendedWidthRestricted());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());
				assertFalse(eR.getMessage().isEmpty(), eR.getMessage());


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
				put(ReceiptDao.ATTR_BOOKING_ID, 1);
			}
		};
	}
	
	HashMap<String, Object> getMapId() {
		HashMap<String, Object> filters = new HashMap<>() {
			{
				put(ReceiptDao.ATTR_ID, 1);
			}
		};
		return filters;
	};


	Map<String, Object> getMapRequiredInsertExtendedWidthRestricted() {

		return new HashMap<>() {
			{
				put(ReceiptDao.ATTR_ID, 1);
				put(ReceiptDao.ATTR_DIAS, 12);
				put(ReceiptDao.ATTR_TOTAL, new BigDecimal(34));
			}
		};
	}
	
	Map<String, Object> getMapRequiredDeletetExtendedWidthRestricted() {
		return getMapRequiredInsertExtendedWidthRestricted();
	}
	
	EntityResult getPrecioDiasHabitacion() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_ID,1);
				put(RoomTypeDao.ATTR_PRICE,new BigDecimal(12));
				put(ReceiptDao.ATTR_DIAS,2);
			}
		});
		return er;
	}
	
	EntityResult getPrecioServcioUnidadesTotal() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<String, Object>() {
			{
				put(BookingServiceExtraDao.ATTR_ID_BKG,1);
				put(BookingServiceExtraDao.ATTR_PRECIO,new BigDecimal(10));
				put(BookingServiceExtraDao.ATTR_ID_UNITS,1);
				put("total",10);
			}
		});
		return er;
	}
//	
	EntityResult getServciosExtra() {
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
				add(ReceiptDao.ATTR_ID);
				add(ReceiptDao.ATTR_BOOKING_ID);
				add(ReceiptDao.ATTR_DATE);
				add(ReceiptDao.ATTR_DIAS);
				add(ReceiptDao.ATTR_TOTAL_ROOM);
				add(ReceiptDao.ATTR_TOTAL_SERVICES);
				add(ReceiptDao.ATTR_TOTAL);
			}
		};
		return columns;
	}
//	// fin datos entrada
}






//class ReceiptServiceTest {
//
//	@Mock
//	DefaultOntimizeDaoHelper daoHelper;
//
//	@Mock
//	BookingService bookingServiceMock;
//
//	@Mock
//	BookingServiceExtraService bookingServiceExtraServiceMock;
//
//	@InjectMocks
//	ReceiptService service;
//
//	@Autowired
//	ReceiptDao receiptDao;
//
//	@Autowired
//	BookingDao bookingDao;
//
//	@Autowired
//	ValidateFields vf;
//
//	@Nested
//	@DisplayName("Test for Receipt queries")
//	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//	public class ReceiptQuery {
//
//
//		@Test
//		@DisplayName("Obtain all data columns from receipts table when rcp_id is -> 2")
//		void when_queryAllColumns_return_specificData() {
//			Map<String, Object> keyMap = new HashMap() {
//				{
//					put(ReceiptDao.ATTR_ID, 2);
//				}
//			};
//			List<String> attrList = Arrays.asList(ReceiptDao.ATTR_ID, ReceiptDao.ATTR_BOOKING_ID,ReceiptDao.ATTR_DATE,ReceiptDao.ATTR_DIAS,ReceiptDao.ATTR_TOTAL_ROOM,ReceiptDao.ATTR_TOTAL_SERVICES,ReceiptDao.ATTR_TOTAL);
//			when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificReceiptData(keyMap, attrList));
//			EntityResult entityResult = service.receiptQuery(keyMap,attrList);
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
//			assertEquals(1, entityResult.calculateRecordNumber());
//			assertEquals(2, entityResult.getRecordValues(0).get(ReceiptDao.ATTR_ID));
//		}
//
//		@Test
//		@DisplayName("Obtain all data columns from receipts table when rcp_id not exist")
//		void when_queryAllColumnsNotExisting_return_empty() {
//			HashMap<String, Object> keyMap = new HashMap<>() {
//				{
//					put(ReceiptDao.ATTR_ID, 5);
//				}
//			};
//			List<String> attrList = Arrays.asList(ReceiptDao.ATTR_ID, ReceiptDao.ATTR_BOOKING_ID,ReceiptDao.ATTR_DATE,ReceiptDao.ATTR_DIAS,ReceiptDao.ATTR_TOTAL_ROOM,ReceiptDao.ATTR_TOTAL_SERVICES,ReceiptDao.ATTR_TOTAL);
//			when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificReceiptData(keyMap, attrList));
//			EntityResult entityResult = service.receiptQuery(keyMap, attrList);
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
//			assertEquals(0, entityResult.calculateRecordNumber());
//		}
//
//		@ParameterizedTest(name = "Obtain data with rcp_id -> {1}")
//		@MethodSource("randomIDGenerator")
//		@DisplayName("Obtain all data columns from receipt table when rcp_id is random")
//		void when_queryAllColumnsWithRandomValue_return_specificData(int random) {
//			HashMap<String, Object> keyMap = new HashMap<>() {
//				{
//					put(ReceiptDao.ATTR_ID, random);
//				}
//			};
//			List<String> attrList = Arrays.asList(ReceiptDao.ATTR_ID, ReceiptDao.ATTR_BOOKING_ID,ReceiptDao.ATTR_DATE,ReceiptDao.ATTR_DIAS,ReceiptDao.ATTR_TOTAL_ROOM,ReceiptDao.ATTR_TOTAL_SERVICES,ReceiptDao.ATTR_TOTAL);
//			when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificReceiptData(keyMap, attrList));
//			EntityResult entityResult = service.receiptQuery(keyMap, attrList);
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
//			assertEquals(1, entityResult.calculateRecordNumber());
//			assertEquals(random, entityResult.getRecordValues(0).get(ReceiptDao.ATTR_ID));
//		}
//		
//		@Test
//		@DisplayName("Missing Fields")
//		void when_Missings_Fields() {
//			List<String> attrList = Arrays.asList(ReceiptDao.ATTR_ID, ReceiptDao.ATTR_BOOKING_ID,ReceiptDao.ATTR_DATE,ReceiptDao.ATTR_DIAS,ReceiptDao.ATTR_TOTAL_ROOM,ReceiptDao.ATTR_TOTAL_SERVICES,ReceiptDao.ATTR_TOTAL);
//			EntityResult entityResult = service.receiptQuery(new HashMap<>(), attrList);
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//			assertEquals(ErrorMessage.REQUIRED_FIELD, entityResult.getMessage());
//			assertEquals(0, entityResult.calculateRecordNumber());
//		}
//		
//		@Test
//		@DisplayName("Missing Columns")
//		void when_missing_columns() {
//			HashMap<String, Object> keyMap = new HashMap<>() {
//				{
//					put(ReceiptDao.ATTR_ID, 1);
//				}
//			};
//			EntityResult entityResult = service.receiptQuery(keyMap, new ArrayList<>());
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//			assertEquals(ErrorMessage.REQUIRED_COLUMNS, entityResult.getMessage());
//			assertEquals(0, entityResult.calculateRecordNumber());
//		}
//		
//
//		@Test
//		@DisplayName("Invalid Fields Value")
//		void when_invalid_fields_value() {
//			HashMap<String, Object> keyMap = new HashMap<>() {
//				{
//					put(ReceiptDao.ATTR_ID, "1");
//				}
//			};
//			List<String> attrList = Arrays.asList(ReceiptDao.ATTR_ID, ReceiptDao.ATTR_BOOKING_ID,ReceiptDao.ATTR_DATE,ReceiptDao.ATTR_DIAS,ReceiptDao.ATTR_TOTAL_ROOM,ReceiptDao.ATTR_TOTAL_SERVICES,ReceiptDao.ATTR_TOTAL);
//			EntityResult entityResult = service.receiptQuery(keyMap, attrList);
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//			assertEquals(ErrorMessage.WRONG_TYPE + " - "+ReceiptDao.ATTR_ID, entityResult.getMessage());
//			assertEquals(0, entityResult.calculateRecordNumber());
//		}
//
//		public EntityResult getAllReceiptsData() {
//			List<String> columnList = Arrays.asList(ReceiptDao.ATTR_ID, ReceiptDao.ATTR_BOOKING_ID,
//					ReceiptDao.ATTR_DATE, ReceiptDao.ATTR_DIAS, ReceiptDao.ATTR_TOTAL_ROOM,
//					ReceiptDao.ATTR_TOTAL_SERVICES, ReceiptDao.ATTR_TOTAL);
//			EntityResult er = new EntityResultMapImpl(columnList);
//			er.addRecord(new HashMap<String, Object>() {
//				{
//					put(ReceiptDao.ATTR_ID, 1);
//					put(ReceiptDao.ATTR_BOOKING_ID, 2);
//					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
//					put(ReceiptDao.ATTR_DIAS, 3);
//					put(ReceiptDao.ATTR_TOTAL_ROOM, new BigDecimal(200.15));
//					put(ReceiptDao.ATTR_TOTAL_SERVICES, new BigDecimal(300.15));
//					put(ReceiptDao.ATTR_TOTAL, new BigDecimal(500.30));
//				}
//			});
//			er.addRecord(new HashMap<String, Object>() {
//				{
//					put(ReceiptDao.ATTR_ID, 2);
//					put(ReceiptDao.ATTR_BOOKING_ID, 4);
//					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
//					put(ReceiptDao.ATTR_DIAS, 3);
//					put(ReceiptDao.ATTR_TOTAL_ROOM, new BigDecimal(200.15));
//					put(ReceiptDao.ATTR_TOTAL_SERVICES, new BigDecimal(300.15));
//					put(ReceiptDao.ATTR_TOTAL, new BigDecimal(500.30));
//				}
//			});
//			er.addRecord(new HashMap<String, Object>() {
//				{
//					put(ReceiptDao.ATTR_ID, 3);
//					put(ReceiptDao.ATTR_BOOKING_ID, 5);
//					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
//					put(ReceiptDao.ATTR_DIAS, 3);
//					put(ReceiptDao.ATTR_TOTAL_ROOM, new BigDecimal(200.15));
//					put(ReceiptDao.ATTR_TOTAL_SERVICES, new BigDecimal(300.15));
//					put(ReceiptDao.ATTR_TOTAL, new BigDecimal(500.30));
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
//		public EntityResult getSpecificReceiptData(Map<String, Object> keyValues, List<String> attributes) {
//			EntityResult allData = this.getAllReceiptsData();
//			int recordIndex = allData.getRecordIndex(keyValues);
//			HashMap<String, Object> recordValues = (HashMap) allData.getRecordValues(recordIndex);
//			List<String> columnList = Arrays.asList(ReceiptDao.ATTR_ID, ReceiptDao.ATTR_BOOKING_ID,
//					ReceiptDao.ATTR_DATE, ReceiptDao.ATTR_DIAS, ReceiptDao.ATTR_TOTAL_ROOM,
//					ReceiptDao.ATTR_TOTAL_SERVICES, ReceiptDao.ATTR_TOTAL);
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
//
//	@Nested
//	@DisplayName("Test for Receipt inserts")
//	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//	public class InsertQuery {
//
//		@Test
//		@DisplayName("Insert Receipt")
//		void when_hotel_insert_is_succsessfull() {
//			Map<String, Object> attrMap = new HashMap<>() {
//				{
//					put(ReceiptDao.ATTR_ID, 1);
//					put(ReceiptDao.ATTR_BOOKING_ID, 2);
//					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
//					put(ReceiptDao.ATTR_DIAS, 3);
//					put(ReceiptDao.ATTR_TOTAL_ROOM, new BigDecimal(200.15));
//					put(ReceiptDao.ATTR_TOTAL_SERVICES, new BigDecimal(300.15));
//					put(ReceiptDao.ATTR_TOTAL, new BigDecimal(500.30));
//				}
//			};
//			EntityResult resultado = new EntityResultMapImpl();
//			resultado.addRecord(attrMap);
//			resultado.setCode(EntityResult.OPERATION_SUCCESSFUL);
//			resultado.setMessage("Receipt registrada");
//			when(daoHelper.insert(any(), anyMap())).thenReturn(resultado);
//			try {
//				when(bookingServiceMock.getBookingStatus(any())).thenReturn(BookingDao.Status.COMPLETED);
//				when(bookingServiceMock.bookingDaysUnitaryRoomPriceQuery(anyMap(), anyList()))
//						.thenReturn(getBookingDaysRoomPrice());
//				when(bookingServiceExtraServiceMock.bookingExtraServicePriceUnitsTotalQuery(anyMap(), anyList()))
//						.thenReturn(getBookingExtraServiceUnitsPriceTotal());
//			} catch (EntityResultRequiredException e) {
//				fail("Err");
//				e.printStackTrace();
//			}
//			EntityResult entityResult = service.receiptInsert(attrMap);
//			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
//			assertEquals("Receipt registrada", entityResult.getMessage());
//		}
//		
//
//		@Test
//		@DisplayName("Duplicated Key")
//		void when_already_exist() {
//			Map<String, Object> attrMap = new HashMap<>() {
//				{
//					put(ReceiptDao.ATTR_ID, 1); // Si ya está guardado en la base datos
//					put(ReceiptDao.ATTR_BOOKING_ID, 2);
//					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
//					put(ReceiptDao.ATTR_DIAS, 3);
//					put(ReceiptDao.ATTR_TOTAL_ROOM, new BigDecimal(200.15));
//					put(ReceiptDao.ATTR_TOTAL_SERVICES, new BigDecimal(300.15));
//					put(ReceiptDao.ATTR_TOTAL, new BigDecimal(500.30));
//				}
//			};
//
//			try {
//				when(bookingServiceMock.getBookingStatus(any())).thenReturn(BookingDao.Status.COMPLETED);
//				when(bookingServiceMock.bookingDaysUnitaryRoomPriceQuery(anyMap(), anyList()))
//						.thenReturn(getBookingDaysRoomPrice());
//				when(bookingServiceExtraServiceMock.bookingExtraServicePriceUnitsTotalQuery(anyMap(), anyList()))
//						.thenReturn(getBookingExtraServiceUnitsPriceTotal());
//			} catch (EntityResultRequiredException e) {
//				fail("Err");
//				e.printStackTrace();
//			}
//			when(daoHelper.insert(any(), anyMap())).thenThrow(DuplicateKeyException.class);
//			EntityResult entityResult = service.receiptInsert(attrMap);
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//			assertEquals(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD, entityResult.getMessage());
//		}
//
//		@Test
//		@DisplayName("Missing Fields - Null")
//		void when_missing_fields_null() {
//			Map<String, Object> attrMap = new HashMap<>() {
//				{
//					put(ReceiptDao.ATTR_ID, 1);
//					put(ReceiptDao.ATTR_BOOKING_ID, null);
//					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
//					put(ReceiptDao.ATTR_DIAS, 3);
//					put(ReceiptDao.ATTR_TOTAL_ROOM, new BigDecimal(200.15));
//					put(ReceiptDao.ATTR_TOTAL_SERVICES, new BigDecimal(300.15));
//					put(ReceiptDao.ATTR_TOTAL, new BigDecimal(500.30));
//				}
//			};
//
//			EntityResult entityResult = service.receiptInsert(attrMap);
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//			assertEquals(ErrorMessage.CREATION_ERROR + "-El campo " + ReceiptDao.ATTR_BOOKING_ID + " es nulo",
//					entityResult.getMessage());
//
//		}
//
//		@Test
//		@DisplayName("Missing Fields - No field")
//		void when_missing_fields() {
//			Map<String, Object> attrMap = new HashMap<>() {
//				{
//					put(ReceiptDao.ATTR_ID, 1);
////					put(ReceiptDao.ATTR_BOOKING_ID, 3); //No se inserta un campo obligatorio
//					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
//					put(ReceiptDao.ATTR_DIAS, 3);
//					put(ReceiptDao.ATTR_TOTAL_ROOM, new BigDecimal(200.15));
//					put(ReceiptDao.ATTR_TOTAL_SERVICES, new BigDecimal(300.15));
//					put(ReceiptDao.ATTR_TOTAL, new BigDecimal(500.30));
//				}
//			};
//
//			EntityResult entityResult = service.receiptInsert(attrMap);
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//			assertEquals(ErrorMessage.CREATION_ERROR + "-Falta el campo " + ReceiptDao.ATTR_BOOKING_ID,
//					entityResult.getMessage());
//
//		}
//
//		@Test
//		@DisplayName("Missing Foreing Key")
//		void when_missing_foreing_key() {
//			Map<String, Object> attrMap = new HashMap<>() {
//				{
//					put(ReceiptDao.ATTR_ID, 1);
//					put(ReceiptDao.ATTR_BOOKING_ID, 100);// Si no existe esta reserva en la tabla bookings
//					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
//					put(ReceiptDao.ATTR_DIAS, 3);
//					put(ReceiptDao.ATTR_TOTAL_ROOM, new BigDecimal(200.15));
//					put(ReceiptDao.ATTR_TOTAL_SERVICES, new BigDecimal(300.15));
//					put(ReceiptDao.ATTR_TOTAL, new BigDecimal(500.30));
//				}
//			};
//
//			try {
//				when(bookingServiceMock.getBookingStatus(any())).thenReturn(BookingDao.Status.COMPLETED);
//				when(bookingServiceMock.bookingDaysUnitaryRoomPriceQuery(anyMap(), anyList()))
//						.thenReturn(getBookingDaysRoomPrice());
//				when(bookingServiceExtraServiceMock.bookingExtraServicePriceUnitsTotalQuery(anyMap(), anyList()))
//						.thenReturn(getBookingExtraServiceUnitsPriceTotal());
//			} catch (EntityResultRequiredException e) {
//				fail("Err");
//				e.printStackTrace();
//			}
//			when(daoHelper.insert(any(), anyMap())).thenThrow(DataIntegrityViolationException.class);
//			EntityResult entityResult = service.receiptInsert(attrMap);
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//			assertEquals(ErrorMessage.CREATION_ERROR_MISSING_FK, entityResult.getMessage());
//		}
//
//		EntityResult getBookingDaysRoomPrice() {
//			List<String> columnList = Arrays.asList(BookingDao.ATTR_ID, RoomTypeDao.ATTR_PRICE, ReceiptDao.ATTR_DIAS);
//			EntityResult er = new EntityResultMapImpl(columnList);
//			er.addRecord(new HashMap<String, Object>() {
//				{
//					put(BookingDao.ATTR_ID, 1);
//					put(RoomTypeDao.ATTR_PRICE, new BigDecimal(20.21));
//					put(ReceiptDao.ATTR_DIAS, 3);
//				}
//			});
//			return er;
//		}
//
//		@Test
//		@DisplayName("Invalid Fields")
//		void when_invalid_fields() {
//			Map<String, Object> attrMap = new HashMap<>() {
//				{
//					put(ReceiptDao.ATTR_ID, 1);
//					put(ReceiptDao.ATTR_BOOKING_ID, "Esto debería ser un int");
//					put(ReceiptDao.ATTR_DATE, LocalDateTime.now());
//					put(ReceiptDao.ATTR_DIAS, 3);
//					put(ReceiptDao.ATTR_TOTAL_ROOM, new BigDecimal(200.15));
//					put(ReceiptDao.ATTR_TOTAL_SERVICES, new BigDecimal(300.15));
//					put(ReceiptDao.ATTR_TOTAL, new BigDecimal(500.30));
//				}
//			};
//
//			EntityResult entityResult = service.receiptInsert(attrMap);
//			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
//			assertEquals(
//					ErrorMessage.CREATION_ERROR + "-" + ErrorMessage.WRONG_TYPE + " - " + ReceiptDao.ATTR_BOOKING_ID,
//					entityResult.getMessage());
//
//		}
//
//		EntityResult getBookingExtraServiceUnitsPriceTotal() {
//			List<String> columnList = Arrays.asList(BookingServiceExtraDao.ATTR_ID_BKG,
//					BookingServiceExtraDao.ATTR_ID_UNITS, BookingServiceExtraDao.ATTR_PRECIO, "total");
//			EntityResult er = new EntityResultMapImpl(columnList);
//			er.addRecord(new HashMap<String, Object>() {
//				{
//					put(BookingServiceExtraDao.ATTR_ID_BKG, 1);
//					put(BookingServiceExtraDao.ATTR_ID_UNITS, 2);
//					put(BookingServiceExtraDao.ATTR_PRECIO, new BigDecimal(22.20));
//					put("total", 44.40);
//				}
//			});
//			return er;
//		}
//
//	}
//	
//}

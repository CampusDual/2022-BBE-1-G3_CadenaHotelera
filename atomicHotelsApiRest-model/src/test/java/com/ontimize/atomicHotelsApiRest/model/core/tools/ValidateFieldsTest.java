package com.ontimize.atomicHotelsApiRest.model.core.tools;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.model.core.service.TestingTools;

import static org.junit.jupiter.api.Assertions.*;

class ValidateFieldsTest {

	Map<String, Object> getKeyMap() {
		return new HashMap<>() {
			{
				put("campo1", "uno");
				put("campo2", "dos");
				put("campo3", "tres");
			}
		};
	}

	List<String> getAttrList() {
		return new ArrayList<>() {
			{
				add("campo1");
				add("campo2");
				add("campo3");
			}
		};
	}

	Map<String, Object> getKeyMapWithNullValues() {
		return new HashMap<>() {
			{
				put("campo1", null);
				put("campo2", null);
				put("campo3", null);
			}
		};
	}

	Map<String, Object> getKeyMapWithEmptyValues() {
		return new HashMap<>() {
			{
				put("campo1", "");
				put("campo2", "");
				put("campo3", "");
			}
		};
	}

	Map<String, Object> getKeyMapWithEmptyNullEmptyValues() {
		return new HashMap<>() {
			{
				put("campo1", new String());
				put("campo2", null);
				put("campo3", "");
			}
		};
	}
//
//	@Nested
//	@DisplayName("Test for required()")
//	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//	public class Required {
//
//		@ParameterizedTest(name = "Keymap  y Campos - Válidos")
//		@DisplayName("Keymap  y Campos - Válidos")
//		@ValueSource(strings = { "campo1", "campo3", "campo2" })
//		void testRequiredOKFields(String string) {
//			assertDoesNotThrow(() -> ValidateFields.required(getKeyMap(), string));
//		}
//
//		@ParameterizedTest(name = "Campos - NO válidos")
//		@DisplayName("Campos - NO válidos")
//		@ValueSource(strings = { "campo4", "", " ", "" })
//		void testRequiredKOFields(String string) {
//			assertThrows(MissingFieldsException.class, () -> ValidateFields.required(getKeyMap(), string));
//		}
//
//		@ParameterizedTest(name = "Campos - Null y Empty")
//		@DisplayName("Campos - Null y Empty")
//		@NullAndEmptySource
//		void testRequiredKOFieldsNullEmpty(String string) {
//			assertThrows(MissingFieldsException.class, () -> ValidateFields.required(getKeyMap(), string));
//		}
//
//		@ParameterizedTest(name = "Keymap NO válido")
//		@DisplayName("Keymap NO válido")
//		@ValueSource(strings = { "campo1", "campo3", "campo2" })
//		void testRequiredKOKeyMap(String string) {
//			assertThrows(MissingFieldsException.class, () -> ValidateFields.required(anyMap(), string));
//		}
//
//	}
//
//	@Nested
//	@DisplayName("Test for restricted()")
//	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//	public class Restricted {
//
//		@Test
//		@DisplayName("Keymap  y Campos - Válidos")
//		void testRestrictedMapOfStringObjectStringArrayOK() {
//			Map<String, Object> keyMap = getKeyMap();
//			assertFalse(keyMap.containsKey("campo999"));
//			ValidateFields.restricted(keyMap, "campo1", "campo2", "campo3", "campo999");
//			assertFalse(keyMap.containsKey("campo1"));
//			assertFalse(keyMap.containsKey("campo2"));
//			assertFalse(keyMap.containsKey("campo3"));
//			assertFalse(keyMap.containsKey("campo999"));
//			assertTrue(keyMap.isEmpty(), keyMap.toString());
//		}
//
//		@Test
//		@DisplayName("attrList  y Campos - Válidos")
//		void testRestrictedListOfStringStringArray() {
//			List<String> attrList = getAttrList();
//			assertFalse(attrList.contains("campo999"));
//			ValidateFields.restricted(attrList, "campo1", "campo2", "campo3", "campo999");
//			assertFalse(attrList.contains("campo1"));
//			assertFalse(attrList.contains("campo2"));
//			assertFalse(attrList.contains("campo3"));
//			assertFalse(attrList.contains("campo999"));
//			assertTrue(attrList.isEmpty(), attrList.toString());
//		}
//	}

	@Nested
	@DisplayName("Test for Dates")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class Dates {

		@ParameterizedTest()
		@DisplayName("Fechas válida")
		@ValueSource(strings = { "2021-01-01", "1998-03-11", "2021-01-01" })
		void testStringToDateOK(String fecha) {
			assertDoesNotThrow(() -> ValidateFields.stringToDate(fecha));
		}

		@ParameterizedTest()
		@DisplayName("Fechas NO válida")
		@ValueSource(strings = { "2021-13-32", "cosas", "01-01-2022", "01/01/2022" })
		void testStringToDateKO(String fecha) {
			assertThrows(Exception.class, () -> ValidateFields.stringToDate(fecha));
			assertThrows(InvalidFieldsValuesException.class, () -> ValidateFields.stringToDate(fecha));
		}

		@ParameterizedTest()
		@DisplayName("Fechas null o vacía")
		@NullAndEmptySource
		void testStringToDateNullEmpty(String fecha) {
			assertThrows(Exception.class, () -> ValidateFields.stringToDate(fecha));
			assertThrows(InvalidFieldsValuesException.class, () -> ValidateFields.stringToDate(fecha));
		}

		@Test
		@DisplayName("Fecha inicio y Fin Válidas")
		void testDataRangeDateDateOK() {
			try {
				assertEquals(0, ValidateFields.dataRange(TestingTools.getNowString(), TestingTools.getTomorrowString()));
			} catch (InvalidFieldsValuesException e) {
				fail();
			}
		}

		@Test
		@DisplayName("Fecha inicio Pasada y Fin Válidas")
		void testDataRangeDateDatePastOK() {
//			Calendar today = Calendar.getInstance();
//			today.setTime(new Date());
//			today.add(Calendar.MINUTE, 1);
//			Calendar c = Calendar.getInstance();
//			c.setTime(new Date());
//			c.add(Calendar.DATE, -2);
			try {
				assertEquals(1, ValidateFields.dataRange(TestingTools.getYesterday(), TestingTools.getNow()));
			} catch (InvalidFieldsValuesException e) {
				fail();
			}
		}

		@Test
		@DisplayName("Fecha inicio superior a Fin (NO Válidas)")
		void testDataRangeDateDatePastKO() {
//			Calendar today = Calendar.getInstance();
//			today.setTime(new Date());
//			today.add(Calendar.MINUTE, 1);
//			Calendar c = Calendar.getInstance();
//			c.setTime(new Date());
//			c.add(Calendar.DATE, -2000);
			assertThrows(InvalidFieldsValuesException.class,
					() -> ValidateFields.dataRange(TestingTools.getNow(), TestingTools.getYesterday()));
		}

//		@Test
//		void testDataRangeStringString() {
//			fail("Not yet implemented");
//		}
//
		@Test
		void testDataRangeObjectObjectOk() {
			Calendar today = Calendar.getInstance();
			today.setTime(new Date());
			today.add(Calendar.MINUTE, 1);
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, 1);

			assertDoesNotThrow(() -> ValidateFields.dataRange((Object) "2022-01-01", (Object) "2022-01-03"));
			assertDoesNotThrow(() -> ValidateFields.dataRange((Object) "2022-01-01", "2022-01-03"));
			assertDoesNotThrow(() -> ValidateFields.dataRange((Object) TestingTools.getNowString(), (Object) TestingTools.getTomorrowString()));
			assertDoesNotThrow(() -> ValidateFields.dataRange((Object) TestingTools.getNowString(), TestingTools.getTomorrowString()));
		}

		@Test
		void testDataRangeObjectObjectKO() {
			assertThrows(InvalidFieldsValuesException.class,
					() -> ValidateFields.dataRange((Object) "2022-10-01", (Object) new Date()));
		}

	}

//	@Nested
//	@DisplayName("Test for empty fields")
//	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//	public class Emptys {
//
//		@ParameterizedTest()
//		@DisplayName("Campos válidos")
//		@ValueSource(strings = { "campo1", "campo2", "campo3" })
//		void testEmptyFieldOK(String valor) {
//			assertDoesNotThrow(() -> ValidateFields.emptyField(getKeyMap(), valor));
//		}
//
//		@ParameterizedTest()
//		@DisplayName("Campos NO válidos")
//		@ValueSource(strings = { "campo1", "campo2", "campo3", "campo999" })
//		void testEmptyFieldKo(String valor) {
//			assertThrows(MissingFieldsException.class,
//					() -> ValidateFields.emptyField(getKeyMapWithEmptyNullEmptyValues(), valor));
//		}
//
////		@Test
////		@DisplayName("Varios Campos entrada válidos")
////		void testEmptyFieldsOK() {
////			assertDoesNotThrow(() -> ValidateFields.emptyFields(getKeyMap(), "campo1", "campo2", "campo3"));
////		}
////
////		@Test
////		@DisplayName("Varios Campos entrada NO válidos")
////		void testEmptyFieldsKO() {
////			assertThrows(MissingFieldsException.class, () -> ValidateFields
////					.emptyFields(getKeyMapWithEmptyNullEmptyValues(), "campo1", "campo2", "campo3", "campo999"));
////		}
//
//	}

	@Nested
	@DisplayName("Test for Prices")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class Prices {

		@ParameterizedTest
		@DisplayName("Campos Double - válidos")
		@ValueSource(doubles = { 1, 4, 2.33, 11234.48, 0.95, 01.14, 0 })
		void testFormatpriceDoublesOK(Double numeros) {
			assertDoesNotThrow(() -> ValidateFields.formatprice(numeros));
		}

		@ParameterizedTest
		@DisplayName("Campos String - válidos")
		@ValueSource(strings = { "1", "4", "2.33", "11234.48", "0.95", "01.14" })
		void testFormatpriceStringsOK(String numeros) {
			assertDoesNotThrow(() -> ValidateFields.formatprice(numeros));
		}

		@ParameterizedTest
		@DisplayName("Campos Double - NO válidos")
		@ValueSource(doubles = { 4.12345, 123123343434D, 0001.999, (Double.MAX_VALUE + 1) })
		void testFormatpriceDoublesKO(Double numeros) {
			assertThrows(NumberFormatException.class, () -> ValidateFields.formatprice(numeros));
		}

		@ParameterizedTest
		@DisplayName("Campos String - NO válidos")
		@ValueSource(strings = { "4.12345", "000000.000000", "123123343434", "1.99000", "albaricoque", "?", "%", ".",
				" ", "-" })
		void testFormatpriceStringsKO(String numeros) {
			assertThrows(NumberFormatException.class, () -> ValidateFields.formatprice(numeros));
			assertThrows(Exception.class, () -> ValidateFields.formatprice(numeros));
		}

		@Test
		@DisplayName("Campos Otro Tipo - NO válidos")
		void testFormatpriceTypesKO() {
			assertThrows(NumberFormatException.class, () -> ValidateFields.formatprice(null));
			assertThrows(NumberFormatException.class, () -> ValidateFields.formatprice(new Date()));
			assertThrows(NumberFormatException.class, () -> ValidateFields.formatprice(new EntityResultWrong()));
		}
	}

	@Nested
	@DisplayName("Test for credit card")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class CreditCard {
		@ParameterizedTest
		@DisplayName("Numeros - Válidos")
		@ValueSource(longs = { 1000_1000_1000_1000L, 1234123412341234L, 1300_0000_0000_0L, 1400_0000_0000_00L,
				1500_0000_0000_000L, 1600_0000_0000_0000L })
		void testFormatpriceDoublesOK(Long numeros) {
			assertDoesNotThrow(() -> ValidateFields.invalidCreditCard(numeros));
		}

		@ParameterizedTest
		@DisplayName("Numeros - NO Válidos")
		@ValueSource(longs = { 10, 1200_0000_0000L, 1700_0000_0000_0000_0L, 0000_0000_0000_0001L, -1400_0000_0000_00L })
		void testFormatpriceDoublesKO(Long numeros) {
			assertThrows(InvalidFieldsValuesException.class, () -> ValidateFields.invalidCreditCard(numeros));
		}

		@Test
		@DisplayName("Fechas Expiración - Válidas")
		void testValidDateExpiryOK() {
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, 2000);
			List<String> fechas = new ArrayList<>() {
				{
					add(ValidateFields.dateFormat.format(c.getTime()));
					add("2095-11-11");
				}
			};
			for (String fecha : fechas) {
				assertDoesNotThrow(() -> ValidateFields.validDateExpiry(fecha), fecha);
			}
		}

		@Test
		@DisplayName("Fechas Expiración - NO Válidas")
		void testValidDateExpiryKO() {
			Calendar today = Calendar.getInstance();
			today.setTime(new Date());
			today.add(Calendar.MILLISECOND, 10);
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, -1);
			List<String> fechas = new ArrayList<>() {
				{
					add(ValidateFields.dateFormat.format(today.getTime()));
					add(ValidateFields.dateFormat.format(c.getTime()));
					add("albaricoque");
				}
			};
			for (String fecha : fechas) {
				assertThrows(InvalidFieldsValuesException.class, () -> ValidateFields.validDateExpiry(fecha), fecha);
			}
		}
	}

//
//	@Test
//	void testNegativeNotAllowed() {
//		fail("Not yet implemented");
//	}
	@Nested
	@DisplayName("Test for emails")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class Emails {

		@ParameterizedTest
		@DisplayName("Emails - Válidos")
		@ValueSource(strings = { "asdasd@asdasdas.es", "enunlugardelamancha@hotmail.com", "a@a.pt", "a.a@a.eu",
				"1233.232@2323.223sad.233.com", "alex8@gmail.com" })
		void testFormatpriceDoublesOK(String email) {
			assertDoesNotThrow(() -> ValidateFields.checkMail(email));
		}

		@ParameterizedTest
		@DisplayName("Emails - NO Válidos")
		@ValueSource(strings = { "asdasd@asdasdas.e", "com", "15565", "@todoloquepuedas.com", "hansolo@.es",
				"coma@asdasd@sd.es" })
		void testFormatpriceDoublesKO(String email) {
			assertThrows(InvalidFieldsValuesException.class, () -> ValidateFields.checkMail(email));
		}

	}
}

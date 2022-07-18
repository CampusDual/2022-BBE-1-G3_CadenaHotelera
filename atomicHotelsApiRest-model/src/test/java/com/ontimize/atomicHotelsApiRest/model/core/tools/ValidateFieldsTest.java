package com.ontimize.atomicHotelsApiRest.model.core.tools;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;

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

	@Nested
	@DisplayName("Test for required()")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class Required {

		@ParameterizedTest(name = "Keymap  y Campos - Válidos")
		@DisplayName("Keymap  y Campos - Válidos")
		@ValueSource(strings = { "campo1", "campo3", "campo2" })
		void testRequiredOKFields(String string) {
			assertDoesNotThrow(() -> ValidateFields.required(getKeyMap(), string));
		}

		@ParameterizedTest(name = "Campos - NO válidos")
		@DisplayName("Campos - NO válidos")
		@ValueSource(strings = { "campo4", "", " ", "" })
		void testRequiredKOFields(String string) {
			assertThrows(MissingFieldsException.class, () -> ValidateFields.required(getKeyMap(), string));
		}

		@ParameterizedTest(name = "Campos - Null y Empty")
		@DisplayName("Campos - Null y Empty")
		@NullAndEmptySource
		void testRequiredKOFieldsNullEmpty(String string) {
			assertThrows(MissingFieldsException.class, () -> ValidateFields.required(getKeyMap(), string));
		}

		@ParameterizedTest(name = "Keymap NO válido")
		@DisplayName("Keymap NO válido")
		@ValueSource(strings = { "campo1", "campo3", "campo2" })
		void testRequiredKOKeyMap(String string) {
			assertThrows(MissingFieldsException.class, () -> ValidateFields.required(anyMap(), string));
		}

	}

	@Nested
	@DisplayName("Test for restricted()")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class Restricted {

		@Test
		@DisplayName("Keymap  y Campos - Válidos")
		void testRestrictedMapOfStringObjectStringArrayOK() {
			Map<String, Object> keyMap = getKeyMap();
			assertFalse(keyMap.containsKey("campo999"));
			ValidateFields.restricted(keyMap, "campo1", "campo2", "campo3", "campo999");
			assertFalse(keyMap.containsKey("campo1"));
			assertFalse(keyMap.containsKey("campo2"));
			assertFalse(keyMap.containsKey("campo3"));
			assertFalse(keyMap.containsKey("campo999"));
			assertTrue(keyMap.isEmpty(), keyMap.toString());
		}

		@Test
		@DisplayName("attrList  y Campos - Válidos")
		void testRestrictedListOfStringStringArray() {
			List<String> attrList = getAttrList();
			assertFalse(attrList.contains("campo999"));
			ValidateFields.restricted(attrList, "campo1", "campo2", "campo3", "campo999");
			assertFalse(attrList.contains("campo1"));
			assertFalse(attrList.contains("campo2"));
			assertFalse(attrList.contains("campo3"));
			assertFalse(attrList.contains("campo999"));
			assertTrue(attrList.isEmpty(), attrList.toString());
		}
	}

	@Nested
	@DisplayName("Test for Dates")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class Dates {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") {
			{
				setLenient(false);
			}
		};

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
		void testDataRangeDateDateOK(){
			Date today = new Date();;
			Calendar c = Calendar.getInstance();
			c.setTime(today);
			c.add(Calendar.DATE, 1);
			try {
				assertEquals(0, ValidateFields.dataRange(today, c.getTime()));
			} catch (InvalidFieldsValuesException e) {
				fail();
			}
		}
		
		@Test
		@DisplayName("Fecha inicio Pasada y Fin Válidas")
		void testDataRangeDateDatePastOK() {
			Date today = new Date();;
			Calendar c = Calendar.getInstance();
			c.setTime(today);
			c.add(Calendar.DATE, -1);	
			try {
				assertEquals(1, ValidateFields.dataRange(c.getTime(),today));
			} catch (InvalidFieldsValuesException e) {
				fail();
			}
		}

		@Test
		@DisplayName("Fecha inicio superior a Fin (NO Válidas)")
		void testDataRangeDateDatePastKO(){
			Date today = new Date();;
			Calendar c = Calendar.getInstance();
			c.setTime(today);
			c.add(Calendar.DATE, -1);	
			assertThrows(InvalidFieldsValuesException.class, () -> ValidateFields.dataRange(today,c.getTime()));
		}
		
//		@Test
//		void testDataRangeStringString() {
//			fail("Not yet implemented");
//		}
//
//		@Test
//		void testDataRangeObjectObject() {
//			fail("Not yet implemented");
//		}

	}

//	@Test
//	void testEmptyFields() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testEmptyField() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testFormatprice() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testNegativeNotAllowed() {
//		fail("Not yet implemented");
//	}

}

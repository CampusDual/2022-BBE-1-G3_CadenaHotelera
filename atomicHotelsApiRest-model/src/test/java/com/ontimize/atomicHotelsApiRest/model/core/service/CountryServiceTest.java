package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.ontimize.atomicHotelsApiRest.model.core.dao.CountryDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@Spy
	ControlFields cf;

	@InjectMocks
	CountryService service;

	@Autowired
	CountryDao dao;

	EntityResult eR;

	@Nested
	@DisplayName("Test for Country queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class CountryQuery {

		// CountryQuery
		@Test
		@DisplayName("ControlFields usar reset()")
		void testCountryQueryControlFieldsReset() {
			service.countryQuery(TestingTools.getMapEmpty(), getColumsName());
			verify(cf, description("No se ha utilizado el metodo reset de ControlFields")).reset();
		}

		@Test
		@DisplayName("ControlFields usar validate() map y list")
		void testCountryQueryControlFieldsValidate() {
			try {
				doNothing().when(cf).restricPermissions(anyMap());
				service.countryQuery(TestingTools.getMapEmpty(), getColumsName());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyMap());
				verify(cf, description("No se ha utilizado el metodo validate de ControlFields")).validate(anyList());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}
		}

		@Test
		@DisplayName("Valores de entrada válidos")
		void testCountryQueryOK() {
		
			doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList());
			try {
				doNothing().when(cf).validate(anyMap());
			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}

			// válido: HashMap vacio (sin filtros)
			eR = service.countryQuery(TestingTools.getMapEmpty(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

			// válido: HashMap con filtro que existe (sin filtros)
			eR = service.countryQuery(getMapIso(), getColumsName());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, eR.getCode(), eR.getMessage());

		}

		@Test
		@DisplayName("Valores de entrada NO válidos")
		void testCountryQueryKO() {
			try {
				// lanzamos todas las excepciones de Validate para comprobar que están bien
				// recojidas.
				doThrow(new MissingFieldsException("testing")).when(cf).validate(anyMap());
				eR = service.countryQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals("testing", eR.getMessage(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(RestrictedFieldException.class).when(cf).validate(anyMap());
				eR = service.countryQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsException.class).when(cf).validate(anyMap());
				eR = service.countryQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(InvalidFieldsValuesException.class).when(cf).validate(anyMap());
				eR = service.countryQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertNotEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

				doThrow(LiadaPardaException.class).when(cf).validate(anyMap());
				eR = service.countryQuery(TestingTools.getMapEmpty(), getColumsName());
				assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
				assertEquals(ErrorMessage.UNKNOWN_ERROR, eR.getMessage(), eR.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
			}

		}
	}

	@Nested
	@DisplayName("Test for mapCountries quer")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class MapCountries {

		@Test
		@DisplayName("Obtener map")
		void testCountryQueryControlFieldsReset() {
			doReturn(getERCountries()).when(daoHelper).query(any(), anyMap(), anyList());
//			try {
//				doNothing().when(cf).validate(anyMap());
//			} catch (Exception e) {
//				e.printStackTrace();
//				fail(ErrorMessage.UNCAUGHT_EXCEPTION + e.getMessage());
//			}
			Map<String, String> resultado = service.mapCountries();
			assertTrue(resultado.containsKey("ES"));
			assertTrue(resultado.containsKey("GB"));
			assertTrue(resultado.containsKey("PT"));
			
			//consulta sin null (no debe llamar a daoHelper
			reset(daoHelper);
			resultado = service.mapCountries();
			assertTrue(resultado.containsKey("ES"));
			assertTrue(resultado.containsKey("GB"));
			assertTrue(resultado.containsKey("PT"));
		}

	}

	// datos

	List<String> getColumsName() {
		List<String> columns = new ArrayList<>() {
			{
				add(dao.ATTR_NAME);
			}
		};
		return columns;
	}

	HashMap<String, Object> getMapIso() {
		HashMap<String, Object> filters = new HashMap<>() {
			{
				put(dao.ATTR_ISO, "ES");
			}
		};
		return filters;
	};

	EntityResult getERCountries() {
		EntityResult er = new EntityResultMapImpl();
		er.addRecord(new HashMap<>() {
			{
				put(dao.ATTR_ISO, "ES");
				put(dao.ATTR_NAME, "España");
			}
		});
		er.addRecord(new HashMap<>() {
			{
				put(dao.ATTR_ISO, "GB");
				put(dao.ATTR_NAME, "Reino Unido");
			}
		});
		er.addRecord(new HashMap<>() {
			{
				put(dao.ATTR_ISO, "PT");
				put(dao.ATTR_NAME, "Portugal");
			}
		});
		return er;
	}
}

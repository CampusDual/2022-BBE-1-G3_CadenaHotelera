package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Types;
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
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CountryDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class CountryTest {
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
		
		//CountryQuery
				@Test
				@DisplayName("ControlFields usar reset()")
				void testCountryQueryControlFieldsReset() {
					service.countryQuery(TestingTools.getMapEmpty(), getColumsName());
					verify(cf, description("No se ha utilizado el método reset de ControlFields")).reset();
				}

				@Test
				@DisplayName("ControlFields usar validate() map y list")
				void testCountryQueryControlFieldsValidate() {
					service.countryQuery(TestingTools.getMapEmpty(), getColumsName());
					try {
						verify(cf, description("No se ha utilizado el método validate de ControlFields")).validate(anyMap());
						verify(cf, description("No se ha utilizado el método validate de ControlFields")).validate(anyList());
					} catch (Exception e) {
						e.printStackTrace();
						fail("excepción no capturada: " + e.getMessage());
					}
				}

				@Test
				@DisplayName("Valores de entrada válidos")
				void testCountryQueryOK() {
					doReturn(new EntityResultMapImpl()).when(daoHelper).query(any(), anyMap(), anyList());

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
						// lanzamos todas las excepciones de Validate para comprobar que están bien recogidas.
						
						doThrow(MissingFieldsException.class).when(cf).validate(anyMap());
						eR = service.countryQuery(TestingTools.getMapEmpty(), getColumsName());
						assertEquals(EntityResult.OPERATION_WRONG, eR.getCode(), eR.getMessage());
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
						fail("excepción no capturada: " + e.getMessage());
					}
				}			
			}

	HashMap<String, Object> getMapIso() {
		HashMap<String, Object> filters = new HashMap<>() {
			{
				put(dao.ATTR_ISO, "EL");
			}
		};
		return filters;
	};
	
	List<String> getColumsName() {
		List<String> columns = new ArrayList<>() {
			{
				add(dao.ATTR_NAME);
			}
		};
		return columns;
	}
}
		

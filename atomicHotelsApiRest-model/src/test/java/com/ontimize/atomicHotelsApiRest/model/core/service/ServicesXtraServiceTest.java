package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServicesXtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class ServicesXtraServiceTest {
	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@InjectMocks
	ServicesXtraService service;

	@Autowired
	ServicesXtraDao servicesXtraDao;
	@Autowired
	ValidateFields vf;

	@Nested
	@DisplayName("Test for ServicesXtra queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class ServicesXtraQuery {
		
		@Test
		@DisplayName("Obtain all data from ServicesXtra table")
		void when_queryOnlyWithAllColumns_return_allServicesXtraData() {
			doReturn(getAllServicesXtraData()).when(daoHelper).query(any(), anyMap(), anyList());
			EntityResult entityResult = service.servicesXtraQuery(new HashMap<>(), new ArrayList<>());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(3, entityResult.calculateRecordNumber());
		}

		@Test
		@DisplayName("Obtain all data columns from ServicesXtra table when sxt_id is -> 2")
		void when_queryAllColumns_return_specificData() { 
			HashMap<String, Object> keyMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 2);
				}
			};
			List<String> attrList = Arrays.asList(ServicesXtraDao.ATTR_ID, ServicesXtraDao.ATTR_NAME, ServicesXtraDao.ATTR_DESCRIPTION);
			doReturn(getSpecificServicesXtraData(keyMap, attrList)).when(daoHelper).query(any(), anyMap(), anyList());
			EntityResult entityResult = service.servicesXtraQuery(new HashMap<>(), new ArrayList<>());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(1, entityResult.calculateRecordNumber());
			assertEquals(2, entityResult.getRecordValues(0).get(ServicesXtraDao.ATTR_ID));
		}
		
		@Test
		@DisplayName("Obtain all data from ServicesXtra table using a personalized query")
		void when_queryOnlyWithAllColumns_return_allServicesXtraData_fromPersonalizedQuery() {
			HashMap<String, Object> keyMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 2);
				} 
			};
			List<String> attrList = Arrays.asList(ServicesXtraDao.ATTR_ID, ServicesXtraDao.ATTR_NAME, ServicesXtraDao.ATTR_DESCRIPTION);
			
			doReturn(getSpecificServicesXtraData(keyMap, attrList)).when(daoHelper).query(any(), anyMap(), anyList());
			EntityResult entityResult = service.servicesXtraQuery(new HashMap<>(), new ArrayList<>());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(1, entityResult.calculateRecordNumber());
			assertEquals(2, entityResult.getRecordValues(0).get(ServicesXtraDao.ATTR_ID));
		}

		@Test
		@DisplayName("Obtain all data columns from ServicesXtra table when sxt_id not exist")
		void when_queryAllColumnsNotExisting_return_empty() {
			HashMap<String, Object> keyMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 56);
				}
			};
			List<String> attrList = Arrays.asList(ServicesXtraDao.ATTR_ID, ServicesXtraDao.ATTR_NAME, ServicesXtraDao.ATTR_DESCRIPTION);
			when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificServicesXtraData(keyMap, attrList));
			EntityResult entityResult = service.servicesXtraQuery(new HashMap<>(), new ArrayList<>());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(0, entityResult.calculateRecordNumber());
		}

		@ParameterizedTest(name = "Obtain data with sxt_id -> {1}")
		@MethodSource("randomIDGenerator")
		@DisplayName("Obtain all data columns from ServicesXtra table when sxt_id is random")
		void when_queryAllColumnsWithRandomValue_return_specificData(int random) {
			HashMap<String, Object> keyMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, random);
				}
			};
			List<String> attrList = Arrays.asList(ServicesXtraDao.ATTR_ID, ServicesXtraDao.ATTR_NAME, ServicesXtraDao.ATTR_DESCRIPTION);
			when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificServicesXtraData(keyMap, attrList));
			EntityResult entityResult = service.servicesXtraQuery(new HashMap<>(), new ArrayList<>());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(1, entityResult.calculateRecordNumber());
			assertEquals(random, entityResult.getRecordValues(0).get(ServicesXtraDao.ATTR_ID));
		}

		public EntityResult getAllServicesXtraData() {
			List<String> columnList = Arrays.asList(ServicesXtraDao.ATTR_ID, ServicesXtraDao.ATTR_NAME, ServicesXtraDao.ATTR_DESCRIPTION);
			EntityResult er = new EntityResultMapImpl(columnList);
			er.addRecord(new HashMap<String, Object>() {
				{
					put(ServicesXtraDao.ATTR_ID, 1);
					put(ServicesXtraDao.ATTR_NAME, "paseacanes");
					put(ServicesXtraDao.ATTR_DESCRIPTION, "canes que pasean humanos");
				}
			});
			er.addRecord(new HashMap<String, Object>() {
				{
					put(ServicesXtraDao.ATTR_ID, 2);
					put(ServicesXtraDao.ATTR_NAME, "Gatería");
					put(ServicesXtraDao.ATTR_DESCRIPTION, "Humanos domesticados por gatos");
				}
			});
			er.addRecord(new HashMap<String, Object>() {
				{
					put(ServicesXtraDao.ATTR_ID, 3);
					put(ServicesXtraDao.ATTR_NAME, "Pajarería");
					put(ServicesXtraDao.ATTR_DESCRIPTION, "Vienen volando");
				}
			});
			er.setCode(EntityResult.OPERATION_SUCCESSFUL);
			er.setColumnSQLTypes(new HashMap<String, Number>() {
				{
					put(ServicesXtraDao.ATTR_ID, Types.INTEGER);
					put(ServicesXtraDao.ATTR_NAME, Types.VARCHAR);
					put(ServicesXtraDao.ATTR_DESCRIPTION, Types.VARCHAR);
				}
			});
			return er;
		}

		public EntityResult getSpecificServicesXtraData(Map<String, Object> keyValues, List<String> attributes) {
			EntityResult allData = this.getAllServicesXtraData();
			int recordIndex = allData.getRecordIndex(keyValues);
			HashMap<String, Object> recordValues = (HashMap) allData.getRecordValues(recordIndex);
			List<String> columnList = Arrays.asList(ServicesXtraDao.ATTR_ID, ServicesXtraDao.ATTR_NAME,ServicesXtraDao.ATTR_DESCRIPTION);
			EntityResult er = new EntityResultMapImpl(columnList);
			if (recordValues != null) {
				er.addRecord(recordValues);
			}
			er.setCode(EntityResult.OPERATION_SUCCESSFUL);
			er.setColumnSQLTypes(new HashMap<String, Number>() {
				{
					put(ServicesXtraDao.ATTR_ID, Types.INTEGER);
					put(ServicesXtraDao.ATTR_NAME, Types.VARCHAR);
					put(ServicesXtraDao.ATTR_DESCRIPTION, Types.VARCHAR);
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
	@DisplayName("Test for ServicesXtra inserts")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class InsertQuery {

		@Test
		@DisplayName("Insert ServicesXtra")
		void when_ServicesXtra_insert_is_succsessfull() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 1);
					put(ServicesXtraDao.ATTR_NAME, "paseacanes");
					put(ServicesXtraDao.ATTR_DESCRIPTION, "Servicio extra a registrar");
				}
			};
			EntityResult resultado = new EntityResultMapImpl();
			resultado.addRecord(attrMap);
			resultado.setCode(EntityResult.OPERATION_SUCCESSFUL);
			resultado.setMessage("Servicio extra registrado");
			when(daoHelper.insert(any(), anyMap())).thenReturn(resultado);
			EntityResult entityResult = service.servicesXtraInsert(attrMap);
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(entityResult.getMessage(), "ServiceXtra registrado");
		}

		@Test
		@DisplayName("Duplicated Key")
		void when_already_exist() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 1);
					put(ServicesXtraDao.ATTR_NAME, "paseacanes");
					put(ServicesXtraDao.ATTR_DESCRIPTION, "Este Servicio extra ya estaría registrado");
				}
			};
			when(daoHelper.insert(any(), anyMap())).thenThrow(DuplicateKeyException.class);
			EntityResult entityResult = service.servicesXtraInsert(attrMap);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(entityResult.getMessage(), ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}
		
		@Test
		@DisplayName("Creation Failure")
		void generic_fail() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 105); 		
					put(ServicesXtraDao.ATTR_NAME, "paseacaness");
					put(ServicesXtraDao.ATTR_DESCRIPTION, 6);		//puede q salte exception ya que no está contemplado un error de cast en descripción. PENDIENTE
				}
			};
	//		when(daoHelper.insert(any(), anyMap())).thenThrow(Exception.class);		//?¿¿?¿??¿? Pq da error si lanzo la excepción
			EntityResult entityResult = service.servicesXtraInsert(attrMap);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(entityResult.getMessage(), ErrorMessage.CREATION_ERROR);
		}

		@Test
		@DisplayName("Missing Fields")
		void when_field_is_empty_null_insert() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 1);
					put(ServicesXtraDao.ATTR_NAME, null);
					put(ServicesXtraDao.ATTR_DESCRIPTION, "Faltan campos no nullables");
				}
			};
			try (MockedStatic<ValidateFields> vf = Mockito.mockStatic(ValidateFields.class)) {
				vf.when(() -> ValidateFields.required(anyMap(), anyString())).thenThrow(MissingFieldsException.class);
			}
//				when(daoHelper.insert(any(), anyMap())).thenThrow(MissingFieldsException.class);
				EntityResult entityResult = service.servicesXtraInsert(attrMap);
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
				assertEquals(entityResult.getMessage(), ErrorMessage.REQUIRED_FIELD);
		//		assertEquals(ErrorMessage.CREATION_ERROR + "El campo " + ServicesXtraDao.ATTR_NAME + " es nulo",entityResult.getMessage());
		}
	}

	@Nested
	@DisplayName("Test for servicesXtra updates")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class UpdateQuery {

		@Test
		@DisplayName("Update servicesXtra")
		void when_servicesXtra_update_is_succsessfull() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 1);
				}
			};
			Map<String, Object> keyMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 1);
					put(ServicesXtraDao.ATTR_NAME, "1 actualizado");
					put(ServicesXtraDao.ATTR_DESCRIPTION, "servicio extra actualizado");
				}
			};
			EntityResult resultado = new EntityResultMapImpl();
			resultado.addRecord(keyMap);
			resultado.setCode(EntityResult.OPERATION_SUCCESSFUL);
			resultado.setMessage("Servicio extra actualizado");

			when(daoHelper.update(any(), anyMap(), anyMap())).thenReturn(resultado);
			EntityResult entityResult = service.servicesXtraUpdate(attrMap, keyMap);
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(entityResult.getMessage(), "ServiceXtra actualizado");
		}

		@Test
		@DisplayName("Duplicated Key")
		void when_already_exist() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 2);
				}
			};
			Map<String, Object> keyMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 2);// ???
					put(ServicesXtraDao.ATTR_NAME, "paseahumanos");// Este servicio extra ya existe
					put(ServicesXtraDao.ATTR_DESCRIPTION, "servicio actualizado");
				}
			};
			when(daoHelper.update(any(), anyMap(), anyMap())).thenThrow(DuplicateKeyException.class);
			EntityResult entityResult = service.servicesXtraUpdate(attrMap, keyMap);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(entityResult.getMessage(), ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		}

    	@Test
		@DisplayName("Update Failure")
    	void when_unable_update() {
    		Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 1);
				}
			};
			Map<String, Object> keyMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 1);
					put(ServicesXtraDao.ATTR_NAME, "/&");
					put(ServicesXtraDao.ATTR_DESCRIPTION, "actualízame");
				}
			};
//			when(daoHelper.update(any(), anyMap(), anyMap())).thenThrow(Exception.class);	////?¿¿?¿??¿? Pq da error si lanzo la excepción
			EntityResult entityResult = service.servicesXtraUpdate(attrMap, keyMap);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(entityResult.getMessage(), ErrorMessage.UPDATE_ERROR);
			
		}
    	
    	@Test
		@DisplayName("Missing Fields")			//emptyField o required?¿?¿? cuál ponemos. La excepción DataIntegrityViolationException de update, ya estaría comprobada en este test
		void when_field_is_empty_null_update() {
    		Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, null);
				}
			};
			Map<String, Object> keyMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 1);
					put(ServicesXtraDao.ATTR_NAME, "campo");
					put(ServicesXtraDao.ATTR_DESCRIPTION, "Faltan campos no nullables");
				}
			};
			try (MockedStatic<ValidateFields> vf = Mockito.mockStatic(ValidateFields.class)) {
				vf.when(() -> ValidateFields.required(anyMap(), anyString())).thenThrow(MissingFieldsException.class);
			}
//				when(daoHelper.update(any(), anyMap(), anyMap())).thenThrow(MissingFieldsException.class);
				EntityResult entityResult = service.servicesXtraUpdate(attrMap, keyMap);
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
				assertEquals(entityResult.getMessage(), ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
		}
    	
    	@Test
		@DisplayName("Required Fields")			
		void when_field_data_is_not_correct() {
    		Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 1);
				}
			};
			Map<String, Object> keyMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID,1 );
					put(ServicesXtraDao.ATTR_NAME, "");
					put(ServicesXtraDao.ATTR_DESCRIPTION, "Faltan campos no nullables");
				}
			};
			/*
			 * try (MockedStatic<ValidateFields> vf =
			 * Mockito.mockStatic(ValidateFields.class)) { vf.when(() ->
			 * ValidateFields.required(anyMap(),
			 * anyString())).thenThrow(MissingFieldsException.class); }
			 */
				when(daoHelper.update(any(), anyMap(), anyMap())).thenThrow(DataIntegrityViolationException.class);
				EntityResult entityResult = service.servicesXtraUpdate(attrMap, keyMap);
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
				assertEquals(entityResult.getMessage(), ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
		}

	}

	@Nested
	@DisplayName("Test for servicesXtra delete")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class DeleteQuery {

		@Test
		@DisplayName("Delete ok")
		void when_servicesXtra_delete_ok() {

			Map<String, Object> keyMapID = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 1);
				}
			};
			EntityResult result = new EntityResultMapImpl();
			result.addRecord(keyMapID);
			when(
					daoHelper.query(any(), anyMap(), anyList())		//en q estado capturamos el daohelper.query(con cualquier genérico)
					
					).thenReturn(result);

			when(daoHelper.delete(any(), anyMap())).thenReturn(new EntityResultMapImpl());
					
			EntityResult entityResult = service.servicesXtraDelete(keyMapID);
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode(), entityResult.getMessage());
			assertEquals(entityResult.getMessage(), "Servicio extra eliminado");
		}
		
		@Test
		@DisplayName("Delete ko")		//?¿??NOMBRE?
		void when_servicesXtra_delete_ko() {			//InvalidFieldsValuesException  (isInt)

			Map<String, Object> keyMapID = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, "blabla");
				}
			};
			EntityResult result = new EntityResultMapImpl();
			result.addRecord(keyMapID);
			lenient().when(				//omitir los stubs estrictos, stubbing indulgente.método estático Mockito.lenient() para habilitar el stubing indulgente en el método add de nuestra lista simulada.
					daoHelper.query(any(), anyMap(), anyList())		//en q estado capturamos el daohelper.query(con cualquier genérico)
					
					).thenReturn(result);

			lenient().when(daoHelper.delete(any(), anyMap())).thenReturn(new EntityResultMapImpl());
					
			EntityResult entityResult = service.servicesXtraDelete(keyMapID);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(), entityResult.getMessage());
			assertEquals(entityResult.getMessage(), ErrorMessage.REQUIRED_FIELD);
		}		
		
		
		
		
		@Test
		@DisplayName("Missing Fields")			//emptyField o required?¿?¿? cuál ponemos. La excepción DataIntegrityViolationException de update, ya estaría comprobada en este test
		void when_field_is_empty_null_delete() {
    		
			/*
			 * try (MockedStatic<ValidateFields> vf = Mockito.mockStatic(ValidateFields.class)) {
//      vf.when(() -> ValidateFields.required(anyMap(), anyString())).thenThrow(MissingFieldsException.class);}
//      when(daoHelper.insert(any(), anyMap())).thenThrow(MissingFieldsException.class);
			 */
			
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_NAME, "loco");
					
				}
			};
				try (MockedStatic<ValidateFields> vf = Mockito.mockStatic(ValidateFields.class)) {
					vf.when(() -> ValidateFields.required(anyMap(), anyString())).thenThrow(MissingFieldsException.class);
				}
				EntityResult entityResult = service.servicesXtraDelete(attrMap);
				assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
				assertEquals(entityResult.getMessage(), ErrorMessage.REQUIRED_FIELD);
			}

		@Test
		@DisplayName("Generic failure")
		void when_already_exist() {
			Map<String, Object> attrMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, 1);
				}
			};
			EntityResult entityResult = service.servicesXtraDelete(attrMap);
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
			assertEquals(entityResult.getMessage(), ErrorMessage.DELETE_ERROR);
		}

		

	}
}
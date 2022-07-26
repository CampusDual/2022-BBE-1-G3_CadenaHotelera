package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doReturn;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BedComboDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServicesXtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)

class HotelServiceExtraServiceTest {

	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@InjectMocks
	HotelServiceExtraService service;
	
	@Autowired
	ValidateFields vf;

	// @Mock/@Autowired/@InjectMocks
	MissingFieldsException e;

		@Nested
		@DisplayName("hotelServiceExtraServiceTest queries")
		@TestInstance(TestInstance.Lifecycle.PER_CLASS)
		public class hotelServiceExtraServiceTest {
			
			@Test
			@DisplayName("Obtain all data from hotelServiceExtraService table")
			void when_queryOnlyWithAllColumns_return_allhotelServiceExtraQueryData() {
				doReturn(getAllhotelServiceExtraData()).when(daoHelper).query(any(), anyMap(), anyList());
				EntityResult entityResult = service.hotelServiceExtraQuery(new HashMap<>(), new ArrayList<>());
				assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
				assertEquals(3, entityResult.calculateRecordNumber());
			}

			@Test
			@DisplayName("Obtain all data columns from hotelServiceExtraService table when hsx_id is -> 1")
			void when_queryAllColumns_return_specificData() {
				HashMap<String, Object> keyMap = new HashMap<>(){
					{
						put(HotelServiceExtraDao.ATTR_ID, 1);
					}
				};
				List<String> attrList = Arrays.asList(HotelServiceExtraDao.ATTR_ID, HotelServiceExtraDao.ATTR_ID_HTL, HotelServiceExtraDao.ATTR_ID_SXT, HotelServiceExtraDao.ATTR_PRECIO);
				doReturn(getSpecificHotelServiceExtraServiceData(keyMap, attrList)).when(daoHelper).query(any(), anyMap(), anyList());
				EntityResult entityResult = service.hotelServiceExtraQuery(new HashMap<>(), new ArrayList<>());
				assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
				System.out.println(entityResult.calculateRecordNumber());
				assertEquals(1, entityResult.calculateRecordNumber());
				assertEquals(1, entityResult.getRecordValues(0).get(HotelServiceExtraDao.ATTR_ID));
			}
			
			@Test
			@DisplayName("Obtain all data from hotelServiceExtraQuery table using a personalized query")
			void when_queryOnlyWithAllColumns_return_allhotelServiceExtraQueryfromPersonalizedQuery() {
				HashMap<String, Object> keyMap = new HashMap<>() {
					{
						put(HotelServiceExtraDao.ATTR_ID, 1);
					} 
				};
				List<String> attrList = Arrays.asList(HotelServiceExtraDao.ATTR_ID, HotelServiceExtraDao.ATTR_ID_HTL, HotelServiceExtraDao.ATTR_ID_SXT, HotelServiceExtraDao.ATTR_PRECIO);
				
				doReturn(getSpecificHotelServiceExtraServiceData(keyMap, attrList)).when(daoHelper).query(any(), anyMap(), anyList());
				EntityResult entityResult = service.hotelServiceExtraQuery(new HashMap<>(), new ArrayList<>());
				assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
				assertEquals(1, entityResult.calculateRecordNumber());
				assertEquals(2, entityResult.getRecordValues(0).get(HotelServiceExtraDao.ATTR_ID));
			}

			@Test
			@DisplayName("Obtain all data columns from HotelServiceExtraService table when hsx_id not exist")
			void when_queryAllColumnsNotExisting_return_empty() {
				HashMap<String, Object> keyMap = new HashMap<>() {

					{
						put(HotelServiceExtraDao.ATTR_ID, 56);
					}
				};
				List<String> attrList = Arrays.asList(HotelServiceExtraDao.ATTR_ID, HotelServiceExtraDao.ATTR_ID_HTL, HotelServiceExtraDao.ATTR_ID_SXT, HotelServiceExtraDao.ATTR_PRECIO);
				when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificHotelServiceExtraServiceData(keyMap, attrList));
				EntityResult entityResult = service.hotelServiceExtraQuery(new HashMap<>(), new ArrayList<>());
				assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
				assertEquals(0, entityResult.calculateRecordNumber());
			}

			@ParameterizedTest(name = "Obtain data with sxt_id -> {1}")
			@MethodSource("randomIDGenerator")
			@DisplayName("All data HotelServiceExtraService table when hsx_htl_id(Hotel id) is random")
			void when_queryAllColumnsWithRandomValue_return_specificData(int random) {
				HashMap<String, Object> keyMap = new HashMap<>() {
					{
						put(HotelServiceExtraDao.ATTR_ID_HTL, random);
					}
				};
				List<String> attrList = Arrays.asList(HotelServiceExtraDao.ATTR_ID, HotelServiceExtraDao.ATTR_ID_HTL, HotelServiceExtraDao.ATTR_ID_SXT, HotelServiceExtraDao.ATTR_PRECIO);
				when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificHotelServiceExtraServiceData(keyMap, attrList));
				EntityResult entityResult = service.hotelServiceExtraQuery(new HashMap<>(), new ArrayList<>());
				assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
				assertEquals(1, entityResult.calculateRecordNumber());
				assertEquals(random, entityResult.getRecordValues(0).get(HotelServiceExtraDao.ATTR_ID_HTL));
			}

			public EntityResult getAllhotelServiceExtraData() {
				List<String> columnList = Arrays.asList(HotelServiceExtraDao.ATTR_ID, HotelServiceExtraDao.ATTR_ID_HTL, HotelServiceExtraDao.ATTR_ID_SXT, HotelServiceExtraDao.ATTR_PRECIO);
				EntityResult er = new EntityResultMapImpl(columnList);
				er.addRecord(new HashMap<String, Object>() {
					{
						put(HotelServiceExtraDao.ATTR_ID_HTL, 1);
						put(HotelServiceExtraDao.ATTR_ID_SXT, 8);
						put(HotelServiceExtraDao.ATTR_PRECIO, 15.07);
					}
				});
				er.addRecord(new HashMap<String, Object>() {
					{
						put(HotelServiceExtraDao.ATTR_ID_HTL, 4);
						put(HotelServiceExtraDao.ATTR_ID_SXT, 5);
						put(HotelServiceExtraDao.ATTR_PRECIO, 15.07);
					}
				});
				er.addRecord(new HashMap<String, Object>() {
					{
						put(HotelServiceExtraDao.ATTR_ID_HTL, 2);
						put(HotelServiceExtraDao.ATTR_ID_SXT, 8);
						put(HotelServiceExtraDao.ATTR_PRECIO, 15.07);
					}
				});
				er.setCode(EntityResult.OPERATION_SUCCESSFUL);
				er.setColumnSQLTypes(new HashMap<String, Number>() {
					{
						put(HotelServiceExtraDao.ATTR_ID_HTL, Types.INTEGER);
						put(HotelServiceExtraDao.ATTR_ID_SXT, Types.INTEGER);
						put(HotelServiceExtraDao.ATTR_PRECIO, Types.FLOAT);
					}
				});
				return er;
			}

			public EntityResult getSpecificHotelServiceExtraServiceData(Map<String, Object> keyValues, List<String> attributes) {
				EntityResult allData = this.getAllhotelServiceExtraData();
				int recordIndex = allData.getRecordIndex(keyValues);
				@SuppressWarnings("unchecked")
				HashMap<String, Object> recordValues = (HashMap) allData.getRecordValues(recordIndex);
				List<String> columnList = Arrays.asList(HotelServiceExtraDao.ATTR_ID, HotelServiceExtraDao.ATTR_ID_HTL, HotelServiceExtraDao.ATTR_ID_SXT, HotelServiceExtraDao.ATTR_PRECIO);
				EntityResult er = new EntityResultMapImpl(columnList);
				if (recordValues != null) {
					er.addRecord(recordValues);
				}
				er.setCode(EntityResult.OPERATION_SUCCESSFUL);
				er.setColumnSQLTypes(new HashMap<String, Number>() {
					{
						put(HotelServiceExtraDao.ATTR_ID_HTL, Types.INTEGER);
						put(HotelServiceExtraDao.ATTR_ID_SXT, Types.INTEGER);
						put(HotelServiceExtraDao.ATTR_PRECIO, Types.FLOAT);
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
		

}

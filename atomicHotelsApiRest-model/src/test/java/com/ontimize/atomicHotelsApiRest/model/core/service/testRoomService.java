package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.mockito.Mockito.*;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ComboBoxEditor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class testRoomService {
	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@InjectMocks
	RoomService service;

	@Nested
	@DisplayName("Test for Education queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class EducationQuery {
//	@Test
//	 void when_candidateHasNonRelatedData_return_onlyRelatedData() {
//        Map<String, Object> keyMap = new HashMap<>() {{
//            put("bkg_stb_id", 1);
//            put("bkg_cst_id", 2);
//            put("bkg_rm_id", 6);
//            put("bkg_start", "2022-08-12");
//            put("bkg_end", "2023-01-19");
//        }};
//
//        String[] attrToExclude = new String[]{CandidateDao.ATTR_EDUCATION, CandidateDao.ATTR_EXPERIENCE_LEVEL,
//                CandidateDao.ATTR_ORIGIN, CandidateDao.ATTR_PROFILE, CandidateDao.ATTR_STATUS};
//
//        Map<String, Object> nonRelatedData = this.candidateService.removeNonRelatedData(attrMap, attrToExclude);
//
//        assertTrue(!(attrMap.get(CandidateDao.ATTR_EDUCATION) instanceof String));
//        assertTrue(!(attrMap.get(CandidateDao.ATTR_EXPERIENCE_LEVEL) instanceof String));
//        assertTrue(!(attrMap.get(CandidateDao.ATTR_ORIGIN) instanceof String));
//        assertTrue(!(attrMap.get(CandidateDao.ATTR_PROFILE) instanceof String));
//        assertTrue(!(attrMap.get(CandidateDao.ATTR_STATUS) instanceof String));
//    }

		@Test
		@DisplayName("Obtain all data from room table")
		void when_queryOnlyWithAllColumns_return_allEducationData() {
			List<String> columnList = Arrays.asList(RoomDao.ATTR_ID, RoomDao.ATTR_NUMBER);
			doReturn(getAllRoomData()).when(daoHelper).query(any(), anyMap(), anyList());
//         doReturn(getAllEducationData()).when(daoHelper).query(any(), anyMap(), anyList());
			EntityResult entityResult = service.roomQuery(new HashMap<>(), new ArrayList<>());
			assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
			assertEquals(2, entityResult.calculateRecordNumber());
		}

		public EntityResult getAllRoomData() {
			List<String> columnList = Arrays.asList(RoomDao.ATTR_ID, RoomDao.ATTR_NUMBER);
			EntityResult er = new EntityResultMapImpl(columnList);
			er.addRecord(new HashMap<String, Object>() {
				{
					put(RoomDao.ATTR_ID, 0);
					put(RoomDao.ATTR_NUMBER, 101);
				}
			});
			er.addRecord(new HashMap<String, Object>() {
				{
					put(RoomDao.ATTR_ID, 1);
					put(RoomDao.ATTR_NUMBER, 102);
				}
			});
			er.setCode(EntityResult.OPERATION_SUCCESSFUL);
			er.setColumnSQLTypes(new HashMap<String, Number>() {
				{
					put(RoomDao.ATTR_ID, Types.INTEGER);
					put(RoomDao.ATTR_NUMBER, Types.INTEGER);
				}
			});
			return er;
		}

	}
}

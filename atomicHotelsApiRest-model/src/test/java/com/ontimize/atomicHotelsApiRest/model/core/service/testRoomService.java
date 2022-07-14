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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

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

	@Autowired
	RoomDao roomDao;
	
	@Nested
	@DisplayName("Test for Education queries")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	
	public class EducationQuery {
		@Test
		@DisplayName("Clave duplicada")
		void when_already_exist() {
			when(daoHelper.insert(any(),anyMap())).thenThrow(DuplicateKeyException.class);		
			EntityResult entityResult = service.roomInsert(new HashMap<>());
			assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode(),entityResult.getMessage());

		}

	}
}

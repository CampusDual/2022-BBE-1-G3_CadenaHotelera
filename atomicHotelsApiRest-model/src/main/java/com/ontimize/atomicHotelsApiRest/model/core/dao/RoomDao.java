package com.ontimize.atomicHotelsApiRest.model.core.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("RoomDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/RoomDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class RoomDao extends OntimizeJdbcDaoSupport{
	
	public static final String TAG = "rm_";
	public static final String ATTR_ID = TAG +"id";
	public static final String ATTR_HOTEL_ID = TAG +"htl_id";
	public static final String ATTR_NUMBER = TAG +"number";
	public static final String ATTR_SQUARE_METERS = TAG +"square_meters";
	public static final String ATTR_ROOM_TYPE_ID = TAG +"rmt_id";
	public static final String ATTR_STATUS = TAG +"status";
	
	public static final Map<String,type> fields = new HashMap<>() {{
		put(ATTR_ID,type.INTEGER);
		put(ATTR_HOTEL_ID,type.INTEGER);
		put(ATTR_NUMBER,type.INTEGER_UNSIGNED);
		put(ATTR_SQUARE_METERS,type.INTEGER_UNSIGNED);
		put(ATTR_ROOM_TYPE_ID,type.INTEGER);
		put(ATTR_STATUS,type.BOOLEAN);	
	}};


}



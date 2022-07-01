package com.ontimize.atomicHotelsApiRest.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

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



}



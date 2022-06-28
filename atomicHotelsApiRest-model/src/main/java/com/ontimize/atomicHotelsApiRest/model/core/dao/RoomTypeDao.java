package com.ontimize.atomicHotelsApiRest.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("RoomTypeDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/RoomTypeDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class RoomTypeDao extends OntimizeJdbcDaoSupport {
	public static final String TAG = "rmt_";
	public static final String ATTR_ID = TAG +"rmt_id";
	public static final String ATTR_NAME = TAG +"name";
	public static final String ATTR_DESCRIPTION = TAG +"description";
	public static final String ATTR_PRICE = TAG +"price";
	public static final String ATTR_HOTEL_ID = TAG +"htl_id";
	public static final String ATTR_BEDS_COMBO = TAG +"bdc_id";	
}
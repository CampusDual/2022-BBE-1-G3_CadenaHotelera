package com.ontimize.atomicHotelsApiRest.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("HotelDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/HotelDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class HotelDao extends OntimizeJdbcDaoSupport {
	
	//Consulta fantasma??
	
	public static final String TAG = "htl_";
	public static final String ATTR_ID = TAG+"id";
	public static final String ATTR_NAME = TAG+"name";
	public static final String ATTR_STREET = TAG+"street";
	public static final String ATTR_CITY = TAG+"city";
	public static final String ATTR_CP = TAG+"postal_code";
	public static final String ATTR_STATE = TAG+"state";
	public static final String ATTR_COUNTRY = TAG+"country";
	public static final String ATTR_PHONE = TAG+"phone";
	public static final String ATTR_EMAIL = TAG+"email";
	public static final String ATTR_DESCRIPTION = TAG+"description";
	public static final String ATTR_IS_OPEN = TAG+"is_open";

}
package com.ontimize.atomicHotelsApiRest.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("HotelDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/HotelDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class HotelDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_ID = "htl_id";
	public static final String ATTR_NAME = "htl_name";
	public static final String ATTR_STREET = "htl_street";
	public static final String ATTR_CITY = "htl_city";
	public static final String ATTR_CP = "htl_postal_code";
	public static final String ATTR_STATE = "htl_state";
	public static final String ATTR_COUNTRY = "htl_country";
	public static final String ATTR_PHONE = "htl_phone";
	public static final String ATTR_EMAIL = "htl_email";
	public static final String ATTR_DESCRIPTION = "htl_description";
	public static final String ATTR_IS_OPEN = "htl_is_open";

}
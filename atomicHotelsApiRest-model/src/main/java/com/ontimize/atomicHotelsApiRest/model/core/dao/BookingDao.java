package com.ontimize.atomicHotelsApiRest.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("BookingDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/BookingDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class BookingDao extends OntimizeJdbcDaoSupport {
	public static final String TAG = "bkg_";
	public static final String ATTR_ID = TAG +"id";
	public static final String ATTR_OBSERVATIONS = TAG +"observations";
	public static final String ATTR_CHECKIN = TAG +"checkin";
	public static final String ATTR_CHECKOUT = TAG +"checkout";
	public static final String ATTR_STATUS_ID = TAG +"stb_id";
	public static final String ATTR_CUSTOMER_ID = TAG +"cst_id";
	public static final String ATTR_ROOM_ID = TAG +"rm_id";
}
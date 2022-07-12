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
	public static final String ATTR_START = TAG +"start";
	public static final String ATTR_END = TAG +"end";
	public static final String ATTR_CHECKIN = TAG +"checkin";
	public static final String ATTR_CHECKOUT = TAG +"checkout";
	public static final String ATTR_CANCELED = TAG +"canceled";
	public static final String ATTR_CREATED= TAG +"created";
	public static final String ATTR_CUSTOMER_ID = TAG +"cst_id";
	public static final String ATTR_ROOM_ID = TAG +"rm_id";
	public static final String NON_ATTR_ACTION = "action";
	public enum Status{CONFIRMED,IN_PROGRESS,COMPLETED,CANCELED}
	public enum Action{CHECK_IN,CHECK_OUT,CANCEL}

}
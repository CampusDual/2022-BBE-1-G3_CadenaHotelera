package com.ontimize.atomicHotelsApiRest.model.core.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
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
	public static final String ATTR_USER = "user_";
	public static final String NON_ATTR_ACTION = "action";
	public enum Status{CONFIRMED,IN_PROGRESS,COMPLETED,CANCELED}
	public enum Action{CHECKIN,CHECKOUT,CANCEL}
	
	//TODO asegurarse de que los tipos son los adecuados. Ver qué hacer con las últimas variables anteriores
	public static final Map<String,type> fields = new HashMap<>() {{
		put(ATTR_ID,type.INTEGER);
		put(ATTR_OBSERVATIONS,type.STRING);
		put(ATTR_START,type.DATE);
		put(ATTR_END,type.DATE);
		put(ATTR_CHECKIN,type.DATETIME);
		put(ATTR_CHECKOUT,type.DATETIME);	
		put(ATTR_CANCELED,type.DATETIME);	
		put(ATTR_CREATED,type.DATETIME);	
		put(ATTR_CUSTOMER_ID,type.INTEGER);	
		put(ATTR_ROOM_ID,type.INTEGER);
		put(ATTR_USER,type.NO_EMPTY_SMALL_STRING);
		put(NON_ATTR_ACTION,type.BOOKING_ACTION);
	}};

}
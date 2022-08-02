package com.ontimize.atomicHotelsApiRest.model.core.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("ReceiptDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/ReceiptDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class ReceiptDao extends OntimizeJdbcDaoSupport{

	public static final String TAG = "rcp_";
	public static final String ATTR_ID = TAG+"id";
	public static final String ATTR_DATE = TAG+"date";
	public static final String ATTR_BOOKING_ID = TAG+"bkg_id";
	public static final String ATTR_TOTAL_SERVICES = TAG+"total_services";
	public static final String ATTR_DIAS = TAG+"days";
	public static final String ATTR_TOTAL_ROOM = TAG+"total_room";
	public static final String ATTR_TOTAL = TAG+"total";
	public static final String ATTR_SERVICIOS_EXTRA ="extra_services";
	
	public static final Map<String,type> fields = new HashMap<>() {{
		put(ATTR_ID,type.INTEGER);
		put(ATTR_DATE,type.DATETIME);
		put(ATTR_BOOKING_ID,type.INTEGER);
		put(ATTR_TOTAL_SERVICES,type.PRICE);
		put(ATTR_DIAS,type.INTEGER);
		put(ATTR_TOTAL_ROOM,type.PRICE);
		put(ATTR_TOTAL,type.PRICE);				
	}};
	
}

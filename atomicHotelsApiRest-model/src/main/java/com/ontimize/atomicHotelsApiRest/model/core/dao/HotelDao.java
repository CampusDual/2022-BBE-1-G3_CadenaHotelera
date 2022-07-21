package com.ontimize.atomicHotelsApiRest.model.core.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes;
import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("HotelDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/HotelDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class HotelDao extends OntimizeJdbcDaoSupport {
	

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
	
	public static final Map<String,type> fields = new HashMap<>() {{
		put(ATTR_ID,type.INTEGER);
		put(ATTR_NAME,type.STRING);
		put(ATTR_STREET,type.STRING);
		put(ATTR_CITY,type.STRING);
		put(ATTR_CP,type.STRING);
		put(ATTR_STATE,type.STRING);
//		put(ATTR_COUNTRY,type.COUNTRY);
		put(ATTR_COUNTRY,type.STRING);
//		put(ATTR_PHONE,type.PHONE);
		put(ATTR_PHONE,type.STRING);
//		put(ATTR_EMAIL,type.EMAIL);
		put(ATTR_EMAIL,type.STRING);
		put(ATTR_DESCRIPTION,type.STRING);
//		put(ATTR_IS_OPEN,type.BOOLEAN);			
		put(ATTR_IS_OPEN,type.INTEGER);	
	}};
	
	
}
package com.ontimize.atomicHotelsApiRest.model.core.dao;


import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;


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
	public static final String ATTR_COUNTRY = TAG+"cnt_iso";
	public static final String ATTR_PHONE = TAG+"phone";	
	public static final String ATTR_EMAIL = TAG+"email";
	public static final String ATTR_DESCRIPTION = TAG+"description";
	public static final String ATTR_IS_OPEN = TAG+"is_open";
	
	public static final String ATTR_MAXIMUN_CAPACITY = TAG +"maximum_capacity";
	public static final String ATTR_OCCUPANCY = TAG +"occupancy";
	public static final String ATTR_LAT = TAG + "lat";
	public static final String ATTR_LON = TAG + "lon";
	public static final String ATTR_FROM = "from";
	public static final String ATTR_TO = "to";
	public static final String ATTR_CAPACITY_IN_DATE_RANGE="capacity_in_date_range";
	public static final String ATTR_OCCUPANCY_IN_DATE_RANGE = "occupancy_in_date_range";
	public static final String ATTR_OCCUPANCY_PERCENTAGE_IN_DATE_RANGE="occupancy_percentage_in_date_range";
	public static final String ATTR_CATEGORY="category";
	public static final String ATTR_RADIUS="radius";
	
	
	public static final Map<String,type> fields = new HashMap<>() {{
		put(ATTR_ID,type.INTEGER);
		put(ATTR_NAME,type.NO_EMPTY_STRING);
		put(ATTR_STREET,type.STRING);
		put(ATTR_CITY,type.STRING);
		put(ATTR_CP,type.STRING);
		put(ATTR_STATE,type.STRING);
		put(ATTR_COUNTRY,type.COUNTRY);		
		put(ATTR_PHONE,type.PHONE);
		put(ATTR_EMAIL,type.EMAIL);
		put(ATTR_DESCRIPTION,type.STRING);
		put(ATTR_IS_OPEN,type.BOOLEAN);	
		put(ATTR_LON,type.SMALL_STRING);
		put(ATTR_LAT, type.SMALL_STRING);
		put(ATTR_CATEGORY, type.STRING);
		put(ATTR_RADIUS, type.STRING);
	}};
	
	
}
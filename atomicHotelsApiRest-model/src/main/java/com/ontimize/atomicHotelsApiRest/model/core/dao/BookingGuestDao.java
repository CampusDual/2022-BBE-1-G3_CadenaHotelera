package com.ontimize.atomicHotelsApiRest.model.core.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("BookingGuestDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/BookingGuestDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class BookingGuestDao extends OntimizeJdbcDaoSupport {
	
	public static final String TAG = "bgs_";
	public static final String ATTR_ID = TAG +"id";
	public static final String ATTR_BKG_ID = TAG+"bkg_id";
	public static final String ATTR_CST_ID = TAG+"cst_id";
	public static final String ATTR_REGISTRATION_DATE = TAG+"registration_date";
	public static final String ATTR_TOTAL_GUESTS ="total_guests";
	public static final String ATTR_TOTAL_SLOTS ="total_slots";

	public static final Map<String, type> fields = new HashMap<>() {
		{
			put(ATTR_ID,type.INTEGER);
			put(ATTR_BKG_ID, type.INTEGER);
			put(ATTR_CST_ID, type.INTEGER);
			put(ATTR_REGISTRATION_DATE, type.DATETIME);
			put(ATTR_TOTAL_GUESTS, type.INTEGER);
			put(ATTR_TOTAL_SLOTS,type.INTEGER);
		}
	};
}

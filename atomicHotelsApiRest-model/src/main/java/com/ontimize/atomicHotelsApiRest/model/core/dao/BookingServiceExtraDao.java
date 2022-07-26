package com.ontimize.atomicHotelsApiRest.model.core.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("BookingServiceExtraDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/BookingServiceExtraDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class BookingServiceExtraDao extends OntimizeJdbcDaoSupport {
		public static final String TAG = "bsx_";
		public static final String ATTR_ID= TAG+"id";
		public static final String ATTR_ID_BKG = TAG+"bkg_id";
		public static final String ATTR_ID_SXT = TAG+"sxt_id";
		public static final String ATTR_ID_UNITS = TAG+"units";
		public static final String ATTR_PRECIO = TAG+"precio";
		public static final String ATTR_DATE = TAG+"date";
		
		public static final Map<String,type> fields = new HashMap<>() {{
			put(ATTR_ID,type.INTEGER);
			put(ATTR_ID_BKG,type.INTEGER);
			put(ATTR_ID_SXT,type.INTEGER);
			put(ATTR_ID_UNITS,type.INTEGER_UNSIGNED);
			put(ATTR_PRECIO,type.PRICE);
			put(ATTR_DATE,type.DATETIME);				
		}};
}

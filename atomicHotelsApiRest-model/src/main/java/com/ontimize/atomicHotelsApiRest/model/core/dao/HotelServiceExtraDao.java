package com.ontimize.atomicHotelsApiRest.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("HotelServiceExtraDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/HotelServiceExtraDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class HotelServiceExtraDao extends OntimizeJdbcDaoSupport {
		public static final String TAG = "hsx_";
		public static final String ATTR_ID_HSX = TAG+"id";
		public static final String ATTR_ID_HTL = TAG+"htl_id";
		public static final String ATTR_ID_SXT = TAG+"sxt_id";
		public static final String ATTR_PRECIO = TAG+"precio";
}

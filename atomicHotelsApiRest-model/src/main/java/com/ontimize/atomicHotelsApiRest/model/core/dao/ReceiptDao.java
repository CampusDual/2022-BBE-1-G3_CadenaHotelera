package com.ontimize.atomicHotelsApiRest.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

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
	public static final String ATTR_TOTAL = TAG+"total";
}

package com.ontimize.atomicHotelsApiRest.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import java.util.*;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("BedComboDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/BedComboDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class BedComboDao extends OntimizeJdbcDaoSupport {
	
	public static final String TAG = "bdc_";
	public static final String ATTR_ID = TAG+"id";
	public static final String ATTR_NAME = TAG+"name";
	public static final String ATTR_SLOTS = TAG+"slots";
	
	public static final Map<String,type> fields=new HashMap<>()
	{
	{
		put(ATTR_ID,type.INTEGER);
		put(ATTR_NAME,type.STRING);
		put(ATTR_SLOTS,type.INTEGER_UNSIGNED);
	}
	};
}

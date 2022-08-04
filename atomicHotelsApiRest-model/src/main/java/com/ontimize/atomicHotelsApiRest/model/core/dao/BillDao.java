package com.ontimize.atomicHotelsApiRest.model.core.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("BillDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/BillDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class BillDao extends OntimizeJdbcDaoSupport {
		public static final String TAG = "bll_";
		public static final String ATTR_ID = TAG+"id";
		public static final String ATTR_ID_HTL = TAG+"htl_id";
		public static final String ATTR_ID_DPT = TAG+"dpt_id";
		public static final String ATTR_CONCEPT = TAG+"concept";
		public static final String ATTR_DATE = TAG+"date";
		public static final String ATTR_AMOUNT = TAG+"amount";
		
		
		public static final Map<String,type> fields = new HashMap<>() {{
			put(ATTR_ID,type.INTEGER);
			put(ATTR_ID_HTL,type.INTEGER);
			put(ATTR_ID_DPT,type.INTEGER);
			put(ATTR_CONCEPT,type.STRING);
			put(ATTR_DATE,type.DATETIME);
			put(ATTR_AMOUNT,type.PRICE);
		}};
}


package com.ontimize.atomicHotelsApiRest.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import java.util.*;
@Repository("CreditCardDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/CreditCardDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class CreditCardDao extends OntimizeJdbcDaoSupport {
		public static final String TAG = "crd_";
		public static final String ATTR_ID = TAG+"id";
		public static final String ATTR_NUMBER = TAG+"number";
		public static final String ATTR_DATE_EXPIRY = TAG+"date_expiry";
		
		public static final Map<String,type> fields=new HashMap<String,type>(){
			{
				put(ATTR_ID,type.INTEGER);
				put(ATTR_NUMBER,type.CREDIT_CARD);
				put(ATTR_DATE_EXPIRY,type.EXPIRATION_DATE);
			}
			};
		
}

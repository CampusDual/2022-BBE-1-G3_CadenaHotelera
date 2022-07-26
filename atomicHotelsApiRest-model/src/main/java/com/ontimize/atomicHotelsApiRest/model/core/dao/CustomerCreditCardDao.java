package com.ontimize.atomicHotelsApiRest.model.core.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("CustomerCreditCardDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/CustomerCreditCardDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class CustomerCreditCardDao extends OntimizeJdbcDaoSupport {
	public static final String ATTR_CST_ID ="cst_id";
	public static final String ATTR_CRD_ID ="crd_id";
	
	public static final Map<String,type> fields=new HashMap<String,type>(){
		{
			put(ATTR_CST_ID,type.INTEGER);
			put(ATTR_CRD_ID,type.INTEGER);
		}
		};

}

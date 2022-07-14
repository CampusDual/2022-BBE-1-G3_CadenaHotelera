package com.ontimize.atomicHotelsApiRest.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("CustomerCreditCardDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/CustomerCreditCardDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class CustomerCreditCardDao extends OntimizeJdbcDaoSupport {
	public static final String ATTR_CST_ID ="cst_id";
	public static final String ATTR_CRD ="crd_id";

}

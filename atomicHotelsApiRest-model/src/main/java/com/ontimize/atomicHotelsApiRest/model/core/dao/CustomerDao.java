package com.ontimize.atomicHotelsApiRest.model.core.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("CustomerDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/CustomerDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class CustomerDao extends OntimizeJdbcDaoSupport {

	public static final String TAG = "cst_";
	public static final String ATTR_ID = TAG+"id";
	public static final String ATTR_NAME = TAG+"name";
	public static final String ATTR_SURNAMES = TAG+"surnames";
	public static final String ATTR_EMAIL = TAG+"email";
	public static final String ATTR_BIRTH_DATE = TAG+"birth_date";
	public static final String ATTR_DNI = TAG+"dni";
	public static final String ATTR_ADDRESS = TAG+"address";
	public static final String ATTR_NATIONALITY = TAG+"nationality";
	public static final String ATTR_PHONE = TAG+"phone";
	public static final String ATTR_CREDITCARD = TAG+"creditcard";
	public static final String ATTR_VALID_DATE = TAG+"valid_date";
//	public static final String ATTR_MAIL_AGREEMENT = TAG+"mailagreement";
	public static final String ATTR_MAIL_AGREEMENT = "mailagreement";
	
	//TODO pendiente cambiar todo esto!!!
	public static final Map<String,type> fields = new HashMap<>() {{
		put(ATTR_ID,type.INTEGER);
		put(ATTR_NAME,type.STRING);
		put(ATTR_SURNAMES,type.STRING);
		put(ATTR_EMAIL,type.EMAIL);
		put(ATTR_BIRTH_DATE,type.DATE);
		put(ATTR_DNI,type.STRING);	
		put(ATTR_ADDRESS,type.STRING);	
		put(ATTR_NATIONALITY,type.COUNTRY);	
		put(ATTR_PHONE,type.PHONE);	
		put(ATTR_CREDITCARD,type.CREDIT_CARD);
		put(ATTR_VALID_DATE,type.EXPIRATION_DATE);
		put(ATTR_MAIL_AGREEMENT,type.BOOLEAN);
	}};

}

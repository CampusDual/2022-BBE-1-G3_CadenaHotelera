package com.ontimize.atomicHotelsApiRest.model.core.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("AnswerDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/AnswerDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class AnswerDao extends OntimizeJdbcDaoSupport {
//	
	public static final String TAG = "ans_";
	
	public static final String ATTR_ID = TAG+"id";
	public static final String ATTR_ANSWER = TAG+"answer";
	public static final String ATTR_QUESTION_ID = TAG+"qst_id";
	public static final String ATTR_USER= TAG+"user";
	public static final String ATTR_CUSTOMER_ID = TAG+"htl_id";	
	public static final String ATTR_PUBLIC = TAG+"public";
//
	public static final Map<String,type> fields = new HashMap<>() {{
		put(ATTR_ID,type.INTEGER);		
		put(ATTR_ANSWER,type.NO_EMPTY_STRING);		
		put(ATTR_QUESTION_ID,type.INTEGER);		
		put(ATTR_CUSTOMER_ID,type.INTEGER);		
		put(ATTR_USER,type.NO_EMPTY_SMALL_STRING);		
		put(ATTR_PUBLIC ,type.BOOLEAN);		
	}};

}

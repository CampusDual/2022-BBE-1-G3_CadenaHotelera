package com.ontimize.atomicHotelsApiRest.model.core.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("QuestionDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/QuestionDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class QuestionDao extends OntimizeJdbcDaoSupport {
	
	public static final String TAG = "qst_";
	
	public static final String ATTR_ID = TAG+"id";
	public static final String ATTR_QUESTION = TAG+"question";
	public static final String ATTR_HTL_ID = TAG+"htl_id";
	public static final String ATTR_USER = TAG+"user";
	public static final String ATTR_PUBLIC = TAG+"public";

	public static final Map<String,type> fields = new HashMap<>() {{
		put(ATTR_ID,type.INTEGER);		
		put(ATTR_QUESTION,type.NO_EMPTY_STRING);		
		put(ATTR_HTL_ID,type.INTEGER);		
		put(ATTR_USER,type.NO_EMPTY_SMALL_STRING);		
		put(ATTR_PUBLIC ,type.BOOLEAN);		
	}};

}

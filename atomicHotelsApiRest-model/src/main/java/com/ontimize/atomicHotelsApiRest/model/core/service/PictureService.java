package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.IPictureService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.PictureDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;



@Service("PictureService")
@Lazy

public class PictureService implements IPictureService {
	
	
	@Autowired
	private PictureDao pictureDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Autowired
	ControlFields cf;
	

	@Override
	public EntityResult pictureQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		// TODO Esbozo de método generado automáticamente
		return null;
	}

	@Override
	public EntityResult pictureInsert(Map<String, Object> data) throws OntimizeJEERuntimeException {
		EntityResult resultado=new EntityResultWrong();
		
		FileInputStream fis;
		int longitudBytes;
		String fileName=(String)data.get(pictureDao.ATTR_FILE);
		try {
			fis=new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
		
		
		System.out.println(pictureDao);
		resultado=daoHelper.insert(pictureDao, data);
		
		
		return resultado;
	}

	@Override
	public EntityResult pictureUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		// TODO Esbozo de método generado automáticamente
		return null;
	}

	@Override
	public EntityResult pictureDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		// TODO Esbozo de método generado automáticamente
		return null;
	}

}

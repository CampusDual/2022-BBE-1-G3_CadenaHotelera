package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttachmentRef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceExtraService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServicesXtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.jee.server.dao.IOntimizeDaoSupport;

@Service("HotelServiceExtra")
@Lazy
public class HotelServiceExtra implements IHotelServiceExtraService {

	@Autowired
	private HotelServiceExtraDao hotelServiceExtraDao;
	
	@Autowired
	private HotelService hotelservice;
	
	@Autowired 
	private ServicesXtraService servicesxtraservice;
	
	@Autowired 
	private DefaultOntimizeDaoHelper daoHelper;
	

	
	public EntityResult hotelServiceExtraHotelQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		System.err.println(keyMap);
		System.err.println(attrList);
		return this.daoHelper.query(this.hotelServiceExtraDao, keyMap, attrList);
	}
	
	@Override
	public EntityResult hotelServiceExtraInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException, MissingFieldsException 
	{
	try{
			ValidateFields.required(attrMap, HotelServiceExtraDao.ATTR_ID_HTL, HotelServiceExtraDao.ATTR_ID_SXT);
			System.err.println(attrMap.entrySet());
			return this.daoHelper.insert(this.hotelServiceExtraDao, attrMap);
		} catch (MissingFieldsException e) {
			return new EntityResultWrong(e.getMessage());
		}
	}
	
/*	@Override
	public EntityResult hotelServiceExtraInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException, MissingFieldsException{
	try {
			ValidateFields.required(attrMap, HotelServiceExtraDao.ATTR_ID_HTL, HotelServiceExtraDao.ATTR_ID_SXT);
			System.err.println(attrMap.entrySet());
			Map<String,Object> cHotel = new HashMap<>();
			cHotel.put("htl_id", attrMap.get("htl_id"));
			List<String> where = Arrays.asList((String)attrMap.get("htl_id"));
			if(hotelservice.hotelDataQuery(cHotel, where)!=null)
			{
				return this.daoHelper.insert(this.hotelServiceExtraDao, attrMap);
			}
		} catch (MissingFieldsException e) {
			return new EntityResultWrong(e.getMessage());
		}
		return null;
	}*/
	
	@Override
	public EntityResult hotelServiceExtraUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public EntityResult hotelServiceExtraDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	
}

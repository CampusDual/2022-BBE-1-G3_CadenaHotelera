package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService;

import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.ontimizeExtra.EntityResultWrong;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.atomicHotelsApiRest.model.core.ontimizeExtra.EntityResultWrong;

import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("BookingService")
@Lazy
public class BookingService implements IBookingService {

	@Autowired
	private BookingDao bookingDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult bookingQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = this.daoHelper.query(this.bookingDao, keyMap, attrList);
		return resultado;
	}

	@Override
	public EntityResult bookingInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		if(attrMap.containsKey(BookingDao.ATTR_CHECKIN) && attrMap.containsKey(BookingDao.ATTR_CHECKOUT)) {
			if(((String) attrMap.get(BookingDao.ATTR_CHECKIN)).compareTo((String) attrMap.get(BookingDao.ATTR_CHECKOUT)) >= 0) {
				resultado.setCode(EntityResult.OPERATION_WRONG);
				resultado.setMessage("Checkin no puede ser posterior a checkout");	
			}else {			
			//hariamos cosas
			resultado = this.daoHelper.insert(this.bookingDao, attrMap);
			}
		}else {
			resultado.setCode(EntityResult.OPERATION_WRONG);
			resultado.setMessage("Faltan campos necesarios");
		}
		return resultado;
	}

	@Override
	public EntityResult bookingUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.update(this.bookingDao, attrMap, keyMap);
	}

	@Override
	public EntityResult bookingDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.bookingDao, keyMap);
	}

}
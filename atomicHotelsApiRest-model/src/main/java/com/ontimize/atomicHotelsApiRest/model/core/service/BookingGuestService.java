package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.IBookingGuestService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingGuestDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("BookingGuestService")
@Lazy
public class BookingGuestService implements IBookingGuestService{
	
	@Autowired
	private BookingGuestDao bookingGuestDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Autowired
	ControlFields cf;
	
	 public EntityResult bookingGuestQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException{
		 EntityResult resultado = new EntityResultWrong();
		 resultado = this.daoHelper.query(this.bookingGuestDao, keyMap, attrList);
		 return resultado;
	 }
	 public EntityResult bookingGuestInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException{
		 EntityResult resultado = new EntityResultWrong();
		 resultado = this.daoHelper.insert(this.bookingGuestDao, attrMap);
		 return resultado;
	 }
	 public EntityResult bookingGuestDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException{
		 EntityResult resultado = new EntityResultWrong();
		 
		 resultado=this.daoHelper.delete(this.bookingGuestDao, keyMap);
		 return resultado;
	 }

}

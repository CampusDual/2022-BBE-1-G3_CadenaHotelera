package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.ontimizeExtra.EntityResultWrong;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("CustomerService")
@Lazy
public class CustomerService implements ICustomerService{

 @Autowired private CustomerDao customerDao;
 @Autowired private DefaultOntimizeDaoHelper daoHelper;
 
 @Override
 public EntityResult customerQuery(Map<String, Object> keyMap, List<String> attrList)
   throws OntimizeJEERuntimeException {
  return this.daoHelper.query(this.customerDao, keyMap, attrList);
 }

 @Override
 public EntityResult customerInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
	 EntityResult resultado = new EntityResultMapImpl();
	 
		if (attrMap.containsKey(CustomerDao.ATTR_DNI)) {
			EntityResult auxEntity = this.daoHelper.query(this.customerDao,
					EntityResultTools.keysvalues(CustomerDao.ATTR_DNI, attrMap.get(CustomerDao.ATTR_DNI)),
					EntityResultTools.attributes(CustomerDao.ATTR_DNI));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = this.daoHelper.insert(this.customerDao, attrMap);
			} else {				
				resultado = new EntityResultWrong("Error al crear Customer - El registro ya existe");
			}
		}
		return resultado;
 }

 @Override
 public EntityResult customerUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
   throws OntimizeJEERuntimeException {
  return this.daoHelper.update(this.customerDao, attrMap, keyMap);
 }

 @Override
 public EntityResult customerDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
  return this.daoHelper.delete(this.customerDao, keyMap);
 }

}
